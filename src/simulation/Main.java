package simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Particle;
import models.Universe;
import utils.Configuration;

public class Main {


    public static void main(String[] args) {
        Configuration config = new Configuration();
        Simulation simulation;
        Universe universe;

        config.loadConfig();
        universe = new Universe();
        simulation = new Simulation(config);

        OvitoGenerator.initializeOvito();
        simulation.startUniverse();
        simulation.Simulate();
        OvitoGenerator.generateFiles();
        OvitoGenerator.closeFiles();
    }

}