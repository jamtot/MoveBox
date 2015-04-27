package com.juanathan.movebox;

import android.content.Context;
//import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonathan on 22/04/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 856;
    public static final int MOVESPEED = 10;
    private MainThread thread;
    //private Background bg;
    private Player player;
    private ArrayList<BoldBox> boldBoxes;
    private Random rand = new Random();
    private long boxStartTime;
    private int best;

    private ParticleSystem pSys;
    private boolean makeParticles = false;
    private boolean drawPlayer = true;

    private boolean switchy = false;

    private float touchX, touchY;

    public GamePanel(Context context) {
        //surfaceviews constructor
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //abstantiate here
        //bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.blackstripes));
        player = new Player(240, 700, 100, 100, 10);
        boldBoxes = new ArrayList<>();
        boxStartTime = System.nanoTime();
        player.setSwitchy((switchy));

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchX = (touchX / getWidth()) * WIDTH;
            touchY = event.getY();
            touchY = (touchY / getHeight()) * HEIGHT;

            if (!player.isPlaying()) {
                player.setPlaying(true);
                player.resetScore();
                boldBoxes.clear();
                drawPlayer = true;
            } else {
               if (switchy){
                   player.switchSide();
               }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            touchX = event.getX();
            touchY = event.getY();
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.isPlaying()) {
            //bg.update();
            if (touchX < (player.getWidth()/2)){
                touchX = player.getWidth()/2;
            } else if (touchX > (WIDTH - (player.getWidth()/2))){
                touchX = (WIDTH -  (player.getWidth()/2));
            }
            if (touchY < (player.getHeight()/2)){
                touchY = player.getHeight()/2;
            } else if (touchY > (HEIGHT - (player.getHeight()/2))){
                touchY = (HEIGHT -  (player.getHeight()/2));
            }
            player.update(touchX, touchY);

            long boxElapsed = (System.nanoTime() - boxStartTime) / 1000000;
            if (boxElapsed > (2000 - player.getScore() * 3)) {
                if (boldBoxes.size() == 0) {

                    boldBoxes.add(new BoldBox(240, -200, 100, 100));
                } else {
                    boldBoxes.add(new BoldBox((int) (rand.nextDouble() * WIDTH), -200, 100, 100));
                }
                //reset timer
                boxStartTime = System.nanoTime();
            }

            //loop through the boxes, update, check collisions and remove ones offscreen
            for (int i = 0; i < boldBoxes.size(); i++) {
                //update the bold boxes
                boldBoxes.get(i).update();

                if (collision(boldBoxes.get(i), player)) {
                    boldBoxes.remove(i);
                    player.setPlaying((false));
                    break;
                }

                //if boxes go offscreen, remove them
                if (boldBoxes.get(i).getY() > 1000) {
                    boldBoxes.remove(i);
                    player.incScore();
                    break;
                }
            }
            if (makeParticles == false){
                makeParticles = true;
                pSys = null;
            }
        } else {
            if (makeParticles){
                makeParticles = false;
                pSys = new ParticleSystem((float)player.getX(), (float)player.getY(), 50, 1);
                drawPlayer = false;
            }
            pSys.update();
        }

        if (player.getScore()> best){
            best = player.getScore();
        }
    }


    @Override
    public void draw(Canvas canvas) {

        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if (canvas != null) {

            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);

            canvas.drawARGB(255,0,0,0);

            //bg.draw(canvas);

            if (drawPlayer){
                player.draw(canvas);
            } else if (pSys != null) {
                if (pSys.isAlive()) {
                    pSys.draw(canvas);
                }
            }

            for (int i = 0; i < boldBoxes.size(); i++){
                boldBoxes.get(i).draw(canvas);
            }

            drawText(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if(Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("SCORE: " + player.getScore(), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if (!player.isPlaying()){
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setColor((Color.WHITE));
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", 90, HEIGHT / 2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("TAP SCREEN TO SWITCH SIDES", 100, HEIGHT / 2 + 20, paint1);

            //pSys.draw(canvas);
        }

    }
}
