package integrationMethods;

import calculators.ForceCalculator;
import calculators.NeighbourCalculator;
import models.Particle;
import models.Vector2D;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VerletIntegrationMethod implements IntegrationMethod {
    private double deltaT;
    private NeighbourCalculator neighbourCalculator;
    private ForceCalculator forceCalculator;
    private Map<Particle, Set<Particle>> map;

    public VerletIntegrationMethod(double deltaT, NeighbourCalculator nc, ForceCalculator fc) {
        this.deltaT = deltaT;
        this.neighbourCalculator = nc;
        this.forceCalculator = fc;
    }

    public Set<Particle> integrate(Set<Particle> particles) {
        map = neighbourCalculator.getNeighbours(particles);
        Set<Particle> auxiliarSet = new HashSet<>(particles);

        for (Particle p : auxiliarSet) {
            if(p.getPreviousPosition() == null) {
                p.setForce(forceCalculator.calculateForce(p, map.get(p)));
                nextWithEuler(p);
            } else {
                p.setForce(forceCalculator.calculateForce(p, map.get(p)));
                updateParticleWithVerlet(p);
            }
        }

        return auxiliarSet;
    }

    private void nextWithEuler(Particle p) {
        updateNextPosition(p);
        updateNextSpeed(p);
        updateAllInformation(p);
    }

    private void updateNextPosition(Particle p) {
        Vector2D nextPositionEuler;
        Vector2D currentR = p.getPosition();
        Vector2D currentV = p.getSpeed();
        Vector2D currentF = p.getForce();

        nextPositionEuler = currentR.add(currentV.multiplyByScalar(deltaT)).
                add(currentF.multiplyByScalar((deltaT * deltaT) / 2 * p.getMass()));

        p.setNextPosition(nextPositionEuler);
    }

    private void updateNextSpeed(Particle p) {
        Vector2D nextSpeedEuler;
        Vector2D currentV = p.getSpeed();
        Vector2D currentF = p.getForce();

        nextSpeedEuler = currentV.add(currentF.multiplyByScalar(deltaT / p.getMass()));

        p.setNextSpeed(nextSpeedEuler);
    }

    private void updateParticleWithVerlet(Particle p) {
        updateVerletPosition(p);
        updateVerletSpeed(p);
        updateAllInformation(p);
    }

    private void updateVerletPosition(Particle p) {
        Vector2D nextPosition;
        Vector2D currentR = p.getPosition();
        Vector2D currentF = p.getForce();
        Vector2D previousR = p.getPreviousPosition();

        nextPosition = currentR.multiplyByScalar(2).subtract(previousR).add(currentF
                .multiplyByScalar((deltaT * deltaT) / 2 * p.getMass()));

        p.setNextPosition(nextPosition);
    }

    private void updateVerletSpeed(Particle p) {
        Vector2D nextSpeed;
        Vector2D nextR = p.getNextPosition();
        Vector2D previousR = p.getPreviousPosition();

        nextSpeed = nextR.subtract(previousR).divideByScalar(2 * deltaT);

        p.setNextSpeed(nextSpeed);
    }

    private void updateAllInformation(Particle p) {
        p.setPreviousSpeed(p.getSpeed());
        p.setSpeed(p.getNextSpeed());
        p.setPreviousPosition(p.getPosition());
        p.setPosition(p.getNextPosition());
        p.reset();
    }


}
