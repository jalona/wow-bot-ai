package com.dzharvis.utils;

import com.dzharvis.graph.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testFindShortest() throws Exception {
        Node n1 = getNode(1, 2);

        Position p2 = new Position(3.0, 4.0);
        Node n2 = new Node(p2);

        Position p3 = new Position(5.0, 6.0);
        Node n3 = new Node(p3);

        n1.addConnection(n2);

        n2.addConnection(n3);

        List<Position> shortest = Utils.findShortest(n1, n3);
        Assert.assertTrue(shortest.size() == 3);
    }

    @Test
    public void testFindShortest2() throws Exception {
        Node n1 = getNode(1, 2);

        Position p2 = new Position(3.0, 4.0);
        Node n2 = new Node(p2);

        Position p3 = new Position(5.0, 6.0);
        Node n3 = new Node(p3);

        n1.addConnection(n2);
        n1.addConnection(n3);

        n2.addConnection(n3);

        n3.addConnection(n1);

        List<Position> shortest = Utils.findShortest(n1, n3);
        Assert.assertTrue(shortest.size() == 2);

    }

    @Test
    public void testFindShortest3() throws Exception {
        Node n1 = getNode(1, 2);
        Node n2 = getNode(3, 4);
        Node n3 = getNode(5, 6);
        Node n4 = getNode(7, 8);
        Node n5 = getNode(9, 10);
        Node n6 = getNode(11, 10);
        Node n7 = getNode(13, 10);
        Node n8 = getNode(14, 10);
        Node n9 = getNode(15, 10);
        Node n10 = getNode(16, 10);

        n1.addConnection(n2);
        n1.addConnection(n8);
        n1.addConnection(n1);

        n2.addConnection(n3);
        n2.addConnection(n4);

        n3.addConnection(n4);

        n4.addConnection(n5);

        n5.addConnection(n6);

        n6.addConnection(n7);

        n8.addConnection(n9);
        n8.addConnection(n10);

        n9.addConnection(n10);

        n10.addConnection(n7);

        List<Position> shortest = Utils.findShortest(n1, n5);
        Assert.assertTrue(shortest.size() == 4);
        Assert.assertTrue(verifyOrder(shortest, n1, n2, n4, n5));

        List<Position> shortest1 = Utils.findShortest(n1, n3);
        Assert.assertTrue(verifyOrder(shortest1, n1, n2, n3));
        List<Position> shortest2 = Utils.findShortest(n1, n9);
        Assert.assertTrue(verifyOrder(shortest2, n1, n8, n9));
        List<Position> shortest3 = Utils.findShortest(n1, n7);
        Assert.assertTrue(verifyOrder(shortest3, n1, n8, n10, n7));
    }

    private boolean verifyOrder(List<Position> shortest, Node... n) {
        for(int i=0; i<n.length; i++){
            Position p1 = shortest.get(i);
            if(!p1.equals(n[i].getPosition())) return false;
        }
        return true;
    }

    private Node getNode(int i, int i1) {
        Position p1 = new Position(i, i1);
        return new Node(p1);
    }
}