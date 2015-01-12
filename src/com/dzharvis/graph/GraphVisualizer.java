package com.dzharvis.graph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class GraphVisualizer {

    private final int width;
    private final int height;
    private ArrayList<Line2D.Double> lines;
    private ArrayList<Ellipse2D.Double> dots;

    public GraphVisualizer() {
        width = height = 2000;
        lines = new ArrayList<>();
        dots = new ArrayList<>();
    }

    public void save(){
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        paintComponent(cg);
        try {
            ImageIO.write(bImg, "png", new File("C:\\Users\\dzharvis\\Desktop\\maps\\"+ UUID.randomUUID().toString()+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.white);
        lines.forEach(g2d::draw);
        g.setColor(Color.GREEN);
        dots.forEach((d)->{
            g2d.draw(d);
            g2d.fill(d);
        });
    }

    public synchronized void addNode(Node n) {
        n.getLinks().forEach((l) -> {
            Line2D.Double line = new Line2D.Double(
                    n.getPosition().getX() * width,
                    -n.getPosition().getY() * height,
                    l.getNode().getPosition().getX() * width,
                    -l.getNode().getPosition().getY() * height
            );
            lines.add(line);
            Ellipse2D.Double dot = new Ellipse2D.Double(l.getNode().getPosition().getX() * width, -l.getNode().getPosition().getY() *  height, 2, 2);
            dots.add(dot);
        });
        Ellipse2D.Double dot = new Ellipse2D.Double(n.getPosition().getX() * width, -n.getPosition().getY() *  height, 2, 2);
        dots.add(dot);
    }

}