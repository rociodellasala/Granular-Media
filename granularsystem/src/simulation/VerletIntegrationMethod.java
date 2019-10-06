package simulation;

import models.Particle;
import models.Vector2D;
import utils.NeighbourCalculator;

import java.util.*;

public class VerletIntegrationMethod {
    private double deltaT;
    private ForceCalculator forceCalculator;
    private NeighbourCalculator neighbourCalculator;
    private Map<Particle, Set<Particle>> neighbours;

    public VerletIntegrationMethod(ForceCalculator fc, double deltaT, Set<Particle> particles, NeighbourCalculator nc) {
        this.deltaT = deltaT;
        this.forceCalculator = fc;
        this.initializeNeighbours(particles);
        this.neighbourCalculator = nc;
    }

    private void initializeNeighbours( Set<Particle> particles) {
        this.neighbours = new HashMap<>();
        for(Particle p : particles){
            neighbours.put(p, Collections.emptySet());
        }
    }

    public void updateParticle(Set<Particle> particles, boolean first) {
        if(first) {
            calculateNextPositionEuler(particles);
            calculateNextVelocityEuler(particles);
        } else {
            neighbours = neighbourCalculator.getNeighbours(particles);
            calculateNextPosition(particles);
            calculateNextVelocity(particles);
        }

    }



    private void calculateNextPosition(Set<Particle> particles) {
        for (Particle p : particles) {
            Vector2D r = p.getPosition();
            Vector2D prevR = p.getPreviousPosition();
            Vector2D f = forceCalculator.calculate(p, neighbours.get(p));

            Vector2D nextPosition = r.scalarMultiply(2).subtract(prevR).add(f.scalarMultiply((deltaT*deltaT/p.getMass())));

            p.setPosition(nextPosition);
        }
    }

    private void calculateNextVelocity(Set<Particle> particles) {
        for(Particle p : particles) {
            Vector2D nextR = p.getPosition();
            Vector2D prevR = p.getPreviousPosition();

            Vector2D nextVelocity = nextR.subtract(prevR).getDivided(2 * deltaT);

            p.setSpeed(nextVelocity);
        }
    }

    private void calculateNextPositionEuler(Set<Particle> particles) {
        for(Particle p : particles) {
            Vector2D r = p.getPosition();
            Vector2D v = p.getSpeed();
            Vector2D f = forceCalculator.calculate(p, neighbours.get(p));

            Vector2D nextPositionEuler = r.add(v.getMultiplied(deltaT)).add(f.getMultiplied((deltaT*deltaT)/(2 * p.getMass())));
            p.setPosition(nextPositionEuler);
        }
    }

    private void calculateNextVelocityEuler(Set<Particle> particles) {
        for(Particle p : particles) {
            Vector2D v = p.getSpeed();
            Vector2D f = forceCalculator.calculate(p, neighbours.get(p));

            Vector2D nextVelocityEuler = v.add(f.getMultiplied(deltaT/p.getMass()));
            p.setSpeed(nextVelocityEuler);
        }
    }

}
