package com.dzharvis.utils;

import static java.lang.Math.sqrt;

/**
 * Created by dzharvis on 07.01.2015.
 */
public class Vector {
    private double dx;
    private double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getLength(){
        return sqrt(dx*dx + dy*dy);
    }

    public double scalMult(Vector v){
        return dx*v.getDx() + dy*v.getDy();
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}
