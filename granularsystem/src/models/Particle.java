package models;

import java.awt.Color;
import java.text.DecimalFormat;

public class Particle {

    private static int ID = 0;
    private int id;

    private Vector2D previousPosition = null;
    private Vector2D position;
    private Vector2D previousSpeed;
    private Vector2D speed;

    private Vector2D force;

    private double mass;
    private double radius;
    private Color color;

    public Particle(Vector2D position, double mass, double radius, Color color) {
        this.setPosition(position);
        this.setSpeed(Vector2D.ZERO);
        this.setForce(Vector2D.ZERO);
        this.setRadius(radius);
        this.setMass(mass);
        this.setColor(color);
        this.setId(ID++);
    }

    public Particle(Vector2D position, double mass, double radius, Color color, int id) {
        this.setPosition(position);
        this.setSpeed(Vector2D.ZERO);
        this.setForce(Vector2D.ZERO);
        this.setRadius(radius);
        this.setMass(mass);
        this.setColor(color);
        this.setId(id);
    }

    public void setPreviousPosition(Vector2D position) {
        this.previousPosition = position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setPreviousSpeed(Vector2D speed) {
        this.previousSpeed = speed;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public void setForce(Vector2D force) {
        this.force = force;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public Vector2D getSpeed() {
        return speed;
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

    public Color getColor() {
        return color;
    }

    public double getMass() {
        return mass;
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
        DecimalFormat df = new DecimalFormat("###.0000000000");
        return df.format(position.getX()) + " "
                + df.format(position.getY()) + " "
                + df.format(speed.getX()) + " "
                + df.format(speed.getY()) + " "
                + radius + " "
                + mass + " ";
    }

    public double getDistance(Particle second) {
        double firstX = this.getPosition().getX();
        double secondX = second.getPosition().getX();
        double firstY = this.getPosition().getY();
        double secondY = second.getPosition().getY();
        double distance;

        distance = Math.sqrt(Math.pow(firstX - secondX,2) + Math.pow(firstY - secondY,2));
        return distance;
    }
}