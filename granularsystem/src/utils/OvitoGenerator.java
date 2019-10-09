package utils;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import models.Particle;
import simulation.Simulation;

public class OvitoGenerator {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    static FileWriter dataWriter;

    public static void initializeOvito() {
        File data = createFile("./data.txt");
        dataWriter = openWriter(data);
    }

    public static void closeFiles() {
        closeWriter(dataWriter);
    }

    public static void recopilateData(Simulation simulation) {
        List<double[]> currentPositions = new ArrayList<>();
        Set<Particle> walls = new HashSet<>();
        recopilatePositions(simulation.getUniverse().getParticles(), currentPositions);
        for(Particle p: simulation.getUniverse().getWalls())
            walls.add(p);
        recopilatePositions(walls, currentPositions);
        generateInput(currentPositions);
    }

    public static void recopilatePositions(Set<Particle> particles, List<double[]> currentPositions) {
        double id;
        double x;
        double y;
        double ra;
        double r;
        double g;
        double b;
        Color color;

        for (Particle p : particles) {
            id = p.getID();
            x = p.getPosition().getX();
            y = p.getPosition().getY();
            ra = p.getRadius();
            color = getColorBySpeed(p.getSpeed().getX(),p.getSpeed().getY());
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();

            if (p.getColor().equals(Color.white)) {
                r = Color.white.getRed();
                g = Color.white.getGreen();
                b = Color.white.getBlue();
            }

            double[] currentParticle = {id, x, y, ra, r, g, b};
            currentPositions.add(currentParticle);
        }
    }

    private static Color getColorBySpeed(double speedX, double speedY) {
        Color color;
        double speed;

        speed = Math.sqrt( Math.pow(speedX, 2) + Math.pow(speedY, 2));
        if(speed < 2.0)
            color = new Color(0,0,255);
        else if(speed < 4.0)
            color = new Color(51,51,255);
        else if(speed < 6.0)
            color = new Color(102,102,150);
        else if(speed < 8.0)
            color = new Color(153,153,100);
        else if(speed < 10.0)
            color = new Color(204,204,50);
        else if(speed < 12.0)
            color = new Color(255,255,255);
        else if(speed < 14.0)
            color = new Color(255,204,204);
        else if(speed < 16.0)
            color = new Color(255,153,153);
        else if(speed < 18.0)
            color = new Color(255,102,102);
        else if(speed < 20.0)
            color = new Color(255,51,51);
        else
            color = new Color(255,0,0);
        return color;
    }

    public static File createFile(String name) {
        File file = new File(name);
        try {
            file.createNewFile();
            System.out.println("File is created!");
        } catch (IOException e) {
            System.out.println("File could not be created");
        }
        return file;
    }

    public static FileWriter openWriter(File file) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return writer;
    }

    public static void closeWriter(FileWriter writer) {
        try {
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateInput(List<double[]> list) {
        int quantity = list.size();
        try {
            dataWriter.write(quantity + "\n");
            dataWriter.write("\\ID" + "\t" + "X" + "\t" + "Y" + "\t" + "Radius" + "\t" + "Red"+ "\t" + "Green" + "\t" + "Blue" + "\n");

            for (double[] d : list) {
                dataWriter.write((int) d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\t" + d[4] + "\t" + d[5] + "\t" + d[6] + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }



}