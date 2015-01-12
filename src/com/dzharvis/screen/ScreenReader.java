package com.dzharvis.screen;

import autoitx4java.AutoItX;
import com.dzharvis.WoW;
import com.dzharvis.utils.Position;

import static com.dzharvis.utils.Utils.getRed;

public class ScreenReader {
    private final AutoItX autoItX;

    public ScreenReader(AutoItX autoItX) {
        this.autoItX = autoItX;
    }

    public Position getCurrentPosition() {
        int p = getPixelColor(5, 5);
        int m = getPixelColor(10, 5);
        return new Position(deserialize(p), -deserialize(m));
    }

    public double deserialize(int p){
        return (p)/16581375.0;
    }

    public boolean isOnBG(){
        int q = getPixelColor(WoW.BG_OFFSET_X, WoW.BG_OFFSET_Y);
        return q == WoW.BLUE;
    }

    public int getInstanceID(){
        return getPixelColor(20, 15);
    }

    public double getFacing() {
        int p = getPixelColor(15, 5);
        return deserialize(p) * 7.0 - (Math.PI * 1.5);
    }

    public int getRawColor(int x, int y){
        return (int) autoItX.pixelGetColor(x, y);
    }

    public int getPixelColor(int x, int y){
        return (int) autoItX.pixelGetColor(WoW.X + x, WoW.Y + y);
    }

    public int getNumOfPlayers() {
        int n = getPixelColor(10, 15);
        int num = (int) Math.round((getRed(n) / 255.0) * 100.0);
        return num;
    }

    public Position getPlayerPosition(int index){
        int x = getPixelColor(10+5, 20+index*5);
        int y = getPixelColor(10+10, 20+index*5);
        return new Position(deserialize(x), -deserialize(y));
    }
}
