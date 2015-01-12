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
    public static final int SPEED = 50;
    private static final double MAX_SPEED = 25;
    private final ScreenReader reader;
    private final PlayerControls control;

    public Parrot(ScreenReader reader, PlayerControls control) {
        this.reader = reader;
        this.control = control;
    }

    public void play(List<Position> positionList) {
        if(positionList == null) return;
//        while (true) {

            positionList.forEach(this::move);

//        }
    }

    private void move(Position p) {
        control.startRotating();
        control.startMoving();
        while (true) {
            PlayerInfo data = new PlayerInfo(reader.getFacing(), reader.getCurrentPosition());
            if (abs(data.getCurrentPosition().distanceTo(p).getLength()) < 0.002) break;
            rotate(p, data);
        }
        control.stopRotating();
        control.stopMoving();
    }

    private void rotate(Position nearestPosition, PlayerInfo data) {
        double angle = angle(nearestPosition, data);
        double sin = sin(angle);
        double v = cos(angle) < 0 ? (sin < 0 ? -SPEED : SPEED) : SPEED * sin;
        v = abs(v) > MAX_SPEED ? (v < 0 ? -MAX_SPEED : MAX_SPEED) : v;
        control.rotateCharacter((int) v);
    }


    private double angle(Position nearestPosition, PlayerInfo data) {
        Vector vectorTo = data.getCurrentPosition().distanceTo(nearestPosition);
        Vector facingVec = data.getFacingVec();
        return Utils.angle(vectorTo, facingVec);
    }

}
