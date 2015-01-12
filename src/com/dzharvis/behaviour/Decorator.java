package com.dzharvis.behaviour;

/**
 * Created by dzharvis on 03.01.2015.
 */
public abstract class Decorator implements Node {

    protected Node node;

    public Decorator(Node node) {
        this.node = node;
    }
}
