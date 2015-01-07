package com.company.utils;

/**
 * Created by dzharvis on 03.01.2015.
 */
public class Utils {
    public static int getRed(int c){
        return (c & 0xff0000) >> 16;
    }

    public static int getGreen(int c){
        return (c & 0x00ff00) >> 8;
    }

    public static int getBlue(int c){
        return (c & 0x0000ff);
    }
}
