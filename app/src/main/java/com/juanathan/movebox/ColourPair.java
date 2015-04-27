package com.juanathan.movebox;

/**
 * Created by Jonathan on 23/04/2015.
 */
public class ColourPair {
    private int cVal;
    private int cSpeed;
    private boolean goUp;
    private int ceiling;

    public ColourPair(int colourValue, boolean isGoingUp, int colourChangeSpeed, int colourValueCeiling){
        this.cVal = colourValue;
        this.goUp = isGoingUp;
        this.cSpeed = colourChangeSpeed;
        this.ceiling = colourValueCeiling;
    }

    public void update(){
        if (goUp){
            cVal+=cSpeed;
        } else {
            cVal -= cSpeed;
        }

        if (goUp && (cVal >= ceiling) ){
            cVal = ceiling;
            goUp = false;
        } else if (!goUp && (cVal <= 0)){
            cVal = 0;
            goUp = true;
        }
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp(boolean goUp) {
        this.goUp = goUp;
    }

    public int getcVal() {
        return cVal;
    }

    public void setcVal(int cVal) {
        this.cVal = cVal;
    }
}
