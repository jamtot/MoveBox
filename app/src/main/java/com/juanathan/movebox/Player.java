package com.juanathan.movebox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jonathan on 22/04/2015.
 */
public class Player extends GameObject {

    private boolean playing;
    private int score;
    private boolean onLeft;
    Paint paint = new Paint();
    Paint paint1 = new Paint();
    private boolean rToG;
    private int rVal;
    private int gVal;
    private int colourSpeed;
    private boolean fancy;
    private boolean switchy;
    private int moveSpeed;

    public Player(int x, int y, int w, int h, int moveSpeed){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.moveSpeed = moveSpeed;

        score = 0;

        onLeft = true;
        rVal = 255;
        gVal = 0;
        rToG = true;
        colourSpeed = 5;

        fancy = true;
        playing = false;

        paint.setStyle(Paint.Style.FILL);
        paint1.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
    }

    public void update(float touchX, float touchY) {
        if (switchy) {
            if (onLeft) {
                if ((x - dx) > 120) {
                    dx -= 1;
                } else {
                    dx = 0;
                    x = 120;
                }
            } else {
                if ((x + dx) < 360) {
                    dx += 1;
                } else {
                    dx = 0;
                    x = 360;
                }
            }


            if (dx > 8) dx = 8;
            if (dx < -8) dx = -8;

            x += dx * 2;
        } else {

            if (x != (int)touchX && y != (int)touchY) {
                float normX = touchX - (float) x;
                float normY = touchY - (float) y;

                //double length = Math.sqrt(normX * normX + normY * normY);
                //x += (normX / length) * moveSpeed;
               //y += (normY / length) * moveSpeed;

                double length = Math.sqrt(normX * normX);
                x += (normX / length) * moveSpeed;

                /*if (Math.sqrt((normX * normX) + (normY * normY)) < moveSpeed) {
                    x = (int) touchX;
                    y = (int) touchY;
                }*/

                if (Math.sqrt(normX * normX) < moveSpeed) {
                    x = (int) touchX;
                    //y = (int) touchY;
                }
            }
            //x = (int)touchX;

        }

        if (fancy) {
            if (rToG) {
                rVal -= colourSpeed;
                gVal += colourSpeed;
                if (rVal <= 0) {
                    rVal = 0;
                    gVal = 255;
                    rToG = false;
                }
            } else {
                rVal += colourSpeed;
                gVal -= colourSpeed;
                if (rVal >= 255) {
                    rVal = 255;
                    gVal = 0;
                    rToG = true;
                }
            }
            paint.setColor(Color.rgb(rVal, gVal, 255));
            paint1.setColor(Color.rgb(gVal, rVal, 255));
        }
    }

    public void draw(Canvas canvas){
        canvas.drawRect((x - width / 2), (y - height / 2), (x + width / 2), (y + height / 2), paint);
        if (fancy)
        canvas.drawRect((x - width / 4), (y - height / 4), (x + width / 4), (y + height / 4), paint1);
    }

    public void switchSide(){
            if(onLeft){
                onLeft = false;
            } else {
                onLeft = true;
            }
    }

    public void goTo(int posX, int posY){
        float normX = (float)posX - (float)x;
        float normY = (float)posY - (float)y;

        double length = Math.sqrt( ((normX * normX) + (normY * normY)) );
        x+=normX/length;
        y+=normY/length;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void incScore() {
        this.score++;
    }

    public void setSwitchy(boolean switchy) {
        this.switchy = switchy;
    }
}
