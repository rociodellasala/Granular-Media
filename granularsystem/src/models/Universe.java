package models;

import java.util.HashSet;
import java.util.Set;

public class Universe {

    private Set<Particle> particles;
    private double lengthX;
    private double lengthY;
    private double holeSize;

    public Universe(double lengthY, double lengthX, double holeSize) {
        this.setLengthX(lengthX);
        this.setLengthY(lengthY);
        setParticles(new HashSet<>());
        this.setHoleSize(holeSize);
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public void setParticles(Set<Particle> particles) {
        this.particles = particles;
    }

    public void setNewParticles(Set<Particle> particles) {
        this.particles.clear();
        this.particles = particles;
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

    public double getHoleSize() {
        return holeSize;
    }

    public void setHoleSize(double holeSize) {
        this.holeSize = holeSize;
    }
}