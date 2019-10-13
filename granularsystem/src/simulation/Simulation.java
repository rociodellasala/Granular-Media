package simulation;

import integrationMethods.IntegrationMethod;
import models.Particle;
import models.Universe;
import models.Vector2D;
import utils.Const;
import utils.OvitoGenerator;

import java.text.DecimalFormat;
import java.util.*;

public class Simulation {

    private Universe universe;
    private double time;
    private double elapsedTime;
    private double fillingPercentage;
    private IntegrationMethod integrationMethod;

    Simulation(Universe universe, double totalTime, double fillingPercentage, IntegrationMethod im) {
        this.universe = universe;
        time = totalTime;
        elapsedTime = 0.0;
        this.fillingPercentage = fillingPercentage;
        this.integrationMethod = im;
    }

    void startUniverse(String quantity_method, int quantity) {
        if (quantity_method.toLowerCase().equals("max")) {
            System.out.println("Creando la maxima cantidad de particulas posibles");
            initializeParticlesMaxCapacity();
        } else {
            System.out.println("Creando " + quantity + " particulas");
            initializeParticlesWithQuantity(quantity);
        }

        System.out.println("Creando paredes");
        double radius = 0.007;
        initializeCornerWalls(radius);
        initializeHoleDelimiterWalls(radius);
    }

    public Universe getUniverse() {
        return universe;
    }

    private void initializeParticlesWithQuantity(int quantity) {
        Particle particle;
        double positionX;
        double positionY;
        double radius;

        boolean check;

        for (int i = 0; i < quantity; i++) {
            do {
                positionX = getRandomDouble(0, Const.W);
                positionY = getRandomDouble(0, Const.L * fillingPercentage);
                radius = getRandomDouble(Const.minRadius, Const.maxRadius);
                check = checkSuperposition(positionX, positionY, radius, Const.W, Const.L);
            } while (!check);
            particle = new Particle(new Vector2D(positionX, positionY), Const.mass, radius, false);
            this.universe.getParticles().add(particle);
        }
    }

    private void initializeParticlesMaxCapacity() {
        Particle particle;
        double positionX;
        double positionY;
        double radius;

        boolean check;
        boolean notfull = true;
        int tries;
        int i;

        for (i = 0; notfull; i++) {
            tries = 0;

            do {
                positionX = getRandomDouble(0, Const.W);
                positionY = getRandomDouble(0, fillingPercentage * Const.L);
                if (tries > Math.pow(10, 5))
                    radius = Const.minRadius;
                else
                    radius = getRandomDouble(Const.minRadius, Const.maxRadius);
                check = checkSuperposition(positionX, positionY, radius, Const.W, Const.L);
                tries++;

                if (tries > Const.MAX_TRIES) {
                    notfull = false;
                    break;
                }
            } while (!check);
            if (check) {
                particle = new Particle(new Vector2D(positionX, positionY), Const.mass, radius, false);
                this.universe.getParticles().add(particle);
            }
        }

        System.out.println("Se pudieron crear " + i + " particulas");
    }

    private void initializeCornerWalls(double radius) {
        Particle corner;

        corner = new Particle(new Vector2D(0.0, Const.L), 0.0, radius, true);
        this.universe.getWalls().add(corner);

        corner = new Particle(new Vector2D(Const.W, Const.L), 0.0, radius, true);
        this.universe.getWalls().add(corner);

        corner = new Particle(new Vector2D(0.0, -Const.L / 10), 0.0, radius, true);
        this.universe.getWalls().add(corner);

        corner = new Particle(new Vector2D(Const.W, -Const.L / 10), 0.0, radius, true);
        this.universe.getWalls().add(corner);

    }

    private void initializeHoleDelimiterWalls(double radius) {
        Particle bottom;
        double a = (Const.W - Const.D) / 2;
        int j = 0;

        for (double i = 0; i < a; i += radius, j++) {
            bottom = new Particle(new Vector2D(radius * j, 0.0), 0.0, radius, true);
            this.universe.getWalls().add(bottom);
        }

        a = Const.D + (Const.W - Const.D) / 2;
        for (double i = Const.W; i > a; i -= radius, j++) {
            bottom = new Particle(new Vector2D(Const.D + radius * j, 0.0), 0.0, radius, true);
            this.universe.getWalls().add(bottom);
        }

    }

