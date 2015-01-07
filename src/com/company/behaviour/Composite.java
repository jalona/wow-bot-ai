package com.company.behaviour;

import java.util.List;

public abstract class Composite implements Node {
    protected List<Node> nodes;

    public Composite( List<Node> nodes) {
        this.nodes = nodes;
    }

}
