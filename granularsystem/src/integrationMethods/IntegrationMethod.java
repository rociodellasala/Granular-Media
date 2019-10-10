package integrationMethods;

import models.Particle;

import java.util.Set;

public interface IntegrationMethod {
    Set<Particle> integrate(Set<Particle> particle);
}
