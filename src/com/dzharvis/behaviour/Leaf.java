package com.dzharvis.behaviour;

/**
 * Created by dzharvis on 03.01.2015.
 */
public class Leaf implements Node {

    private Action a;

    public Leaf(Action a) {
        this.a = a;
    }

    @Override
    public boolean action() {
        return a.action();
    }

}
