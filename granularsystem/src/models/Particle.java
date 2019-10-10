package models;


public class Particle {

    private static int ID = 0;
    private static int WALL_ID = -1;
    private int id;

    private Vector2D previousPosition = null;
    private Vector2D position;
    private Vector2D futurePosition;

    private Vector2D previousSpeed;
    private Vector2D speed;
    private Vector2D futureSpeed;

    private Vector2D force;

    private double mass;
    private double radius;

    public Particle(Vector2D position, double mass, double radius, boolean isWall) {
        this.setPosition(position);
        this.setSpeed(Vector2D.ZERO);
        this.setForce(Vector2D.ZERO);
        this.setRadius(radius);
        this.setMass(mass);
        if(isWall)
            this.setId(WALL_ID--);
        else
            this.setId(ID++);
    }

    public Particle(Vector2D position, double mass, double radius, int id) {
        this.setPosition(position);
        this.setSpeed(Vector2D.ZERO);
        this.setForce(Vector2D.ZERO);
        this.setRadius(radius);
        this.setMass(mass);
        this.setId(id);
    }

    public void setPreviousPosition(Vector2D previousPosition) {
        this.previousPosition = previousPosition;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setNextPosition(Vector2D nextPosition) {
        this.futurePosition = nextPosition;
    }

    public void setPreviousSpeed(Vector2D previousSpeed) {
        this.previousSpeed = previousSpeed;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public void setNextSpeed(Vector2D nextSpeed) {
        this.futureSpeed = nextSpeed;
    }

    public void setForce(Vector2D force) {
        this.force = force;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Vector2D getPreviousPosition() {
        return previousPosition;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getNextPosition() {
        return futurePosition;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public Vector2D getNextSpeed() {
        return futureSpeed;
    }

    public Vector2D getForce() {
        return force;
    }

    public int getID() {
        return id;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public void reset() {
        this.futurePosition = null;
        this.futureSpeed = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
         return position.getX() + " "
                + position.getY() + " "
                + id;
    }

    public double getDistance(Particle p){
        return this.getPosition().distance(p.getPosition()).getModule();
    }
}