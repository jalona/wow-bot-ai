package com.dzharvis.graph;

import java.io.Serializable;

public class Connection implements Serializable {
    private int weight = 100;
    private final Node node;
    private static final double MODIFIER = 0.95;

    public Connection(Node node) {
        this.node = node;
    }

    public int getWeight() {
        return weight;
    }

    public void relax() {
        weight = Math.round(weight*MODIFIER);   
    }

    public Node getNode() {
        return node;
    }

}
