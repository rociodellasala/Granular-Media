package utils;

public class Const {
    public final static double g = 9.8;                // m/s^2
    public final static double kn = Math.pow(10, 5);    // N/m
    public final static double kt = 2 * kn;            // N/m
    public final static double gamma = 70;            // N/m

    public final static double MAX_TRIES = Math.pow(10, 7);
    public final static double MEDIUM_TRIES = Math.pow(10, 5);

    public final static double minRadius = 0.01;
    public final static double maxRadius = 0.015;

    public final static double mass = 0.01;

    public static double L;
    public static double W;
    public static double D;

    public Const(double L, double W, double D) {
        Const.L = L;
        Const.W = W;
        Const.D = D;
    }
}
