package simulation;

import models.Particle;
import models.Universe;
import models.Vector2D;
import utils.NeighbourCalculator;
import utils.OvitoGenerator;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Simulation {

    private final static double g = 9.8;                // m/s^2
    private final static double kn = Math.pow(10,5);    // N/m
    private final static double kt = 2 * kn;            // N/m
    private final static double mu = 0.1;               //
    private final static double gamma = 70;             // kg/s

    private double L;
    private double W;
    private double D;

    private Universe universe;
    private double time;
    private double elapsedTime;
    private VerletIntegrationMethod verlet;

    public Simulation(Universe universe) {
        this.universe = universe;
        time = 100;  //config!!
        elapsedTime = 0.0;
        this.L = universe.getLengthY();
        this.W = universe.getLengthX();
        this.D = universe.getHoleSize();
    }

    public void startUniverse(int quantity, double minRadius, double maxRadius, double mass) {
        initializeParticles(quantity, minRadius, maxRadius, mass);
    }

    public Universe getUniverse() {
        return universe;
    }


    public void initializeParticles(int quantity, double minRadius, double maxRadius, double mass) {
        Particle particle;
        double positionX;
        double positionY;
        double radius;

        boolean check;

        for (int i = 0; i < quantity; i++) {
            do {
                positionX = getRandomDouble(0, W);
                positionY = getRandomDouble(0, L);
                radius = getRandomDouble(minRadius, maxRadius);
                check = checkSuperposition(positionX, positionY, radius, W, L);
            } while (!check);
            particle = new Particle(new Vector2D(positionX, positionY), mass, radius, Color.BLUE);
            this.universe.getParticles().add(particle);
        }
    }

    private boolean checkSuperposition(double positionX, double positionY, double radio, double universeLengthX, double universeLengthY) {
        for (Particle particle : this.universe.getParticles()) {
            if (!check(particle, positionX, positionY, radio))
                return false;
        }

        if (positionX + radio > universeLengthX || positionX - radio < 0.0)
            return false;

        return !(positionY + radio > universeLengthY) && !(positionY - radio < 0.0);
    }

    public boolean check(Particle particle, double positionX, double positionY, double radio) {
        double distanceX;
        double distanceY;
        double radios;

        distanceX = Math.pow(positionX - particle.getPosition().getX(), 2);
        distanceY = Math.pow(positionY - particle.getPosition().getY(), 2);
        radios = Math.pow(radio + particle.getRadius(), 2);
        return !(distanceX + distanceY <= radios);
    }

    public double getRandomDouble(double min, double max) {
        Random rand = new Random();
        double randomValue = min + (max - min) * rand.nextDouble();
        return randomValue;
    }

    public void simulate(double deltaT, double deltaT2, NeighbourCalculator neighbourCalculator) {
        int iterations = 0;
        verlet = new VerletIntegrationMethod(deltaT);

        do {
            if (iterations == 0 || iterations % deltaT2 == 0) {
                OvitoGenerator.recopilateData(this);
            }
            checkParticleInteractions(neighbourCalculator);
            iterations++;
            elapsedTime += deltaT;
        } while(isConditionNotComplete(elapsedTime));

    }

    public boolean isConditionNotComplete(double elapsedTime) {
        return (elapsedTime <= time);
    }

    private void checkParticleInteractions(NeighbourCalculator neighbourCalculator) {
        Map<Particle, Set<Particle>> map = neighbourCalculator.getNeighbours(universe.getParticles());

        for (Particle p : this.universe.getParticles()) {
            Set<Particle> neighbours = map.get(p);
            p.setForce(calculateForce(p, neighbours));
            verlet.integrate(universe.getParticles());
            removeFallenParticles();
        }

        // puedo estar seteando fuerzas y posiciones del mismo conjunto q estoy recorriendo
        // podria tener un set de todas mis particulas a eliminar y las nuevas a agregar y cuando finalizo de recorrer
        // hago los cambios
    }

    private Vector2D calculateForce(Particle p, Set<Particle> neighbours) {
        Vector2D force = new Vector2D(0,- p.getMass() * g);
        double overlap;
        double xDistanceFraction, yDistanceFraction, distance;
        double forceX = 0;
        double forceY = 0;
        double normalForce, tangencialForce;

        for(Particle neighbour : neighbours) {
            if(!p.equals(neighbour)) {
                overlap = overlapping(p, neighbour);

                if (overlap > 0) {
                    distance = neighbour.getDistance(p);
                    xDistanceFraction = (neighbour.getPosition().getX() - p.getPosition().getX())/distance;
                    yDistanceFraction = (neighbour.getPosition().getY() - p.getPosition().getY())/distance;
                    Vector2D normalVector = new Vector2D(yDistanceFraction, -xDistanceFraction);
                    double relativeVelocity = getRelativeVelocity(p, neighbour , normalVector);

                    normalForce = -kn * overlap;
                    tangencialForce = -kt * overlap * relativeVelocity;

                    forceX += normalForce * xDistanceFraction + tangencialForce * (-yDistanceFraction);
                    forceY += normalForce * yDistanceFraction + tangencialForce * xDistanceFraction;
                }
            }
        }

        force = force.add(new Vector2D(forceX, forceY));
        force.add(calculateForceFromWalls(force, p));

        return force;
    }

    private static double getRelativeVelocity(Particle one, Particle another, Vector2D tan) {
        Vector2D v = another.getSpeed().subtract(one.getSpeed());
        return v.getX() * tan.getX() + v.getY() * tan.getY();
    }

    public double overlapping(Particle i, Particle j){
        double result = i.getRadius() + j.getRadius() - i.getDistance(j);
        return result > 0 ? result : 0;
    }

    private Vector2D calculateForceFromWalls(Vector2D force, Particle p) {
        return force.add(getWallForces(p));
    }

    private Vector2D getWallForces(Particle p) {
        Vector2D right = rightWall(p);
        Vector2D left = leftWall(p);
        Vector2D horizontal = horizontalWall(p);

        Vector2D total = right.add(left).add(horizontal);

        return total;
    }

    private Vector2D horizontalWall(Particle p){
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getX();

        if (overlap > 0){
            double relativeVelocity = -p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-forceNormalAndTan.getX(), -forceNormalAndTan.getY());
        }

        return force;
    }

    private Vector2D leftWall(Particle p){
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getX();

        if (overlap > 0){
            double relativeVelocity = p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-forceNormalAndTan.getX(), forceNormalAndTan.getY());
        }

        return force;
    }

    private Vector2D rightWall(Particle p) {
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - W + p.getPosition().getX();

        if (overlap > 0){
            double relativeVelocity = -p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(forceNormalAndTan.getX(), -forceNormalAndTan.getY());
        }

        return force;
    }

    private Vector2D getNormalAndTangencialVector(double overlapSize, double relativeVelocity){
        return new Vector2D(
                -kn * overlapSize,
                -kt * overlapSize * relativeVelocity
        );
    }

    private void addFallenParticles(Particle old, Set<Particle> newParticles) {
        Particle p;
        double positionX, positionY;
        int oldId = old.getID();
        double oldMass = old.getMass();
        double oldRadius = old.getRadius();
        Color oldColor = old.getColor();
        newParticles.remove(old);
        boolean check;
        Vector2D position;

        do {
            positionX = getRandomDouble(0, W);
            positionY = getRandomDouble(L - oldRadius, L - L/10);
            position = new Vector2D(positionX, positionY);
            check = checkSuperposition(positionX, positionY, oldRadius, W, L);
        } while (!check);

        p = new Particle(position, oldMass, oldRadius, oldColor, oldId);
        newParticles.add(p);
    }

    private void removeFallenParticles() {
        Set<Particle> particles = new HashSet<>();
        for (Particle p : universe.getParticles()) {
            if(p.getPosition().getY() < (-L/10)) {
                particles.add(p);
            } else {
                System.out.println("SALIO");
                addFallenParticles(p, particles);
            }
        }

        universe.setNewParticles(particles);
    }

}