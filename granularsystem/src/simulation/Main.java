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
        double interactionRadio = 0.015;

        config.loadConfig();
        universe = new Universe(config.getL(), config.getW(), config.getHoleSize());
        simulation = new Simulation(universe);

        NeighbourCalculator ncalculator = new NeighbourCalculator(config.getL(), config.getW(),
                interactionRadio, config.getMaxRadius());

        OvitoGenerator.initializeOvito();

        simulation.startUniverse(config.getQuantity(), config.getMaxRadius(), config.getMinRadius(), config.getMass());
        simulation.simulate(config.getDeltaT(), config.getDeltaT2(), ncalculator);

        OvitoGenerator.closeFiles();
    }

}