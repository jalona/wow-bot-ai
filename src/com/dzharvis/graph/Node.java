package com.dzharvis.graph;

import com.dzharvis.utils.Position;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Node implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public static final ReentrantLock lock = new ReentrantLock();
    public int djScore = Integer.MAX_VALUE;
    private final Position p;
    private List<Connection> links = new LinkedList<>();
    private HashMap<Node, Connection> nodes = new HashMap<>();

    public Node(Position p) {
        this.p = p;
    }

    public void addConnection(Node n){
        Connection c = new Connection(n);
        links.add(c);
        nodes.put(n, c);
    }

    public boolean isLinkedTo(Node node){
        return nodes.containsKey(node);
    }

    public Connection getLinkTo(Node node){
        return nodes.get(node);
    }

    public Position getPosition() {
        return p;
    }

    public List<Connection> getLinks() {
        return links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (p != null ? !p.equals(node.p) : node.p != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return p != null ? p.hashCode() : 0;
    }
}
