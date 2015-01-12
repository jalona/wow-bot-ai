package com.dzharvis.graph;

import java.io.Serializable;

public class Connection implements Serializable {
    int weight = 100;
    private final Node node;

    public Connection(Node node) {
        this.node = node;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getNode() {
        return node;
    }

}
