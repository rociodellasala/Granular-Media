package simulation;

import models.Particle;
import models.Universe;
import models.Vector2D;
import utils.ForceCalculator;
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
    private final static double MAX_TRIES = Math.pow(10,8);
    private double L;
    private double W;
    private double D;

    private Universe universe;
    private double time;
    private double elapsedTime;
    private VerletIntegrationMethod verlet;
    private ForceCalculator fc;

    public Simulation(Universe universe) {
        this.universe = universe;
        time = 5;  //config!!
        elapsedTime = 0.0;
        this.L = universe.getLengthY();
        this.W = universe.getLengthX();
        this.D = universe.getHoleSize();
        this.fc = new ForceCalculator(W, L, D);
    }

    public void startUniverse(String quantity_method, int quantity, double minRadius, double maxRadius, double mass) {
        if(quantity_method.toLowerCase().equals("max"))
            initializeParticlesMaxCapacity(minRadius, maxRadius, mass);
        else
            initializeParticlesWithQuantity(quantity, minRadius, maxRadius, mass);
        initializeWalls();
    }

    public Universe getUniverse() {
        return universe;
    }


    public void initializeParticlesWithQuantity(int quantity, double minRadius, double maxRadius, double mass) {
        Particle particle;
        double positionX;
        double positionY;
        double radius;

        boolean check;

        for (int i = 0; i < quantity ; i++) {
            do {
                positionX = getRandomDouble(0, W);
                positionY = getRandomDouble(0, L - L/3);
                radius = getRandomDouble(minRadius, maxRadius);
                check = checkSuperposition(positionX, positionY, radius, W, L);
            } while (!check);
            particle = new Particle(new Vector2D(positionX, positionY), mass, radius, Color.BLUE);
            this.universe.getParticles().add(particle);

        }
    }

    public void initializeParticlesMaxCapacity(double minRadius, double maxRadius, double mass) {
        Particle particle;
        double positionX;
        double positionY;
        double radius;

        boolean check;
        boolean notfull = true;
        int tries;
        int i;

        for (i = 0; notfull ; i++) {
            tries = 0;

            do {
                positionX = getRandomDouble(0, W);
                positionY = getRandomDouble(0, L - L/3);
                if(tries > Math.pow(10,5))
                    radius = minRadius;
                else
                    radius = getRandomDouble(minRadius, maxRadius);
                check = checkSuperposition(positionX, positionY, radius, W, L);
                tries++;

                if(tries > MAX_TRIES) {
                    notfull = false;
                    break;
                }
            } while (!check);
            if(check == true) {
                particle = new Particle(new Vector2D(positionX, positionY), mass, radius, Color.BLUE);
                this.universe.getParticles().add(particle);
            }
        }

        System.out.println("Se pudieron crear " + i + " particulas");
    }

    public void initializeWalls(){
        Particle corner;
        Particle bottom;
        double radius = 0.003;

        corner = new Particle(new Vector2D(0.0, 0.0), 0.0, radius, Color.WHITE);
        this.universe.getWalls().add(corner);

        corner = new Particle(new Vector2D(W, 0.0), 0.0, radius , Color.WHITE);
        this.universe.getWalls().add(corner);

        corner = new Particle(new Vector2D(0.0, L), 0.0, radius, Color.WHITE);
        this.universe.getWalls().add(corner);

        corner = new Particle( new Vector2D(W, L), 0.0, radius, Color.WHITE);
        this.universe.getWalls().add(corner);

        double a = (W - D)/2;
        int j = 1;

        for (double i = radius*2; i < a; i += radius, j++) {
            bottom = new Particle(new Vector2D(radius*j, 0.0), 0.0, 0.003, Color.WHITE);
            this.universe.getWalls().add(bottom);
        }

        a = D + (W - D)/2;
        for (double i = W-radius; i > a; i -= radius, j++) {
            bottom = new Particle(new Vector2D(D + radius*j, 0.0), 0.0, 0.003, Color.WHITE);
            this.universe.getWalls().add(bottom);
        }

        bottom = new Particle(new Vector2D((W - D)/2, 0.0), 0.00, 0.003, Color.WHITE);
        this.universe.getWalls().add(bottom);
        bottom = new Particle(new Vector2D(D + (W - D)/2, 0.0), 0.0, 0.003, Color.WHITE);
        this.universe.getWalls().add(bottom);


    }

    private boolean checkSuperposition(double potentialPositionX, double potentialPositionY,
                                       double potentialRadio, double universeLengthX, double universeLengthY) {
        if(potentialPositionX + potentialRadio > universeLengthX || potentialPositionX - potentialRadio < 0.0)
            return false;

        if(potentialPositionY + potentialRadio > universeLengthY || potentialPositionY - potentialRadio < 0.0)
            return false;

        for(Particle particle : this.universe.getParticles()) {
            if(Math.pow(potentialPositionX - particle.getPosition().getX(), 2) +
                    Math.pow(potentialPositionY - particle.getPosition().getY(), 2) <=
                    Math.pow(potentialRadio + particle.getRadius(), 2))
                return false;
        }

        for(Particle wall: this.universe.getWalls()) {
            if(Math.pow(potentialPositionX - wall.getPosition().getX(), 2) +
                    Math.pow(potentialPositionY - wall.getPosition().getY(), 2) <=
                    Math.pow(potentialRadio + wall.getRadius(), 2))
                return false;
        }

        return true;
    }

    public double getRandomDouble(double min, double max) {
        Random rand = new Random();
        double randomValue = min + (max - min) * rand.nextDouble();
        return randomValue;
    }

    public void simulate(double deltaT, double deltaT2, NeighbourCalculator neighbourCalculator) {
        verlet = new VerletIntegrationMethod(deltaT);
        double elapsedDeltaT2 = deltaT2;

        do {
            if (elapsedTime == 0 || elapsedTime > elapsedDeltaT2) {
                OvitoGenerator.recopilateData(this);
                elapsedDeltaT2 = elapsedTime + deltaT2;
                System.out.println(elapsedTime);
            }
            checkParticleInteractions(neighbourCalculator);
            elapsedTime += deltaT;
        } while(isConditionNotComplete(elapsedTime));

    }

    public boolean isConditionNotComplete(double elapsedTime) {
        return (elapsedTime <= time);
    }

    private void checkParticleInteractions(NeighbourCalculator neighbourCalculator) {
        Map<Particle, Set<Particle>> map = neighbourCalculator.getNeighbours(universe.getParticles());
        Set<Particle> auxiliar = new HashSet<>();
        auxiliar.addAll(universe.getParticles());

        for (Particle p : universe.getParticles()) {
            Set<Particle> neighbours = map.get(p);
            p.setForce(fc.calculateForce(p, neighbours));
            verlet.integrate(auxiliar);
        }

        universe.setParticles(auxiliar);
        universe.setNewParticles(removeFallenParticles());
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
            positionY = getRandomDouble(L-L/4, L);
            position = new Vector2D(positionX, positionY);

            check = checkSuperposition(positionX, positionY, oldRadius, W, L);
        } while (!check);
        p = new Particle(position, oldMass, oldRadius, oldColor, oldId);
        newParticles.add(p);
    }

    private Set<Particle> removeFallenParticles() {
        Set<Particle> particles = new HashSet<>();

        for (Particle p : universe.getParticles()) {
            if (p.getPosition().getY() > (-L/Math.pow(10, 8))) {
                particles.add(p);
            } else {
                addFallenParticles(p, particles);
            }
        }

        return particles;
    }

}