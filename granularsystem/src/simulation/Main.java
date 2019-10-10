package simulation;

import models.Universe;
import utils.Configuration;
import utils.NeighbourCalculator;
import utils.OvitoGenerator;

public class Main {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        Simulation simulation;
        Universe universe;

        config.loadConfig();
        checkParameters(config);
        System.out.println("Todos los parametros de configuración son correctos");

        System.out.println("Creando el universo");
        universe = new Universe(config.getL(), config.getW(), config.getHoleSize());

        simulation = new Simulation(universe, config.getTotalTime());

        NeighbourCalculator ncalculator = new NeighbourCalculator(config.getL(), config.getW(),
                config.getInteractionRadio());

        OvitoGenerator.initializeOvito();

        System.out.println("Iniciando la simulación del medio granular");
        simulation.startUniverse(config.getQuantityMethod(), config.getQuantity());
        simulation.simulate(config.getDeltaT(), config.getDeltaT2(), ncalculator);

        OvitoGenerator.closeFiles();
    }

    private static void checkParameters(Configuration config) {
        if (config.getQuantity() < 1) {
            throw new IllegalArgumentException("La cantidad de partículas debe ser como minimo 1");
        }

        if (config.getL() < 1.0 || config.getL() > 1.5) {
            throw new IllegalArgumentException("El alto L del recinto debe pertenecer al intervalo [1.0,1.5] m");
        }

        if (config.getW() < 0.3 || config.getW() > 0.4) {
            throw new IllegalArgumentException("El ancho W del recinto debe pertenecer al intervalo [0.3,0.4] m");
        }

        if (config.getHoleSize() < 0.15 || config.getHoleSize() > 0.25) {
            throw new IllegalArgumentException("El ancho de salida D del recinto debe pertenecer al intervalo " +
                    "[0.15,0.25] m");
        }

    }

}