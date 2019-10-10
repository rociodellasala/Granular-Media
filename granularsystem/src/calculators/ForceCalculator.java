package calculators;

import models.Particle;
import models.Vector2D;
import utils.Const;

import java.util.Set;

public class ForceCalculator {

    public ForceCalculator() { }

    public Vector2D calculateForce(Particle p, Set<Particle> neighbours) {
        Vector2D force = new Vector2D(0, -p.getMass() * Const.g);
        double overlap;
        double xDistanceFraction, yDistanceFraction, distance;
        double forceX = 0;
        double forceY = 0;
        double normalForce, tangencialForce;

        for (Particle neighbour : neighbours) {
            if (!p.equals(neighbour)) {
                overlap = overlapping(p, neighbour);

                if (overlap > 0) { //TODO: > o >= ???? DUDA
                    distance = neighbour.getDistance(p);
                    xDistanceFraction = (neighbour.getPosition().getX() - p.getPosition().getX()) / distance;
                    yDistanceFraction = (neighbour.getPosition().getY() - p.getPosition().getY()) / distance;

                    Vector2D tangencialVector = new Vector2D(-yDistanceFraction, xDistanceFraction);
                    Vector2D relativeVelocity = p.getSpeed().subtract(neighbour.getSpeed());

                    normalForce = -Const.kn * overlap;
                    tangencialForce = -Const.kt * overlap * relativeVelocity.multiplyByVector(tangencialVector);

                    forceX += normalForce * xDistanceFraction + tangencialForce * (-yDistanceFraction);
                    forceY += normalForce * yDistanceFraction + tangencialForce * xDistanceFraction;
                }
            }
        }

        force = force.add(new Vector2D(forceX, forceY));
        force = force.add(calculateForceFromWalls(force, p));

        return force;
    }

    private Vector2D getNormalAndTangencialVector(double overlap, double relativeVelocity) {
        return new Vector2D(
                -Const.kn * overlap,
                -Const.kt * overlap * relativeVelocity
        );
    }

    private double overlapping(Particle i, Particle j) {
        double result = i.getRadius() + j.getRadius() - i.getDistance(j);
        return result >= 0 ? result : 0;
    }

    private Vector2D calculateForceFromWalls(Vector2D force, Particle p) {
        return force.add(getWallForces(p));
    }

    private Vector2D getWallForces(Particle p) {
        Vector2D right = rightWall(p);
        Vector2D left = leftWall(p);
        Vector2D bottom = bottomWall(p);
        Vector2D top = topWall(p);

        return right.add(left).add(bottom).add(top);
    }

    private Vector2D bottomWall(Particle p) {
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getY();

        boolean shouldCrashBottom = (p.getPosition().getX() < (Const.W / 2 - Const.D / 2) || p.getPosition().getX() >
                Const.W - (Const.W / 2 - Const.D / 2))
                && p.getPosition().getY() > 0;

        if (shouldCrashBottom && overlap > 0) {
            double relativeVelocity = -p.getSpeed().getX();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-forceNormalAndTan.getY(), -forceNormalAndTan.getX());
        }

        return force;
    }

    private Vector2D topWall(Particle p) {
        Vector2D force = Vector2D.ZERO;
        double overlap = +p.getRadius() + p.getPosition().getY() - Const.L;

        if (overlap > 0) {
            double relativeVelocity = p.getSpeed().getX();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(forceNormalAndTan.getY(), forceNormalAndTan.getX());
        }

        return force;
    }

    private Vector2D leftWall(Particle p) {
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getX();

        if (overlap > 0) {
            double relativeVelocity = p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-1 * forceNormalAndTan.getX(), forceNormalAndTan.getY());
        }

        return force;
    }

    private Vector2D rightWall(Particle p) {
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - Const.W + p.getPosition().getX();

        if (overlap > 0) {
            double relativeVelocity = -p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(forceNormalAndTan.getX(), -forceNormalAndTan.getY());
        }

        return force;
    }
}
