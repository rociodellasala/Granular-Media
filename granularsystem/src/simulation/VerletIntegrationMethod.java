package simulation;

import models.Particle;
import models.Vector2D;

import java.util.Set;

public class VerletIntegrationMethod {
    private double deltaT;


    public VerletIntegrationMethod(double deltaT) {
        this.deltaT = deltaT;

    }

    public void integrate(Set<Particle> particles) {
        for (Particle p : particles) {
            if (p.getPreviousPosition() == null) {
                nextWithEuler(p);
            } else {
                updateParticleWithVerlet(p);
            }
        }
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

        p.setPreviousPosition(p.getPosition());

        nextPositionEuler = currentR.add(currentV.getMultiplied(deltaT)).
                add(currentF.getMultiplied((deltaT*deltaT)/2 * p.getMass()));

        p.setPosition(nextPositionEuler);
    }

    private void updateNextSpeed(Particle p) {
        Vector2D nextSpeedEuler;
        Vector2D currentV = p.getSpeed();
        Vector2D currentF = p.getForce();

        p.setPreviousSpeed(p.getSpeed());

        nextSpeedEuler = currentV.add(currentF.getMultiplied(deltaT/p.getMass()));
        p.setSpeed(nextSpeedEuler);
    }

    private void updateParticleWithVerlet(Particle particle) {
        Vector2D previous = particle.getPreviousPosition();
        updateVerletPosition(particle);
        updateVerletSpeed(particle, previous);

    }

    private void updateVerletPosition(Particle p) {
        Vector2D nextPosition;
        Vector2D currentR = p.getPosition();
        Vector2D currentF = p.getForce();
        Vector2D previousR = p.getPreviousPosition();

        nextPosition = currentR.getMultiplied(2).subtract(previousR).add(currentF
                .getMultiplied((deltaT*deltaT)/2 * p.getMass()));

        p.setPreviousPosition(p.getPosition());
        p.setPosition(nextPosition);
    }

    private void updateVerletSpeed(Particle p, Vector2D previousPos) {
        Vector2D nextSpeed;
        Vector2D nextR = p.getPosition();
        Vector2D previousR = previousPos;

        nextSpeed = nextR.subtract(previousR).getDivided(2 * deltaT);

        p.setPreviousSpeed(p.getSpeed());
        p.setSpeed(nextSpeed);
    }

}
