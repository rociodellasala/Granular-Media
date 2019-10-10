package utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {

    private Map<String,String> program_configuration;
    private Properties properties;

    public  Configuration() {
        this.program_configuration = new HashMap<>();
        this.properties = new Properties();
    }

    public void loadConfig() {
        InputStream input = getClass().getResourceAsStream("/configuration_file.txt");

        try {
            properties.load(input);
        } catch (Exception E) {

        }

        for (final String name : properties.stringPropertyNames()) {
            program_configuration.put(name, properties.getProperty(name).toLowerCase());
        }
    }

    /* Getters for universe modifiers */

    public int getQuantity() {
        return Integer.parseInt(program_configuration.get("quantity"));
    }

    public double getHoleSize() {
        return Double.parseDouble(program_configuration.get("D"));
    }

    public double getL() {
        return Double.parseDouble(program_configuration.get("L"));
    }

    public double getW() {
        return Double.parseDouble(program_configuration.get("W"));
    }

    public String getQuantityMethod() {
        return program_configuration.get("quantityMethod");
    }

    public double getInteractionRadio() {
        return Double.parseDouble(program_configuration.get("interactionRadio"));
    }

    /* Getters for simulation modifiers */

    public double getDeltaT() {
        return Double.parseDouble(program_configuration.get("deltaT"));
    }

    public double getDeltaT2() {
        return Double.parseDouble(program_configuration.get("deltaT2"));
    }

    public double getTotalTime() {
        return Double.parseDouble(program_configuration.get("totalTime"));
    }

}