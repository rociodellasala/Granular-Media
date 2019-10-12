package models;

public class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0.0, 0.0);
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Vector2D add(final Vector2D vector) {
        return new Vector2D(x + vector.x, y + vector.y);
    }

    public Vector2D subtract(final Vector2D vector) {
        return new Vector2D(x - vector.x, y - vector.y);
    }

    public Vector2D multiplyByScalar(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    public double multiplyByVector(Vector2D other) {
        return x * other.x + y * other.y;
    }

    public Vector2D divideByScalar(double scalar) {
        return new Vector2D(x / scalar, y / scalar);
    }

    Vector2D distance(Vector2D v) {
        return new Vector2D(Math.abs(this.x - v.getX()), Math.abs(this.y - v.getY()));
    }

    double getModule() {
        return Math.sqrt(Math.pow(this.x, 2d) + Math.pow(this.y, 2d));
    }

    public double projectedOn(Vector2D other) {
        return new Vector2D(x, y).multiplyByVector(other.versor());
    }

    private Vector2D versor() {
        return new Vector2D(x, y).divideByScalar(abs());
    }

    public double abs() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector2D) {
            Vector2D v = (Vector2D) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector2d[" + x + ", " + y + "]";
    }

}