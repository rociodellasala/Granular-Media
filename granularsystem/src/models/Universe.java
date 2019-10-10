package models;

import utils.Const;

import java.util.HashSet;
import java.util.Set;

public class Universe {

    private Set<Particle> particles;
    private Set<Particle> walls;

    public Universe() {
        setParticles(new HashSet<>());
        setWalls(new HashSet<>());
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

    private void setWalls(Set<Particle> walls) {
        this.walls = walls;
    }

    public Set<Particle> getWalls() {
        return walls;
    }
}