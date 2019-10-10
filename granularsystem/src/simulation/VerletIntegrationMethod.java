package simulation;

import models.Particle;
import models.Vector2D;
import utils.ForceCalculator;
import utils.NeighbourCalculator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VerletIntegrationMethod implements IntegrationMethod {
    private double deltaT;

    public VerletIntegrationMethod(double deltaT) {
        this.deltaT = deltaT;
    }

    public Set<Particle> integrate(Set<Particle> particles, NeighbourCalculator neighbourCalculator, ForceCalculator fc) {
        Map<Particle, Set<Particle>> map = neighbourCalculator.getNeighbours(particles);

        for (Particle p : particles) {
            Set<Particle> neighbours = map.get(p);
            p.setForce(fc.calculateForce(p, neighbours));
            if (p.getPreviousPosition() == null) {
                nextWithEuler(p);
            } else {
                updateParticleWithVerlet(p);
            }
        }

        return updateParticles(particles);
    }

    private void nextWithEuler(Particle particle) {
        updateNextPosition(particle);
        updateNextSpeed(particle);
    }

    private void updateNextPosition(Particle p) {
        Vector2D nextPositionEuler;
        Vector2D currentR = p.getPosition();
        Vector2D currentV = p.getSpeed();
        Vector2D currentF = p.getForce();

        nextPositionEuler = currentR.add(currentV.getMultiplied(deltaT)).
                add(currentF.getMultiplied((deltaT*deltaT)/2 * p.getMass()));

        p.setNextPosition(nextPositionEuler);
    }

    private void updateNextSpeed(Particle p) {
        Vector2D nextSpeedEuler;
        Vector2D currentV = p.getSpeed();
        Vector2D currentF = p.getForce();

        nextSpeedEuler = currentV.add(currentF.getMultiplied(deltaT/p.getMass()));
        p.setNextSpeed(nextSpeedEuler);
    }

    private void updateParticleWithVerlet(Particle particle) {
        updateVerletPosition(particle);
        updateVerletSpeed(particle);

    }

    private void updateVerletPosition(Particle p) {
        Vector2D nextPosition;
        Vector2D currentR = p.getPosition();
        Vector2D currentF = p.getForce();
        Vector2D previousR = p.getPreviousPosition();


        nextPosition = currentR.getMultiplied(2).subtract(previousR).add(currentF
                .getMultiplied((deltaT*deltaT)/2 * p.getMass()));

        p.setNextPosition(nextPosition);
    }

    private void updateVerletSpeed(Particle p) {
        Vector2D nextSpeed;
        Vector2D nextR = p.getNextPosition();
        Vector2D previousR = p.getPreviousPosition();

        nextSpeed = nextR.subtract(previousR).getDivided(2 * deltaT);

        p.setNextSpeed(nextSpeed);
    }

    private Set<Particle> updateParticles(Set<Particle> particles) {
        Set<Particle> updatedParticles = new HashSet<>();

        for(Particle p : particles) {
            p.setPreviousSpeed(p.getSpeed());
            p.setSpeed(p.getNextSpeed());
            p.setPreviousPosition(p.getPosition());
            p.setPosition(p.getNextPosition());
            p.reset();
            updatedParticles.add(p);
        }

        return updatedParticles;
    }

}
