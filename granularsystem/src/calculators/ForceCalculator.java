package calculators;

import models.Particle;
import models.Vector2D;
import utils.Const;

import java.util.Set;

public class ForceCalculator {

    public ForceCalculator() {
    }

    public Vector2D calculateForce(Particle p, Set<Particle> neighbours) {
        Vector2D force = new Vector2D(0, -p.getMass() * Const.g);
        double overlap, derivativeOverlap;
        double xDistanceFraction, yDistanceFraction;
        double forceX = 0;
        double forceY = 0;
        double normalForce, tangencialForce;

        for (Particle neighbour : neighbours) {
            if (!p.equals(neighbour)) {
                overlap = overlapping(p, neighbour);

                if (overlap > 0) {
                    derivativeOverlap = overlappingDerivation(p, neighbour);

                    xDistanceFraction = (neighbour.getPosition().getX() - p.getPosition().getX());
                    yDistanceFraction = (neighbour.getPosition().getY() - p.getPosition().getY());
                    double enT = Math.sqrt(Math.pow(xDistanceFraction, 2) + Math.pow(yDistanceFraction, 2));

                    Vector2D tangencialVector = new Vector2D(-yDistanceFraction, xDistanceFraction);
                    Vector2D relativeVelocity = p.getSpeed().subtract(neighbour.getSpeed());

                    normalForce = -Const.kn * overlap - Const.gamma * derivativeOverlap;
                    tangencialForce = -Const.kt * overlap * relativeVelocity.multiplyByVector(tangencialVector);

                    Vector2D enV = new Vector2D(xDistanceFraction, yDistanceFraction);
                    Vector2D en = enV.divideByScalar(enT);

                    forceX += normalForce * en.x + tangencialForce * (-en.y);
                    forceY += normalForce * en.y + tangencialForce * en.x;
                }
            }
        }

        force = force.add(new Vector2D(forceX, forceY));
        force = force.add(getWallForces(p));

        return force;
    }

    private double overlapping(Particle i, Particle j) {
        double result = i.getRadius() + j.getRadius() - i.getDistance(j);
        return result >= 0 ? result : 0;
    }

    private double overlappingDerivation(Particle i, Particle j) {
        double directionX = j.getPosition().getX() - i.getPosition().getX();
        double directionY = j.getPosition().getY() - i.getPosition().getY();
        Vector2D direction = new Vector2D(directionX, directionY);

        double result = i.getSpeed().projectedOn(direction) - j.getSpeed().projectedOn(direction);

        return overlapping(i, j) > 0 ? result : 0;
    }


    private Vector2D getWallForces(Particle p) {
        Vector2D top = topWall(p);
        Vector2D left = leftWall(p);
        Vector2D right = rightWall(p);
        Vector2D horizontal = bottomWall(p);

        return right.add(left).add(horizontal).add(top);
    }

    private Vector2D bottomWall(Particle p) {
        double derivativeOverlap, overlap;
        double enx, eny;
        Vector2D force = Vector2D.ZERO;

        boolean shouldCrashBottom = (p.getPosition().getX() < (Const.W / 2 - Const.D / 2)
                || p.getPosition().getX() > Const.W - (Const.W / 2 - Const.D / 2))
                && p.getPosition().getY() > 0;

        if (shouldCrashBottom && p.getPosition().getY() - p.getRadius() < 0) {
            overlap = p.getRadius() - p.getPosition().getY();
            derivativeOverlap = p.getSpeed().projectedOn(new Vector2D(0, -1));
            enx = 0;
            eny = -1;
            force = calculateForce(p, overlap, derivativeOverlap, eny, enx);
        }

        return force;
    }

    private Vector2D topWall(Particle p) {
        double derivativeOverlap, overlap;
        double enx, eny;
        Vector2D force = Vector2D.ZERO;

        if (p.getPosition().getY() + p.getRadius() > Const.L) {
            overlap = p.getPosition().getY() + p.getRadius() - Const.L;
            derivativeOverlap = p.getSpeed().projectedOn(new Vector2D(0, 1));
            enx = 0;
            eny = 1;
            force = calculateForce(p, overlap, derivativeOverlap, eny, enx);
        }

        return force;

    }

    private Vector2D leftWall(Particle p) {
        double derivativeOverlap, overlap;
        double enx, eny;
        Vector2D force = Vector2D.ZERO;

        if (p.getPosition().getX() - p.getRadius() < 0) {
            overlap = p.getRadius() - p.getPosition().getX();
            derivativeOverlap = p.getSpeed().projectedOn(new Vector2D(-1, 0));
            enx = -1;
            eny = 0;
            force = calculateForce(p, overlap, derivativeOverlap, eny, enx);
        }

        return force;
    }

    private Vector2D rightWall(Particle p) {
        double derivativeOverlap, overlap;
        double enx, eny;
        Vector2D force = Vector2D.ZERO;

        if (p.getPosition().getX() + p.getRadius() > Const.W) {
            overlap = p.getPosition().getX() + p.getRadius() - Const.W;
            derivativeOverlap = p.getSpeed().projectedOn(new Vector2D(1, 0));
            enx = 1;
            eny = 0;
            force = calculateForce(p, overlap, derivativeOverlap, eny, enx);
        }

        return force;
    }

    private Vector2D calculateForce(Particle p, double overlap, double derivativeOverlap, double eny, double enx) {
        double fn, ft;

        fn = -Const.kn * overlap - Const.gamma * derivativeOverlap;

        Vector2D relativeVelocity = p.getSpeed().subtract(Vector2D.ZERO);
        Vector2D tangentVector = new Vector2D(-eny, enx);
        ft = -Const.kt * overlap * (relativeVelocity.multiplyByVector(tangentVector));

        return new Vector2D(fn * enx - ft * eny, fn * eny + ft * enx);
    }
}
