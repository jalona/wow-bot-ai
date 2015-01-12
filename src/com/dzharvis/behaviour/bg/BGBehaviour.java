package com.dzharvis.behaviour.bg;

import com.dzharvis.WoW;
import com.dzharvis.behaviour.Leaf;
import com.dzharvis.behaviour.Node;
import com.dzharvis.behaviour.Selector;
import com.dzharvis.behaviour.Sequence;
import com.dzharvis.control.PlayerControls;
import com.dzharvis.screen.ScreenReader;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.util.Arrays.asList;

public class BGBehaviour extends Thread {
    private static final Logger log = Logger.getLogger(BGBehaviour.class.getName());
    private static final long DEFAULT_ASLEEP = 100;

    private final ScreenReader reader;
    private final PlayerControls controls;

    private Node entry;
    private volatile boolean paused = false;

    public BGBehaviour(ScreenReader reader, PlayerControls controls) {
        this.reader = reader;
        this.controls = controls;
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
            int q = reader.getRawColor(WoW.X + WoW.STATUS_OFFSET_X, WoW.Y + WoW.STATUS_OFFSET_Y);
            return q == WoW.WHITE;
        });
        Node isStartScreenQ = new Leaf(() -> {
            int b = reader.getRawColor(28, 689);
            int w = reader.getRawColor(31, 711);
            return (w == 16776191 || w == 4210496) && b == WoW.BLACK;
        });
        Node clearShit = new Leaf(()->{
            asleep(5000);
            controls.leftClick(800, 610); //realm select
            asleep();
            controls.leftClick(741, 445);
            asleep();
            controls.leftClick(738, 397);
            asleep();
            controls.leftClick(868, 609);
            asleep();
            controls.leftClick(608, 473);
            asleep();
            return true;
        });
        Node enterPass = new Leaf(() -> {
            log.info("entering pass");
            if("".equals(WoW.PASS)){
                throw new RuntimeException("empty pass");
            }
            controls.send(WoW.PASS, true);
            asleep();
            controls.send(WoW.ENTER_DOWN, false);
            controls.send(WoW.ENTER_UP, false);
            asleep(5000);
            return true;
        });
        Node isPlayerSelectScreen = new Leaf(()->{
            int l = reader.getRawColor(157, 724);
            int m = reader.getRawColor(604, 703);
            int r = reader.getRawColor(1171, 618);
            return l == 6947330 && m == 6422785 && r == 7274752;
        });
        Node passA = new Sequence(asList(isStartScreenQ, clearShit, enterPass));
        Node enterWorld = new Leaf(()->{
            log.info("entering world");
            controls.send(WoW.ENTER_DOWN, false);
            controls.send(WoW.ENTER_UP, false);
            return true;
        });
        Node enterA = new Sequence(asList(isPlayerSelectScreen, enterWorld));
        Node loginQ = new Selector(asList(passA, enterA));
        Node login = new Selector(asList(inGameQ, loginQ));
        //selector bg
        Node notInBGQ = new Leaf(() -> {
            int q = reader.getPixelColor(WoW.BG_OFFSET_X, WoW.BG_OFFSET_Y);
            return q == WoW.BLACK;
        });
        Node notInBGA = new Leaf(() -> {
//            x.mouseClick(LEFT_MB, PVP_X, PVP_Y, 1, 10);
            controls.send("{H down}", false);
            controls.send("{H up}", false);
            asleep();
            controls.leftClick(WoW.BGS_X, WoW.BGS_Y);
            asleep();
            controls.leftClick(101, 222);
            asleep();
            controls.leftClick(WoW.ENTER_BG_X, WoW.ENTER_BG_Y);
            asleep();
            return true;
        });
        Node reg = new Sequence(asList(notInBGQ, notInBGA));

        Node queuedQ = new Leaf(() -> {
            int q = reader.getPixelColor(WoW.BG_OFFSET_X, WoW.BG_OFFSET_Y);
            return q == WoW.RED;
        });
        Node queuedA = new Leaf(() -> true);
        Node wait = new Sequence(asList(queuedQ, queuedA));

        Node confirmQ = new Leaf(() -> {
            int q = reader.getPixelColor(WoW.BG_OFFSET_X, WoW.BG_OFFSET_Y);
            return q == WoW.GREEN;
        });
        Node confirmA = new Leaf(() -> {
            log.info("entering bg");
            controls.leftClick(WoW.CONFIRM_BG_X, WoW.CONFIRM_BG_Y);
            asleep();
            return true;
        });
        Node confirm = new Sequence(asList(confirmQ, confirmA));

        Node inBGQ = new Leaf(() -> {
            int q = reader.getPixelColor(WoW.BG_OFFSET_X, WoW.BG_OFFSET_Y);
            return q == WoW.BLUE;
        });
        Node inBGA = new Leaf(() -> {
            if (random() < .01f) {
                controls.send(WoW.SPACE_DOWN, false);
                controls.send(WoW.SPACE_UP, false);
            }
            return true;
        });
        Node bg = new Sequence(asList(inBGQ, inBGA));
        //player
        Node isAliveQ = new Leaf(() -> {
            int q = reader.getPixelColor(WoW.PLAYER_OFFSET_X, WoW.PLAYER_OFFSET_Y);
            return q != WoW.BLACK;
        });
        Node isDeadA = new Leaf(() -> {
            controls.leftClick(WoW.RES_X, WoW.RES_Y);
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
        asleep( DEFAULT_ASLEEP);
    }
}
