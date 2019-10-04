package utils;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.Particle;
import simulation.Simulation;

public class OvitoGenerator {
    static FileWriter dataWriter;
    static FileWriter informationWriter;
    static FileWriter energyWriter;
    static FileWriter speedWriter;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    static List<Double> mechEnergyEnergy;
    static List<Double> mechEnergyTime;
    static List<String> informationLeft;
    static List<String> informationRight;
    static List<Double> informationTime;
    static List<String> speedValues;

    public static void initializeOvito() {

    }

    public static void closeFiles() {

    }

    public static void recopilateData(Simulation simulation) {

    }

    public static void recopilatePositions(List<Particle> particles, List<double[]> currentPositions) {
        double id;
        double x;
        double y;
        double ra;
        double r;
        double g;
        double b;
        Color color;

        for (Particle p : particles) {
            id = p.getId();
            x = p.getPositionX();
            y = p.getPositionY();
            ra = p.getRadius();
            color = getColorBySpeed(p.getSpeedX(),p.getSpeedY());
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();

            if (p.getColor().equals(Color.white)) {
                r = Color.white.getRed();
                g = Color.white.getGreen();
                b = Color.white.getBlue();
            }

            double currentParticle[] = {id,x,y,ra,r,g,b};
            currentPositions.add(currentParticle);
        }
    }

    private static Color getColorBySpeed(double speedX, double speedY) {
        Color color = null;
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

    public static void generateFiles() {

        //Information
        for(Double s : informationTime) {
            try {
                informationWriter.write(s + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            informationWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String s : informationLeft) {
            try {
                informationWriter.write(s + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            informationWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String s : informationRight) {
            try {
                informationWriter.write(s + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //Energy
        for(Double s : mechEnergyTime) {
            try {
                energyWriter.write(s + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            energyWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Double d : mechEnergyEnergy) {
            try {
                energyWriter.write(d + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Speed
        for(String s : speedValues) {
            try {
                speedWriter.write(s + ",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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