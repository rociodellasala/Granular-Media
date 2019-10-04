package models;

import java.awt.Color;

public class Particle {

    private static int ID = 0;
    private static int WALL_ID = -1;

    private int id;
    private double positionX;
    private double positionY;
    private double prevPositionX;
    private double prevPositionY;
    private double speedX;
    private double speedY;
    private double mass;
    private double radius;
    private double forceX;
    private double forceY;
    private double prevForceX;
    private double prevForceY;
    private Color color;

    public Particle(double positionX, double positionY, double speedX, double speedY, double mass, double radius, Color color) {
        this.setPositionX(positionX);
        this.setPositionY(positionY);
        this.setSpeedX(speedX);
        this.setSpeedY(speedY);
        this.setMass(mass);
        this.setRadius(radius);
        this.setColor(color);

        if(color.equals(Color.WHITE))
            this.setId(WALL_ID--);
        else
            this.setId(ID++);
    }

    public Particle(double positionX, double positionY, double prevPositionX, double prevPositionY, double speedX, double speedY, double mass, double radius, Color color) {
        this.setPositionX(positionX);
        this.setPositionY(positionY);
        this.setSpeedX(speedX);
        this.setSpeedY(speedY);
        this.setMass(mass);
        this.setRadius(radius);
        this.setColor(color);
        this.setPrevPositionX(prevPositionX);
        this.setPrevPositionY(prevPositionY);
        setForceX(0);
        setForceY(0);
        setPrevForceX(0);
        setPrevForceY(0);

        if(color.equals(Color.WHITE))
            this.setId(WALL_ID--);
        else
            this.setId(ID++);
    }

    public Particle(double positionX, double positionY, double radius) {
        this.setPositionX(positionX);
        this.setPositionY(positionY);
        this.setRadius(radius);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPrevPositionX() {
        return prevPositionX;
    }

    public void setPrevPositionX(double prevPositionX) {
        this.prevPositionX = prevPositionX;
    }

    public double getPrevPositionY() {
        return prevPositionY;
    }

    public void setPrevPositionY(double prevPositionY) {
        this.prevPositionY = prevPositionY;
    }

    public double getForceX() {
        return forceX;
    }

    public void setForceX(double forceX) {
        this.forceX = forceX;
    }

    public double getForceY() {
        return forceY;
    }

    public void setForceY(double forceY) {
        this.forceY = forceY;
    }

    public double getPrevForceX() {
        return prevForceX;
    }

    public void setPrevForceX(double prevForceX) {
        this.prevForceX = prevForceX;
    }

    public double getPrevForceY() {
        return prevForceY;
    }

    public void setPrevForceY(double prevForceY) {
        this.prevForceY = prevForceY;
    }
}