package com.dzharvis.screen.data;

import com.dzharvis.utils.Position;
import com.dzharvis.utils.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by dzharvis on 08.01.2015.
 */
public class PlayerInfo {
    private double facing;
    private Position position;
    private Vector facingVec;

    public PlayerInfo(double facing, Position position) {
        this.facing = facing;
        this.position = new Position(position.getX(), position.getY());
    }

    public double getFacing() {
        return facing;
    }

    public Vector getFacingVec() {
        if(facingVec == null){
            facingVec = new Vector(cos(facing), sin(facing));
        }
        return facingVec;
    }

    public Position getCurrentPosition(){
        return position;
    }

}
