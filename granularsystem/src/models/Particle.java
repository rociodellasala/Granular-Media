package models;

import java.awt.Color;
import java.text.DecimalFormat;

public class Particle {

    private static int ID = 0;
    private static int WALL_ID = 0;

    private int id;

    private Vector2D previusPosition;
    private Vector2D position;
    private Vector2D speed;
    private Vector2D force;
    private double normalForce;
    private double mass;
    private double radius;

    private Color color;

    public Particle(Vector2D position, Vector2D speed, Vector2D force, double mass, double radius, Color color) {
        this.setPosition(position);
        this.setSpeed(speed);
        this.setMass(mass);
        this.setForce(force);
        this.setRadius(radius);
        this.setNormalForce(0.0);
        this.setColor(color);

        if(color.equals(Color.WHITE))
            this.setId(WALL_ID--);
        else
            this.setId(ID++);
    }

    public Particle(Vector2D position, double mass, double radius, Color color) {
        this.setPosition(position);
        this.setSpeed(new Vector2D(0.0,0.0));
        this.setMass(mass);
        this.setForce(new Vector2D(0.0,0.0));
        this.setRadius(radius);
        this.setNormalForce(0.0);
        this.setColor(color);

        if(color.equals(Color.WHITE))
            this.setId(WALL_ID--);
        else
            this.setId(ID++);
    }

    public void setPreviusPosition(Vector2D position) {
        this.previusPosition = position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
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

    public void setNormalForce(double fn) {
        this.normalForce = fn;
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

    public Vector2D getPreviousPosition() {
        return previusPosition;
    }

    public double getPositionX(){
        return position.x;
    }

    public double getPositionY(){
        return position.y;
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getSpeedX(){
        return speed.x;
    }

    public double getSpeedY(){
        return speed.y;
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public double getForceX() {
        return force.x;
    }

    public double getForceY() {
        return force.y;
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

    public double getNormalForce() {
        return normalForce;
    }
}