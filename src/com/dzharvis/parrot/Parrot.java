package com.dzharvis.parrot;

import com.dzharvis.control.PlayerControls;
import com.dzharvis.screen.ScreenReader;
import com.dzharvis.screen.data.PlayerInfo;
import com.dzharvis.utils.Position;
import com.dzharvis.utils.Utils;
import com.dzharvis.utils.Vector;

import java.util.List;

import static java.lang.Math.*;

public class Parrot {
    public static final int SPEED = 30;
    private static final double MAX_SPEED = 20;
    private final ScreenReader reader;
    private final PlayerControls control;

    public Parrot(ScreenReader reader, PlayerControls control) {
        this.reader = reader;
        this.control = control;
    }

    public void play(List<Position> positionList) {
            positionList.forEach(this::move);
    }

    private void move(Position p) {
        PlayerInfo data = new PlayerInfo(reader.getFacing(), reader.getCurrentPosition());
        control.startRotating();
        rotate(p, data, true);
        control.startMoving();
        while (true) {
            data = new PlayerInfo(reader.getFacing(), reader.getCurrentPosition());
            if (abs(data.getCurrentPosition().distanceTo(p).getLength()) < 0.0025) break;
            rotate(p, data, false);
        }
        control.stopRotating();
        control.stopMoving();
    }

    private void rotate(Position nearestPosition, PlayerInfo data, boolean loop) {
        double angle = angle(nearestPosition, data);
        do {
            double sin = sin(angle);
            double v = cos(angle) < 0 ? (sin < 0 ? -SPEED : SPEED) : SPEED * sin;
            v = abs(v) > MAX_SPEED ? (v < 0 ? -MAX_SPEED : MAX_SPEED) : v;
            control.rotateCharacter((int) v);
            data = new PlayerInfo(reader.getFacing(), reader.getCurrentPosition());
            angle = angle(nearestPosition, data);
        } while(loop && (abs(cos(angle)) < 0.95));
    }


    private double angle(Position nearestPosition, PlayerInfo data) {
        Vector vectorTo = data.getCurrentPosition().distanceTo(nearestPosition);
        Vector facingVec = data.getFacingVec();
        return Utils.angle(vectorTo, facingVec);
    }

}
