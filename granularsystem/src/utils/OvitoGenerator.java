package utils;

import models.Particle;
import simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class OvitoGenerator {
    private static FileWriter ovitoWriter;
    private static FileWriter kineticWriter;
    private static FileWriter caudalWriter;
    private static FileWriter caudalTimesWriter;

    public static void initializeOvito() {
        File data = createFile("./ovito.txt");
        File kineticEnergy = createFile("./kineticEnergy.txt");
        File caudal = createFile("./caudal.txt");
        File auxiliar = createFile("./caudalTimes.txt");
        ovitoWriter = openWriter(data);
        kineticWriter = openWriter(kineticEnergy);
        caudalWriter = openWriter(caudal);
        caudalTimesWriter = openWriter(auxiliar);
    }

    public static void closeFiles() {
        closeWriter(ovitoWriter);
        closeWriter(kineticWriter);
        closeWriter(caudalWriter);
        closeWriter(caudalTimesWriter);
    }

    public static void recopilateData(Simulation simulation) {
        List<double[]> data = new ArrayList<>();
        Set<Particle> walls = new HashSet<>();
        recopilateParticlesData(simulation.getUniverse().getParticles(), data);
        for (Particle p : simulation.getUniverse().getWalls())
            walls.add(p);
        recopilateParticlesData(walls, data);
        generateOutput(data);
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
            vx = p.getSpeed().getX();
            vy = p.getSpeed().getY();
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

    private static void generateOutput(List<double[]> list) {

        try {
        	ovitoWriter.write(list.size() + "\n");
            ovitoWriter.write("\\ID" + "\t" + "X" + "\t" + "Y" + "\t" + "Vx" + "\t" + "Vy" + "\t" +
                    "Radius" + "\n");

            for (double[] d : list) {
                ovitoWriter.write((int) d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\t" + d[4] +
                        "\t" + d[5] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateKineticOutput(List<double[]> data) {
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
    
    public static void generateCaudalOutput(List<Double> caudalTimes) {
    	
    	for(double d: caudalTimes) {
			try {
				caudalTimesWriter.write(d + ",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	caudalTimes = applyQFormula(caudalTimes);
    	
    	for(double d: caudalTimes) {
			try {
				caudalWriter.write(d + ",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
    }
    
    private static List<Double> applyQFormula(List<Double> caudalTimes) {
    	List<Double> q = new LinkedList<>();
    	int N = 10;
    	int caudalTimesSize = caudalTimes.size();
    	
    	for(int i = 0; i < caudalTimes.size() && i + 9 < caudalTimesSize; i += 10) {
    		q.add(N/(caudalTimes.get(i + 9) - caudalTimes.get(i)));
    	}
    	
    	return q;
    }


}