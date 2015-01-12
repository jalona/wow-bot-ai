package com.dzharvis.utils;

import com.dzharvis.graph.Node;

import java.util.*;

import static java.lang.Math.atan2;

public class Utils {
    public static int getRed(int c) {
        return (c & 0xff0000) >> 16;
    }

    public static int getGreen(int c) {
        return (c & 0x00ff00) >> 8;
    }

    public static int getBlue(int c) {
        return (c & 0x0000ff);
    }

    public static double angle(Vector a, Vector b) {
        return atan2(a.getDx() * b.getDy() - a.getDy() * b.getDx(), a.getDx() * b.getDx() + a.getDy() * b.getDy());
    }

    public static List<Position> findShortest(Node from, Node to) {
        HashMap<Node, Node> predecessor = new HashMap<>();
        HashSet<Node> visited = dfs(from, to, predecessor);
        List<Position> path = new LinkedList<>();
        Node step = to;
        if (predecessor.get(step) == null) return null;
        path.add(step.getPosition());
        while (predecessor.get(step) != null) {
            step = predecessor.get(step);
            path.add(step.getPosition());
        }
        System.out.println(step.equals(from));
        Collections.reverse(path);
        visited.forEach((n)->n.djScore=Integer.MAX_VALUE);
        return path;
    }

    private static HashSet<Node> dfs(Node from, Node to, HashMap<Node, Node> predecessor) {
        TreeSet<Node> enqueue = new TreeSet<>((o1, o2) -> {
            int v = Integer.valueOf(o1.djScore).compareTo(o1.djScore);
            if(v==0) return -1;
            return v;
        });
        HashSet<Node> visited = new HashSet<>();
        from.djScore = 0;
        enqueue.add(from);
        while (!enqueue.isEmpty()) {
            Node node = enqueue.pollFirst();
            if (visited.contains(node)) continue;
            visited.add(node);
            Node.lock.lock();
            try {
                node.getLinks().forEach((c) -> {
                    Node raw = c.getNode();
                    enqueue.remove(raw);
                    int score = node.djScore + c.getWeight();
                    if (score < raw.djScore) {
                        raw.djScore = score;
                        predecessor.put(raw, node);
                    }
                    enqueue.add(raw);
                });
            } finally {
                Node.lock.unlock();
            }
        }
        return visited;
    }

}
