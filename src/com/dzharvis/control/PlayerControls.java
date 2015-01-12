package com.dzharvis.control;

import autoitx4java.AutoItX;
import com.dzharvis.WoW;

public class PlayerControls {
    private final AutoItX autoItX;

    public PlayerControls(AutoItX autoItX) {
        this.autoItX = autoItX;
    }

    public void startRotating() {
        autoItX.mouseMove(autoItX.winGetClientSizeWidth(WoW.WOW) / 2, autoItX.winGetClientSizeHeight(WoW.WOW) / 2);
        autoItX.mouseDown(WoW.RIGHT_MB);
    }

    public void stopRotating() {
        autoItX.mouseUp(WoW.RIGHT_MB);
    }

    public void rotateCharacter(int d) {
        if (d == 0) return;
        autoItX.mouseMove(autoItX.mouseGetPosX() + d, autoItX.mouseGetPosY());
    }

    public void startMoving() {
        autoItX.send(WoW.W_DOWN, false);
    }

    public void stopMoving() {
        autoItX.send(WoW.W_UP, false);
    }

    public void send(String string, boolean isRaw){
        autoItX.send(string, isRaw);
    }

    public void rightClick(int x, int y){
        autoItX.mouseClick(WoW.RIGHT_MB, x, y, 1, 10);
    }

    public void leftClick(int x, int y){
        autoItX.mouseClick(WoW.LEFT_MB, x, y, 1, 10);
    }


}
