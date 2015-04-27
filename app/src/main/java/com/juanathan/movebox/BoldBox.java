package com.juanathan.movebox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jonathan on 23/04/2015.
 */
public class BoldBox extends GameObject {

    //enum BoxType {STRAIGHT_SHOOTER, SIDE_SWITCHER, CARDBOARD_CONCENTRATOR, CONFUSED_CRATE, PAUSING_PACKAGE}
    ArrayList<ColourPair> cPs;
    Paint paint = new Paint();

    public BoldBox(int x, int y, int width, int height){
        super.x= x;
        super.y = y;
        super.width = width;
        super.height = height;

        cPs = new ArrayList<ColourPair>(3);
        cPs.add(new ColourPair(0, true, 5, 255));
        cPs.add(new ColourPair(128, true, 5, 255));
        cPs.add(new ColourPair(255, false, 5, 255));

        paint.setStyle(Paint.Style.FILL);
    }

    public void update() {

        for (ColourPair cp: cPs){
            cp.update();
        }
        paint.setColor(Color.rgb(cPs.get(0).getcVal(), cPs.get(1).getcVal(), cPs.get(2).getcVal()));

        dy +=1;

        if (dy > 10) dy = 10;
        y+=dy;
    }

    public void draw(Canvas canvas){
        canvas.drawRect((x - width / 2), (y - height / 2), (x + width / 2), (y + height / 2), paint);
    }
}
