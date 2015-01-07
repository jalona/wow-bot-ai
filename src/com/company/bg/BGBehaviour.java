package com.company.bg;

import autoitx4java.AutoItX;
import com.company.behaviour.Leaf;
import com.company.behaviour.Node;
import com.company.behaviour.Selector;
import com.company.behaviour.Sequence;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.util.Arrays.asList;

/**
 * Created by dzharvis on 03.01.2015.
 */
public class BGBehaviour extends Thread {
    private static final int X = 5;
    private static final int Y = 100;
    private static final int STATUS_OFFSET_X = 0;
    private static final int STATUS_OFFSET_Y = 0;
    private static final int BG_OFFSET_X = 5;
    private static final int BG_OFFSET_Y = 0;
    private static final int PLAYER_OFFSET_X = 10;
    private static final int PLAYER_OFFSET_Y = 0;

    private static final int WHITE = 0xffffff;
    private static final int BLACK = 0x000000;
    private static final int RED = 0xff0000;
    private static final int GREEN = 0x00ff00;
    private static final int BLUE = 0x0000ff;

    private static final int PVP_X = 1104;
    private static final int PVP_Y = 752;
    private static final int BGS_X = 61;
    private static final int BGS_Y = 557;
    private static final int ENTER_BG_X = 72;
    private static final int ENTER_BG_Y = 526;
    private static final int CONFIRM_BG_X = 615;
    private static final int CONFIRM_BG_Y = 206;
    private static final int RES_X = 689;
    private static final int RES_Y = 198;

    private static final String LEFT_MB = "left";
    private static final String SPACE_DOWN = "{SPACE down}";
    private static final String SPACE_UP = "{SPACE up}";
    private static final String ENTER_DOWN = "{ENTER down}";
    private static final String ENTER_UP = "{ENTER up}";

    private static final int DEFAULT_ASLEEP = 500;
    public static final String PASS = "";

    private static final Logger log = Logger.getLogger(BGBehaviour.class.getName());

    private final AutoItX x;
    private Node entry;
    private volatile boolean paused = false;

    public BGBehaviour(AutoItX x) {
        this.x = x;
        constructTree();
    }

    @Override
    public void run() {
        while (!currentThread().isInterrupted()) {
            if (!paused)
                entry.action();
            asleep(16);
        }
    }

    public void pauseAI() {
        paused = true;
    }

    public void resumeAI() {
        paused = false;
    }

    private void constructTree() {
        //selector login
        Node inGameQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + STATUS_OFFSET_X, Y + STATUS_OFFSET_Y);
            return q == WHITE;
        });
        Node isStartScreenQ = new Leaf(() -> {
            int b = (int) x.pixelGetColor(28, 689);
            int w = (int) x.pixelGetColor(31, 711);
            return (w == 16776191 || w == 4210496) && b == BLACK;
        });
        Node clearShit = new Leaf(()->{
            asleep(5000);
            x.mouseClick(LEFT_MB, 800, 610, 1, 10); //realm select
            asleep();
            x.mouseClick(LEFT_MB, 741, 445, 1, 10);
            asleep();
            x.mouseClick(LEFT_MB, 738, 397, 1, 10);
            asleep();
            x.mouseClick(LEFT_MB, 868, 609, 1, 10);
            asleep();
            x.mouseClick(LEFT_MB, 608, 473, 1, 10);
            asleep();
            return true;
        });
        Node enterPass = new Leaf(() -> {
            log.info("entering pass");
            if("".equals(PASS)){
                throw new RuntimeException("empty pass");
            }
            x.send(PASS, true);
            asleep();
            x.send(ENTER_DOWN, false);
            x.send(ENTER_UP, false);
            asleep(5000);
            return true;
        });
        Node isPlayerSelectScreen = new Leaf(()->{
            int l = (int) x.pixelGetColor(157, 724);
            int m = (int) x.pixelGetColor(604, 703);
            int r = (int) x.pixelGetColor(1171, 618);
            return l == 6947330 && m == 6422785 && r == 7274752;
        });
        Node passA = new Sequence(asList(isStartScreenQ, clearShit, enterPass));
        Node enterWorld = new Leaf(()->{
            log.info("entering world");
            x.send(ENTER_DOWN, false);
            x.send(ENTER_UP, false);
            return true;
        });
        Node enterA = new Sequence(asList(isPlayerSelectScreen, enterWorld));
        Node loginQ = new Selector(asList(passA, enterA));
        Node login = new Selector(asList(inGameQ, loginQ));
        //selector bg
        Node notInBGQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + BG_OFFSET_X, Y + BG_OFFSET_Y);
            return q == BLACK;
        });
        Node notInBGA = new Leaf(() -> {
//            x.mouseClick(LEFT_MB, PVP_X, PVP_Y, 1, 10);
            x.send("{H down}", false);
            x.send("{H up}", false);
            asleep();
            x.mouseClick(LEFT_MB, BGS_X, BGS_Y, 1, 10);
            asleep();
            x.mouseClick(LEFT_MB, 101, 222, 1, 10);
            asleep();
            x.mouseClick(LEFT_MB, ENTER_BG_X, ENTER_BG_Y, 1, 10);
            asleep();
            return true;
        });
        Node reg = new Sequence(asList(notInBGQ, notInBGA));

        Node queuedQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + BG_OFFSET_X, Y + BG_OFFSET_Y);
            return q == RED;
        });
        Node queuedA = new Leaf(() -> true);
        Node wait = new Sequence(asList(queuedQ, queuedA));

        Node confirmQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + BG_OFFSET_X, Y + BG_OFFSET_Y);
            return q == GREEN;
        });
        Node confirmA = new Leaf(() -> {
            log.info("entering bg");
            x.mouseClick(LEFT_MB, CONFIRM_BG_X, CONFIRM_BG_Y, 1, 10);
            asleep();
            return true;
        });
        Node confirm = new Sequence(asList(confirmQ, confirmA));

        Node inBGQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + BG_OFFSET_X, Y + BG_OFFSET_Y);
            return q == BLUE;
        });
        Node inBGA = new Leaf(() -> {
            if (random() < .01f) {
                x.send(SPACE_DOWN, false);
                x.send(SPACE_UP, false);
            }
            return true;
        });
        Node bg = new Sequence(asList(inBGQ, inBGA));
        //player
        Node isAliveQ = new Leaf(() -> {
            int q = (int) x.pixelGetColor(X + PLAYER_OFFSET_X, Y + PLAYER_OFFSET_Y);
            return q != BLACK;
        });
        Node isDeadA = new Leaf(() -> {
            x.mouseClick(LEFT_MB, RES_X, RES_Y, 1, 10);
            return true;
        });
        Node res = new Selector(asList(isAliveQ, isDeadA));
        //player
        Node bgRoot = new Selector(asList(reg, wait, confirm, bg));

        entry = new Sequence(asList(login, inGameQ, res, bgRoot));
    }

    private void asleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void asleep() {
        asleep(DEFAULT_ASLEEP);
    }
}