    private boolean checkSuperposition(double potentialPositionX, double potentialPositionY,
                                       double potentialRadio, double universeLengthX, double universeLengthY) {
        if (potentialPositionX + potentialRadio > universeLengthX || potentialPositionX - potentialRadio < 0.0)
            return false;

        if (potentialPositionY + potentialRadio > universeLengthY || potentialPositionY - potentialRadio < 0.0)
            return false;

        for (Particle particle : this.universe.getParticles()) {
            if (Math.pow(potentialPositionX - particle.getPosition().getX(), 2) +
                    Math.pow(potentialPositionY - particle.getPosition().getY(), 2) <=
                    Math.pow(potentialRadio + particle.getRadius(), 2))
                return false;
        }

        for (Particle wall : this.universe.getWalls()) {
            if (Math.pow(potentialPositionX - wall.getPosition().getX(), 2) +
                    Math.pow(potentialPositionY - wall.getPosition().getY(), 2) <=
                    Math.pow(potentialRadio + wall.getRadius(), 2))
                return false;
        }

        return true;
    }

    private double getRandomDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    void simulate(double deltaT, double deltaT2) {
        System.out.println("Comenzando la simulación");
        double elapsedDeltaT2 = deltaT2;
        List<double[]> kineticEnergy = new ArrayList<>();
        double kineticE = getEnergy(universe.getParticles());
        List<Double> caudalTimes = new ArrayList<>();
        
        do {
            if (elapsedTime == deltaT || elapsedTime > elapsedDeltaT2) {
                OvitoGenerator.recopilateData(this);
                elapsedDeltaT2 = elapsedTime + deltaT2;
                System.out.println("Elapsed time: " + elapsedTime);
                kineticE = getEnergy(universe.getParticles());
                System.out.println(kineticE);
                double[] current = {elapsedTime, kineticE};
                kineticEnergy.add(current);
            }

            universe.setParticles(integrationMethod.integrate(universe.getParticles()));
            universe.setNewParticles(removeFallenParticles(caudalTimes, elapsedTime));
            elapsedTime += deltaT;
        } while (isConditionNotComplete(elapsedTime, kineticE));


        OvitoGenerator.generateKineticOutput(kineticEnergy);
        OvitoGenerator.generateCaudalOutput(caudalTimes);
    }

    private boolean isConditionNotComplete(double elapsedTime, double kineticEnergy) {
        return (elapsedTime <= time && (elapsedTime < 0.5 || kineticEnergy > 1e-5));
    }


    private void addFallenParticles(Particle old, Set<Particle> newParticles) {
        Particle p;
        double positionX, positionY;
        int oldId = old.getID();
        double oldMass = old.getMass();
        double oldRadius = old.getRadius();
        newParticles.remove(old);
        boolean check;
        double refillLowerLimit = fillingPercentage * Const.L;
        Vector2D position;

        int tries = 0;

        do {
            positionX = getRandomDouble(0, Const.W - Const.maxRadius);
            positionY = getRandomDouble(refillLowerLimit , Const.L - Const.maxRadius);
            position = new Vector2D(positionX, positionY);

            check = checkSuperposition(positionX, positionY, oldRadius, Const.W, Const.L);
            if (tries > Const.MEDIUM_TRIES) {
                refillLowerLimit = fillingPercentage * 0.7 * Const.L;
            }
            tries++;
        } while (!check);
        p = new Particle(position, oldMass, oldRadius, oldId);
        newParticles.add(p);
    }

    private Set<Particle> removeFallenParticles(List<Double> caudalTimes, double elapsedTime) {
        Set<Particle> particles = new HashSet<>();
        
        for (Particle p : universe.getParticles()) {
            if (p.getPosition().getY() > (-Const.L / 10)) {
                particles.add(p);
            } else {
            	caudalTimes.add(elapsedTime);
                addFallenParticles(p, particles);
            }
        }
        
        return particles;
    }

    private double getEnergy(Set<Particle> particles) {
        double speed = 0;

        for(Particle p : particles) {
            speed += Math.pow(p.getSpeed().abs(), 2);
        }

        return 0.5 * Const.mass * speed;
    }

}