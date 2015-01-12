package com.dzharvis.behaviour;

import java.util.List;

/**
 * Created by dzharvis on 03.01.2015.
 */
public class Selector extends Composite {

    public Selector(List<Node> nodes) {
        super(nodes);
    }

    @Override
    public boolean action() {
        for (Node n : nodes){
            if(n.action())
                return true;
        }
        return false;
    }
}
