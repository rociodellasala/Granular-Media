package utils;

import models.Particle;
import models.Vector2D;

import java.util.Set;

public class ForceCalculator {

    private final static double g = 9.8;                // m/s^2
    private final static double kn = Math.pow(10,5);    // N/m
    private final static double kt = 2 * kn;            // N/m
    private double L;
    private double W;
    private double D;

    public ForceCalculator(double w, double l, double d) {
        this.L = l;
        this.W = w;
        this.D = d;
    }

    public Vector2D calculateForce(Particle p, Set<Particle> neighbours) {
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
        force = force.add(calculateForceFromWalls(force, p));

        return force;
    }

    private Vector2D getNormalAndTangencialVector(double overlap, double relativeVelocity){
        return new Vector2D(
                -kn * overlap,
                -kt * overlap * relativeVelocity
        );
    }

    private static double getRelativeVelocity(Particle one, Particle another, Vector2D tan) {
        Vector2D v = another.getSpeed().subtract(one.getSpeed());
        return v.getX() * tan.getX() + v.getY() * tan.getY();
    }

    public double overlapping(Particle i, Particle j){
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
        Vector2D total = right.add(left).add(bottom).add(top);

        return total;
    }

    private Vector2D bottomWall(Particle p){
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getY();

        boolean shouldCrashBottom = (p.getPosition().getX() < (W/2 - D/2) || p.getPosition().getX() > W - (W/2 - D/2))
                && p.getPosition().getY() > 0;

        if(shouldCrashBottom && overlap > 0){
            double relativeVelocity = - p.getSpeed().getX();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-forceNormalAndTan.getY(), -forceNormalAndTan.getX());
        }

        return force;
    }

    private Vector2D topWall(Particle p){
        Vector2D force = Vector2D.ZERO;
        double overlap = + p.getRadius() + p.getPosition().getY() - L;

        if (overlap > 0){
            double relativeVelocity = p.getSpeed().getX();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(forceNormalAndTan.getY(), forceNormalAndTan.getX());
        }

        return force;
    }

    private Vector2D leftWall(Particle p){
        Vector2D force = Vector2D.ZERO;
        double overlap = p.getRadius() - p.getPosition().getX();

        if (overlap > 0){
            double relativeVelocity = p.getSpeed().getY();
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlap, relativeVelocity);
            force = new Vector2D(-1 * forceNormalAndTan.getX(), forceNormalAndTan.getY());
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
}
