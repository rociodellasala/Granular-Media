package utils;

import models.Particle;
import simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OvitoGenerator {
    private static FileWriter dataWriter;
    private static FileWriter kineticWriter;

    public static void initializeOvito() {
        File data = createFile("./data.txt");
        File kineticEnergy = createFile("./kineticEnergy.txt");
        dataWriter = openWriter(data);
        kineticWriter = openWriter(kineticEnergy);
    }

    public static void closeFiles() {
        closeWriter(dataWriter);
        closeWriter(kineticWriter);
    }

    public static void recopilateData(Simulation simulation) {
        List<double[]> data = new ArrayList<>();
        Set<Particle> walls = new HashSet<>();
        recopilateParticlesData(simulation.getUniverse().getParticles(), data);
        for (Particle p : simulation.getUniverse().getWalls())
            walls.add(p);
        recopilateParticlesData(walls, data);
        generateInput(data);
    }


    public static void recopilateParticlesData(Set<Particle> particles, List<double[]> data) {
        double id;
        double x, y;
        double vx, vy;
        double ra;

        for (Particle p : particles) {
            id = p.getID();
            x = p.getPosition().getX();
            y = p.getPosition().getY();
            vx = p.getPosition().getX();
            vy = p.getPosition().getY();
            ra = p.getRadius();

            double[] currentParticle = {id, x, y, vx, vy, ra};
            data.add(currentParticle);
        }
    }

    private static File createFile(String name) {
        File file = new File(name);
        try {
            file.createNewFile();
            System.out.println("El archivo de output " + name + " ha sido creado");
        } catch (IOException e) {
            System.out.println("El archivo de output " + name + " no se ha podido crear");
        }
        return file;
    }

    private static FileWriter openWriter(File file) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer;
    }

    private static void closeWriter(FileWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateInput(List<double[]> list) {

        try {
            dataWriter.write(list.size() + "\n");
            dataWriter.write("\\ID" + "\t" + "X" + "\t" + "Y" + "\t" + "Vx" + "\t" + "Vy" + "\t" +
                    "Radius" + "\n");

            for (double[] d : list) {
                dataWriter.write((int) d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\t" + d[4] +
                        "\t" + d[5] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateKineticInput(List<double[]> data) {
        
    	
    	
    	for(double d[]: data) {
			try {
				kineticWriter.write(d[0] + ",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			kineticWriter.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(double d[]: data) {
			try {
				kineticWriter.write(d[1] + ",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }


}