package com.dzharvis.screen.radar;

import com.dzharvis.graph.GraphVisualizer;
import com.dzharvis.graph.Node;
import com.dzharvis.screen.ScreenReader;
import com.dzharvis.utils.Position;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class BgMapBuilder extends Thread {
    private static final long RPS = 50;
    private static final long DELAY = 1000 / RPS;
    public static final String PATHNAME = "C:\\Users\\dzharvis\\Desktop\\ser\\map.ser";
    private final ScreenReader sr;

    private final HashMap<Integer, Node> lastPos = new HashMap<>();
    private HashMap<Integer, HashMap<Position, Node>> maps = new HashMap<>();

    public BgMapBuilder(ScreenReader sr) {
        this.sr = sr;
        restoreMap();
    }

    private void restoreMap() {
        File f = new File(PATHNAME);
        if (f.exists()) {
            try {
                FileInputStream is = new FileInputStream(f);
                HashMap<Integer, HashMap<Position, Node>> o = SerializationUtils.deserialize(is);
                maps = o;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!interrupted()) {
            long st = System.currentTimeMillis();
            readData();
            long end = System.currentTimeMillis();
            try {
                long timeout = DELAY - (end - st);
                timeout = timeout < 0 ? 0 : timeout;
                TimeUnit.MILLISECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                System.out.println("stopping");
                break;
            }
        }
        save();
    }

    protected void save() {
        maps.entrySet().forEach((e) -> {
            GraphVisualizer gv = new GraphVisualizer();
            e.getValue().values().forEach(gv::addNode);
            gv.save();
        });
        saveMap();
    }

    private void saveMap() {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(PATHNAME));
            SerializationUtils.serialize(maps, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private synchronized HashMap<Position, Node> getNodes(int id) {
        HashMap<Position, Node> map = maps.get(id);
        if (map == null)
            map = new HashMap<>();
        maps.put(id, map);
        return map;
    }

    private volatile Position randomPlayer;

    public Node getRandomPlayerPos() {
        HashMap<Position, Node> nodes = getNodes(sr.getInstanceID());
//        Collection<Node> values = nodes.values();
//        int size = values.size();
//        return (Node) values.toArray()[new Random().nextInt(size)];
//        int num = sr.getNumOfPlayers();
//        Random random = new Random();
//        int i = random.nextInt(num + 1);
//        Position srPlayerPosition = sr.getPlayerPosition(i);
        return nodes.get(randomPlayer);
//        return randomPlayer;
    }

    protected void readData() {
        if (!sr.isOnBG()) return;
        int num = sr.getNumOfPlayers();
        if (num < 1 || num > 40) return;
        HashMap<Position, Node> nodes = getNodes(sr.getInstanceID());
        for (int i = 1; i <= num; i++) {
            Node from = this.lastPos.get(i);
            Position srPlayerPosition = sr.getPlayerPosition(i);
            if (abs(srPlayerPosition.getX()) <= 0.005 || abs(srPlayerPosition.getY()) <= 0.05) {
                continue;
            }
            if (from == null) {
                from = nodes.get(srPlayerPosition);
                if (from == null) {
                    from = new Node(srPlayerPosition);
                }
                this.lastPos.put(i, from);
                nodes.put(srPlayerPosition, from);
                continue;
            }
            Node to = nodes.get(srPlayerPosition);
            if (to == null) {
                to = new Node(srPlayerPosition);
                nodes.put(srPlayerPosition, to);
            }
            if (!from.equals(to)) {
                if (!connectionExists(from, to)) {
                    Node.lock.lock();
                    try {
                        if (abs(from.getPosition().distanceTo(to.getPosition()).getLength()) < 0.025) {
                            from.addConnection(to);
                            if (Math.random() > .1) randomPlayer = srPlayerPosition;
                        }
                    } finally {
                        Node.lock.unlock();
                    }
                } else {
                    from.getLinkTo(to).relax();
                }
            }
            this.lastPos.put(i, to);
        }
    }

    private boolean connectionExists(Node from, Node to) {
        return from.isLinkedTo(to);
    }

    public Node getCurrentPosition() {
        HashMap<Position, Node> nodes = getNodes(sr.getInstanceID());
        return nodes.get(sr.getCurrentPosition());
    }
}
