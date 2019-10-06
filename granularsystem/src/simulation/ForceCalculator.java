package simulation;

import models.Particle;
import models.Vector2D;

import java.util.Set;

public class ForceCalculator {
    private static double g;
    private static double kn;
    private static double kt;
    private static double mu;
    private static double gamma;

    private double L, W, D;


    public ForceCalculator(double L, double W, double D, double gamma, double kn, double kt, double g, double mu) {
        this.L = L;
        this.W = W;
        this.D = D;
        ForceCalculator.gamma = gamma;
        ForceCalculator.kn = kn;
        ForceCalculator.kt = kt * kn;
        ForceCalculator.g = g;
        ForceCalculator.mu = mu;
    }

    public Vector2D calculate(Particle p, Set<Particle> neighbours) {
        Vector2D force = new Vector2D(0,- p.getMass() * g);
        double overlap, d;

        double totalFn = 0;

        for(Particle neighbour : neighbours) {
            if(!p.equals(neighbour)) {
                overlap = overlapping(p, neighbour);
                d = overlappingDerivation(p, neighbour);
                if(overlap > 0){
                    double nForce = nForce(overlap, d);

                    totalFn += nForce;

                    double enX = neighbour.getPosition().getX() - p.getPosition().getX();
                    double enY = neighbour.getPosition().getY() - p.getPosition().getY();
                    double enT = Math.sqrt(Math.pow(enX,2) + Math.pow(enY,2));

                    Vector2D relativeVelocity = p.getSpeed().subtract(neighbour.getSpeed());
                    Vector2D tangentVector = new Vector2D(-enY, enX);
                    double tForce = -kt * overlap * (relativeVelocity.dot(tangentVector));


                    Vector2D enV = new Vector2D(enX, enY);
                    Vector2D en = enV.getDivided(enT);

                    Vector2D newForce = new Vector2D(nForce * en.x - tForce * en.y,nForce * en.y + tForce * en.x);

                    force = force.add(newForce);
                }
            }
        }

        p.setNormalForce(totalFn);
        force = force.add(getWallForces(p));

        return force;
    }



    private double nForce(double overlap, double dOverlap) {
        return -kn * overlap - gamma * dOverlap;
    }

    private double tForce(double nForce, double vr) {
        return - mu * Math.abs(nForce) * Math.signum(vr);
    }

    public double overlapping(Particle i, Particle j){
        double resultX = Math.abs(i.getPosition().getX() - j.getPosition().getX());
        double resultY = Math.abs(i.getPosition().getY() - j.getPosition().getY());

        double result = i.getRadius() + j.getRadius() - Math.sqrt(Math.pow(resultX, 2) + Math.pow(resultY,2));

        if (result > 0) {
            return result;
        } else {
            return 0;
        }
    }

    public double overlappingDerivation(Particle i, Particle j){
        double directionX = j.getPosition().getX() - i.getPosition().getX();
        double directionY = j.getPosition().getY() - i.getPosition().getY();
        Vector2D direction = new Vector2D(directionX, directionY);

        double result = i.getSpeed().project(direction) - j.getSpeed().project(direction);

        if(overlapping(i, j) > 0) {
            return result;
        }
        return 0;
    }


    private double relativeVelocity(Particle i, Particle j) {
        double directionX = j.getPosition().getX() - i.getPosition().getX();
        double directionY = j.getPosition().getY() - i.getPosition().getY();
        Vector2D direction = new Vector2D(directionX, directionY).tangent();

        double speedX = i.getSpeed().getX() - j.getSpeed().getX();
        double speedY = i.getSpeed().getY() - j.getSpeed().getY();
        Vector2D speedV = new Vector2D(speedX, speedY);

        double rv = speedV.project(direction);

        return rv;
    }

    private Vector2D getWallForces(Particle p) {
        Vector2D right = rightWall(p);
        Vector2D left = leftWall(p);
        Vector2D horizontal = horizontalWall(p);
        Vector2D total = right.add(left).add(horizontal);

        return total;
    }

    private Vector2D horizontalWall(Particle p){
        double dervOver = 0, overlap = 0;
        double enx = 0, eny = 0;
        double fn, ft;

        boolean shouldCrashBottom = (p.getPosition().getX() < (W/2 - D/2) || p.getPosition().getX() > W - (W/2 - D/2))
                && p.getPosition().getY() > 0;

        if(shouldCrashBottom && p.getPosition().getY() - p.getRadius() < 0) {
            overlap = p.getRadius() - p.getPosition().getY();
            dervOver = p.getSpeed().project(new Vector2D(0,-1));
            enx = 0;
            eny = -1;
        }

        fn = nForce(overlap, dervOver);

        Vector2D relativeVelocity = p.getSpeed().subtract(Vector2D.ZERO);
        Vector2D tangentVector = new Vector2D(-eny, enx);
        ft  = -kt * overlap * (relativeVelocity.dot(tangentVector));


        Vector2D force = new Vector2D(fn * enx - ft * eny, fn * eny + ft * enx);
        return force;
    }

    private Vector2D leftWall(Particle p){
        double overlap = 0, dervOver = 0;
        double enx = 0, eny = 0;
        double fn, ft;
        if(p.getPosition().getX() - p.getRadius() < 0){
            overlap = p.getRadius() - p.getPosition().getX();
            dervOver = p.getSpeed().project(new Vector2D(-1, 0));
            enx = -1;
            eny = 0;
        }

        fn = nForce(overlap,dervOver);

        Vector2D relativeVelocity = p.getSpeed().subtract(Vector2D.ZERO);
        Vector2D tangentVector = new Vector2D(-eny, enx);
        ft  = -kt * overlap * (relativeVelocity.dot(tangentVector));

        Vector2D force = new Vector2D(fn * enx - ft * eny, fn * eny + ft * enx);
        return force;
    }

    private Vector2D rightWall(Particle p) {
        double dervOver = 0;
        double overlap = 0;
        double enx = 0, eny = 0;
        double fn, ft;


        if(p.getPosition().getX() + p.getRadius() > W){
            overlap = p.getPosition().getX() + p.getRadius() - W;
            dervOver = p.getSpeed().project(new Vector2D(1, 0));
            enx = 1;
            eny = 0;
        }

        fn = nForce(overlap,dervOver);
        Vector2D relativeVelocity = p.getSpeed().subtract(Vector2D.ZERO);
        Vector2D tangentVector = new Vector2D(-eny, enx);
        ft  = -kt * overlap * (relativeVelocity.dot(tangentVector));

        Vector2D force = new Vector2D(fn * enx - ft * eny, fn * eny + ft * enx);
        return force;
    }

    public static double getKn() {
        return kn;
    }

    public static double getGama() {
        return gamma;
    }
}