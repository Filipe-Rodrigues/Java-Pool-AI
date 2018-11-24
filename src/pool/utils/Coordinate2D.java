/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.utils;

/**
 *
 * @author filipe
 */
public class Coordinate2D {

    public double x;
    public double y;

    public Coordinate2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate2D(Coordinate2D another) {
        this.x = another.x;
        this.y = another.y;
    }

    public double getDistance(Coordinate2D another) {
        return getDistance(this, another);
    }

    public static double getDistance(Coordinate2D coord1, Coordinate2D coord2) {
        if (coord1 != null && coord2 != null) {
            double sumSquared = (double) (Math.pow((coord1.x - coord2.x), 2)
                    + Math.pow((coord1.y - coord2.y), 2));
            return (double) Math.sqrt(sumSquared);
        }
        return Float.NaN;
    }

    public double computeAngle(Coordinate2D target, Coordinate2D reference) {
        return computeAngle(this, target, reference);
    }

    public static double computeAngle(Coordinate2D source, Coordinate2D target, Coordinate2D reference) {
        Coordinate2D u = new Coordinate2D(target.x - source.x, target.y - source.y);
        Coordinate2D v = new Coordinate2D(reference.x - source.x, reference.y - source.y);
        double cosinusAlphaNumerator = ((u.x * v.x) + (u.y * v.y));
        double cosinusAlphaDenominator = Math.sqrt((((u.x * u.x) + (u.y * u.y))) * (((v.x * v.x) + (v.y * v.y))));

        double cosinusAlpha = cosinusAlphaNumerator / cosinusAlphaDenominator;

        double alpha = Math.acos(cosinusAlpha);

        /*if (((u.x * v.y) - (v.x * u.y)) < 0) {
            alpha = (2 * Math.PI) - alpha;
        }*/

        return alpha;
    }

    public static double getRatio(double min, double max, double value) {
        return (value - min) / (max - min);
    }

    public static double getRelativePosition(double min, double max, double ratio) {
        return (max - min) * ratio + min;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Coordinate2D) {
            Coordinate2D otherCoordinate = (Coordinate2D) other;
            return x == otherCoordinate.x && y == otherCoordinate.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
}
