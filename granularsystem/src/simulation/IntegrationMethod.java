package simulation;

import models.Particle;
import utils.ForceCalculator;
import utils.NeighbourCalculator;

import java.util.Set;

public interface IntegrationMethod {
    Set<Particle> integrate(Set<Particle> particles, NeighbourCalculator neighbourCalculator, ForceCalculator fc);
}
