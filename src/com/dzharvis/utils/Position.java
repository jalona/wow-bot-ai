package com.dzharvis.utils;

import java.io.Serializable;

public class Position implements Serializable{
    private static final long serialVersionUID = 1529685098267757691L;
    private final double x;
    private final double y;
    private final int _x;
    private final int _y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
        _x = (int) (x*2000);
        _y = (int) (y*2000);
    }

    public Vector distanceTo(Position p){
        double dx = p.getX() - x;
        double dy = p.getY() - y;
        return new Vector(dx, dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (_x != position._x) return false;
        if (_y != position._y) return false;

        return true;
    }

    @Override
    public synchronized int hashCode() {
        int result = _x;
        result = 31 * result + _y;
        return result;
    }
}
