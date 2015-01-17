package com.dzharvis.graph;

import java.io.Serializable;

public class Connection implements Serializable {
    private static final long serialVersionUID = -5088758845787797106l;
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
        weight = (int) Math.round(weight * MODIFIER);
    }

    public Node getNode() {
        return node;
    }

}
