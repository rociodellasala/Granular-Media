package simulation;

import calculators.ForceCalculator;
import calculators.NeighbourCalculator;
import integrationMethods.IntegrationMethod;
import integrationMethods.VerletIntegrationMethod;
import models.Universe;
import utils.Configuration;
import utils.Const;
import utils.OutputGenerator;

public class Main {

    public static void main(String[] args) {
        Configuration config;
        Simulation simulation;
        Universe universe;

        config = new Configuration();
        config.loadConfig();
        checkParameters(config);
        System.out.println("Todos los parametros de configuración son correctos");

        new Const(config.getL(), config.getW(), config.getHoleSize());

        System.out.println("Creando el universo");
        universe = new Universe();

        NeighbourCalculator ncalculator = new NeighbourCalculator(config.getInteractionRadio());
        ForceCalculator forceCalculator = new ForceCalculator();
        IntegrationMethod ig = new VerletIntegrationMethod(config.getDeltaT(), ncalculator, forceCalculator);

        simulation = new Simulation(universe, config.getTotalTime(), config.getFillingPercentage(), ig);

        OutputGenerator.initializeOvito();

        System.out.println("Iniciando la simulación del medio granular");
        simulation.startUniverse(config.getQuantityMethod(), config.getQuantity());
        simulation.simulate(config.getDeltaT(), config.getDeltaT2());

        OutputGenerator.closeFiles();
    }

    private static void checkParameters(Configuration config) {
        if (!config.getQuantityMethod().toLowerCase().equals("max") && config.getQuantity() < 1) {
            throw new IllegalArgumentException("La cantidad de partÃ­culas debe ser como minimo 1");
        }

        if (config.getL() < 1.0 || config.getL() > 1.5) {
            throw new IllegalArgumentException("El alto L del recinto debe pertenecer al intervalo [1.0,1.5] m");
        }

        if (config.getW() < 0.3 || config.getW() > 0.4) {
            throw new IllegalArgumentException("El ancho W del recinto debe pertenecer al intervalo [0.3,0.4] m");
        }

        if (config.getHoleSize() < 0 || config.getHoleSize() > 0.25) {
            throw new IllegalArgumentException("El ancho de salida D del recinto debe pertenecer al intervalo " +
                    "[0.15,0.25] m");
        }

        if (config.getFillingPercentage() < 0.02 || config.getFillingPercentage() > 0.995) {
            throw new IllegalArgumentException("El porcentaje de llenado debe pertenecer al intervalo [0.2,0.85]");
        }

    }

}