package models;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class Universe {

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private List<Particle> particles;
    private List<Particle> walls;
    private double lengthX;
    private double lengthY;
    private double holeSize;

    public Universe(double lengthX, double lengthY, double holeSize) {
        this.setLengthX(lengthX);
        this.setLengthY(lengthY);
        particles = new LinkedList<Particle>();
        walls = new LinkedList<Particle>();
        this.setHoleSize(holeSize);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public List<Particle> getWalls() {
        return walls;
    }

    public void setWalls(List<Particle> walls) {
        this.walls = walls;
    }

    public double getLengthX() {
        return lengthX;
    }

    public void setLengthX(double lengthX) {
        this.lengthX = lengthX;
    }

    public double getLengthY() {
        return lengthY;
    }

    public void setLengthY(double lengthY) {
        this.lengthY = lengthY;
    }

    public void printUniverseInfo() {
        System.out.println("Printing universe State-------");
        for(Particle p: this.getParticles()) {
            System.out.println("Id: " + p.getId() + " PositionX: " + df2.format(p.getPositionX()) + " - PositionY: " +
                    df2.format(p.getPositionY()) + " SpeedX: " + df2.format(p.getSpeedX()) + " - SpeedY: " + df2.format(p.getSpeedY()));
        }
    }

    public double getHoleSize() {
        return holeSize;
    }

    public void setHoleSize(double holeSize) {
        this.holeSize = holeSize;
    }


}