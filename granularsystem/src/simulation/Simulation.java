package simulation;

import models.Particle;
import models.Universe;
import models.Vector2D;
import utils.NeighbourCalculator;
import utils.OvitoGenerator;

import java.awt.*;
import java.util.Random;

public class Simulation {

    private Universe universe;
    private int steps;

    private double time;
    private double elapsedTime;
    private VerletIntegrationMethod verlet;
    private NeighbourCalculator ncalculator;

    public Simulation(Universe universe) {
        this.universe = universe;
        steps = 0;
        time = 0.0;
        elapsedTime = 0.0;
    }

    public void startUniverse(int quantity, double minRadius, double maxRadius, double mass) {
        initializeWalls(minRadius, mass);
        initializeParticles(quantity, minRadius, maxRadius, mass);
    }

    public Universe getUniverse() {
        return universe;
    }

    public void initializeWalls(double minRadius, double mass) {
        initializeCorner(minRadius, mass);
        initializeInnerWalls(minRadius, mass);
    }

    public void initializeCorner(double minRadius, double mass) {
        double radius = minRadius;
        this.universe.getWalls().add(setInitialCorner(this.universe.getLengthX(), 0.0, mass, radius, Color.WHITE));
        this.universe.getWalls().add(setInitialCorner(0.0, 0.0, mass, radius, Color.WHITE));
        this.universe.getWalls().add(setInitialCorner(0.0, this.universe.getLengthY(), mass, radius, Color.WHITE));
        this.universe.getWalls().add(setInitialCorner(this.universe.getLengthX(), this.universe.getLengthY(), mass, radius, Color.WHITE));
    }

    public Particle setInitialCorner(double positionX, double positionY, double mass, double radius, Color color) {
        Vector2D position = new Vector2D(positionX,positionY);
        Vector2D speed = new Vector2D(0.0,0.0);
        Vector2D force = new Vector2D(0.0, 0.0);

        return new Particle(position, speed, force, mass, radius, color);
    }


    public void initializeInnerWalls(double minRadius, double mass) {
        Particle wall;
        double l = universe.getLengthX();
        double w = universe.getLengthY();
        double holesize = universe.getHoleSize();

        /* TAPA */
        double quantity = w / minRadius;
        double lastPosition = 0 + minRadius;

        for(int i = 0 ; i < quantity; i++) {
                wall = new Particle(new Vector2D(lastPosition, l), mass, minRadius, Color.WHITE);
                this.universe.getWalls().add(wall);
                lastPosition += minRadius;
        }


        /* DONDE ESTA HOLE SIZE */
        quantity = w / minRadius - holesize;
        double middle = quantity / 2;
        lastPosition = 0 + minRadius;

        for(int i = 0 ; i < quantity; i++) {
            wall = new Particle(new Vector2D(lastPosition, 0), mass, minRadius, Color.WHITE);
            this.universe.getWalls().add(wall);
            if(lastPosition >= middle){
                lastPosition += middle + minRadius;
            } else {
                lastPosition += minRadius;
            }
        }


        /* PAREDES */
        quantity = l / minRadius;
        lastPosition = 0 + minRadius;

        for(int i = 0 ; i < quantity; i++) {
            wall = new Particle(new Vector2D(0, lastPosition), mass, minRadius, Color.WHITE);
            this.universe.getWalls().add(wall);
            wall = new Particle(new Vector2D(w, lastPosition), mass, minRadius, Color.WHITE);
            this.universe.getWalls().add(wall);
            lastPosition += minRadius;
        }

    }

    public void initializeParticles(int quantity, double minRadius, double maxRadius, double mass) {
        Particle particle;
        double positionX;
        double positionY;
        double radius;
        double l = universe.getLengthX();
        double w = universe.getLengthY();

        boolean check = false;

        for(int i = 0; i < quantity; i++ ){
            do {
                positionX = getRandomDouble(0 + minRadius, w - minRadius);
                positionY = getRandomDouble(0 + minRadius, l - minRadius);
                radius = getRandomDouble(minRadius, maxRadius);
                check = checkSuperposition(positionX, positionY, radius, w, l);
            } while(!check);
            particle = new Particle(new Vector2D(positionX,positionY), mass, radius, Color.BLUE);
            this.universe.getParticles().add(particle);
        }
    }

    private boolean checkSuperposition(double positionX, double positionY, double radio, double universeLengthX, double universeLengthY) {
        for(Particle particle : this.universe.getParticles()) {
            if(!check(particle, positionX, positionY, radio))
                return false;
        }

        for(Particle particle: this.universe.getWalls()) {
            if(!check(particle, positionX, positionY, radio))
                return false;
        }

        if(positionX + radio > universeLengthX || positionX - radio < 0.0)
            return false;

        return !(positionY + radio > universeLengthY) && !(positionY - radio < 0.0);
    }

    public boolean check(Particle particle, double positionX, double positionY, double radio) {
        double distanceX;
        double distanceY;
        double radios;

        distanceX = Math.pow(positionX - particle.getPositionX(), 2);
        distanceY = Math.pow(positionY - particle.getPositionY(), 2);
        radios = Math.pow(radio + particle.getRadius(), 2);
        return !(distanceX + distanceY <= radios);
    }

    public double getRandomDouble(double min, double max) {
        Random rand = new Random();
        double randomValue = min + (max - min) * rand.nextDouble();
        return randomValue;
    }


    public void simulate(ForceCalculator fc, double deltaT, double deltaT2, NeighbourCalculator neighbourCalculator){
        int iterations = 0;
        verlet = new VerletIntegrationMethod(fc, deltaT, universe.getParticles(), neighbourCalculator);

        do {
            verlet.updateParticle(universe.getParticles(), iterations == 0);
            iterations++;
            if(iterations == 0 || iterations % deltaT2 == 0) {
                OvitoGenerator.recopilateData(this);
            }
            elapsedTime += deltaT;
        } while(!isConditionComplete(elapsedTime));

    }


    public boolean isConditionComplete(double elapsedTime) {
        if(elapsedTime <= time) {
            return false;
        }

        return true;
    }
}