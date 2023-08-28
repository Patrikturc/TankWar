package com.example.thomas.tankwar;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TankWarView extends SurfaceView implements Runnable{

    //region Variables

    //region Drawing

    private Rect top;
    private Rect topLeft;
    private Rect topRight;
    private Rect left;
    private Rect right;
    private Rect downLeft;
    private Rect downRight;
    private Rect down;
    private Bitmap bitmapback;
    private Canvas canvas;
    private Paint paint;
    private int images[];
    private int diffbacks[];

    //endregion

    //region Game

    private Tank tank;
    private Tank enemy;
    private Bullet bullet;
    private Bullet eBullet;
    private Bullet triple1;
    private Bullet triple2;
    private PowerUp power;
    private int score = 0;
    private int lives = 5;
    private String time;

    //endregion

    //region System

    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private int fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    private int game0X;
    private int game0Y;
    private int gameX;
    private int gameY;

    //endregion

    //region Timer

    private int seconds = 0;
    private int minutes;
    private int spawn = 0;
    private Handler gameHandler = new Handler();
    private Runnable gameRunnable = new Runnable() {

        @Override
        public void run() {
            seconds ++;
            minutes += seconds / 60;
            seconds = seconds % 60;

            time = (String.format("%d:%02d", minutes, seconds));

            //will generate a powerup every ten seconds after one has despawned
            if (power.getActive() == false && ((minutes * 60) + seconds) - spawn == 10) {
                power.generatePower();
                spawnHandler.postDelayed(spawnRunnable, 7000);
            }

            gameHandler.postDelayed(this, 1000);

//            diffbacks = new int[]{
//                    R.drawable.background,
//
//            };







        }
//        public void setDefImages(int[] bitmaps){
//            diffbacks = bitmaps;
//            reset();
//        };
//
//
//
//        public void reset(){
//            prepareLevel(diffbacks);
//
//        }




    };

    //removes the powerup after seven seconds of activity
    private Handler spawnHandler = new Handler();
    private Runnable spawnRunnable = new Runnable() {

        @Override
        public void run() {
            power.initialisePowerup();
            setSpawn();
        }
    };

    //removes the effects of the power up after 10 seconds of usage
    private Handler powerHandler = new Handler();
    private Runnable powerRunnable = new Runnable() {

        @Override
        public void run(){
            power.deactivate();
            bullet.setTriple(power.getTriple());
            spawnHandler.postDelayed(spawnRunnable, 0);
        }
    };

    //endregion

    //endregion

    //region Constructor

    public TankWarView(Context context, int x, int y, int images[], int diffbacks[]) {
        super(context);
        this.context = context;
        this.images = images;
        this.diffbacks = diffbacks;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;
        diffbacks = new int[]{
                R.drawable.background
        };
        reset();



        prepareLevel(diffbacks);




    }



    public void reset(){
        prepareLevel(diffbacks);

    }

    //endregion

    //region Initial Setup

    private void prepareLevel(int[] bitmaps){
        setBorderButtons();

        tank = new Tank(context, screenX, screenY);
        bullet = new Bullet(context, screenX, screenY);

        enemy = new Tank(context, screenX, screenY);
        eBullet = new Bullet(context, screenX, screenY);

        power = new PowerUp(context, game0X, game0Y, gameX, gameY);
        power.setPlayer(tank);

        triple1 = new Bullet(context, screenX, screenY);
        triple2 = new Bullet(context, screenX, screenY);

        tank.setEnemy(enemy);
        enemy.setEnemy(tank);
        enemy.isEnemy();









        bitmapback = BitmapFactory.decodeResource(context.getResources(), bitmaps[0]);
        bitmapback = Bitmap.createScaledBitmap(bitmapback, (int) (screenX), (int) (screenY),false);

        gameHandler.postDelayed(gameRunnable, 0);
    }

    public void setImages(int images[]){
        tank.setDefImages(images);
    }



    //sets the positions of the white and black outer edge rectangles
    //used for marking where touch inputs are valid
    private void setBorderButtons() {
        top = new Rect();
        topLeft = new Rect();
        topRight = new Rect();
        left = new Rect();
        right = new Rect();
        downLeft = new Rect();
        downRight = new Rect();
        down = new Rect();

        top.top = 0;
        top.left = screenX/7;
        top.bottom = screenY/14;
        top.right = screenX - screenX/7;

        topLeft.top = 0;
        topLeft.left = 0;
        topLeft.bottom = screenY/9;
        topLeft.right = screenX/6;

        topRight.top = 0;
        topRight.left = screenX - screenX/6;
        topRight.bottom = screenY/9;
        topRight.right = screenX;

        left.top = screenY/14;
        left.left = 0;
        left.bottom = screenY - screenY/14;
        left.right = screenX/7;

        right.top = screenY/14;
        right.left = screenX - screenX/7;
        right.bottom = screenY - screenY/14;
        right.right  = screenX;

        downLeft.top = screenY - screenY/9;
        downLeft.left = 0;
        downLeft.bottom = screenY;
        downLeft.right = screenX/6;

        downRight.top = screenY-screenY/9;
        downRight.left = screenX-screenX/6;
        downRight.bottom = screenY;
        downRight.right = screenX;

        down.top = screenY - screenY/14;
        down.left = screenX/7;
        down.bottom = screenY;
        down.right = screenX - screenX/7;

        game0X = left.right;
        game0Y = top.bottom;

        gameX = right.left;
        gameY = down.top;

    }

    /// <summary>
    /// sets the spawn marker equal to the current time
    /// </summary>
    public void setSpawn(){
        spawn = (minutes * 60) + seconds;
    }

    //endregion

    //region System

    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            if(!paused)
                update();

            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1)
                fps = (int)(1000 / timeThisFrame);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update(){
        tank.update(fps);

        if(bullet.getStatus()) {
            bullet.update(fps);
            bulletCollisions(bullet);
        }

        if(triple1.getStatus()) {
            triple1.triple1Update(fps);
            bulletCollisions(triple1);
        }

        if(triple2.getStatus()) {
            triple2.triple2Update(fps);
            bulletCollisions(triple2);
        }

        checkCollisions(tank);
        //checkCollisions(enemy);
    }

    //endregion

    //region Drawing

    private void draw(){
        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            canvas.drawBitmap(bitmapback, game0X, game0Y, paint);
            canvas.drawBitmap(tank.getBitmap(), tank.getX(), tank.getY(), paint);
            canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), paint);

            if(bullet.getStatus())
                canvas.drawBitmap(bullet.getBitmap(), bullet.getX(), bullet.getY(), paint);

            if(triple1.getStatus())
                canvas.drawBitmap(triple1.getBitmap(), triple1.getX(), triple1.getY(), paint);

            if(triple2.getStatus())
                canvas.drawBitmap(triple2.getBitmap(), triple2.getX(), triple2.getY(), paint);

            if(power.getSpawn() == true)
                canvas.drawBitmap(power.getBitmap(), power.getX(), power.getY(), paint);

            drawBorderButtons();
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives + "    Time: " + time, 10,50, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void drawBorderButtons() {
        Paint brush = new Paint();

        brush.setColor(Color.argb(255, 0, 0, 0));
        canvas.drawRect(top, brush);
        canvas.drawRect(left, brush);
        canvas.drawRect(right, brush);
        canvas.drawRect(down, brush);

        brush.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawRect(topLeft, brush);
        canvas.drawRect(topRight, brush);
        canvas.drawRect(downLeft, brush);
        canvas.drawRect(downRight, brush);
    }

    //endregion

    //region Interaction

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                paused = false;
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();
                Rect touch = new Rect();

                touch.top = y;
                touch.left = x;
                touch.bottom = y+3;
                touch.right = x+3;

                if(Rect.intersects(touch, topLeft))
                    tank.setTrajectory(tank.UPL);
                else if(Rect.intersects(touch, topRight))
                    tank.setTrajectory(tank.UPR);
                else if(Rect.intersects(touch, downRight))
                    tank.setTrajectory(tank.DOWNR);
                else if(Rect.intersects(touch, downLeft))
                    tank.setTrajectory(tank.DOWNL);
                else if(Rect.intersects(touch, left))
                    tank.setTrajectory(tank.LEFT);
                else if(Rect.intersects(touch, right))
                    tank.setTrajectory(tank.RIGHT);
                else if(Rect.intersects(touch, top))
                    tank.setTrajectory(tank.UP);
                else if(Rect.intersects(touch, down))
                    tank.setTrajectory(tank.DOWN);
                else{
                    if(power.getTriple())
                    {
                        if(!bullet.getStatus() && !triple1.getStatus() && !triple2.getStatus())
                        {
                            triple1.tripleShot1(tank, tank.getTrajectory());
                            triple2.tripleShot2(tank, tank.getTrajectory());
                        }
                    }
                    bullet.shoot(tank, tank.getTrajectory());
                }
                break;
            case MotionEvent.ACTION_UP:
                tank.stopTank();
                break;
        }
        return true;
    }

    private void bulletCollisions(Bullet bullet) {
        if((bullet.getImpactPointY() < game0Y) || (bullet.getImpactPointY() > gameY) || (bullet.getImpactPointX() < game0X) || (bullet.getImpactPointX() > gameX))
            bullet.setInactive();

        if(Rect.intersects(tank.getEnemy().getRect(), bullet.getRect()))
            bullet.setInactive();
    }

    private void checkCollisions(Tank tank){

        int width = tank.getLength() / 2;
        int height = tank.getHeight() / 2;

        if (tank.getX() + width < game0X)
            tank.setX(gameX - width);
        else if (tank.getX() > gameX - width)
            tank.setX(game0X - width);
        else if (tank.getY() + height < game0Y)
            tank.setY(gameY - height);
        else if (tank.getY() > gameY - height)
            tank.setY(game0Y - height);

        if(Rect.intersects(tank.getRect(), tank.getEnemy().getRect())){
            int x1 = tank.getX();
            int y1 = tank.getY();
            int x2 = tank.getEnemy().getX();
            int y2 = tank.getEnemy().getY();

            switch(tank.getTrajectory()) {
                case 1:
                    tank.setX(x1 + 20);
                    tank.getEnemy().setX(x2 - 20);
                    break;
                case 2:
                    tank.setX(x1 - 20);
                    tank.getEnemy().setX(x2 + 20);
                    break;
                case 3:
                    tank.setY(y1 + 20);
                    tank.getEnemy().setY(y2 - 20);
                    break;
                case 4:
                    tank.setY(y1 - 20);
                    tank.getEnemy().setY(y2 +20);
                    break;
                case 5:
                    tank.setX(x1 + 20);
                    tank.setY(y1 + 20);
                    tank.getEnemy().setX(x2 - 20);
                    tank.getEnemy().setY(y2 - 20);
                    break;
                case 6:
                    tank.setX(x1 - 20);
                    tank.setY(y1 + 20);
                    tank.getEnemy().setX(x2 + 20);
                    tank.getEnemy().setY(y2 - 20);
                    break;
                case 7:
                    tank.setX(x1 - 20);
                    tank.setY(y1 - 20);
                    tank.getEnemy().setX(x2 + 20);
                    tank.getEnemy().setY(y2 + 20);
                    break;
                case 8:
                    tank.setX(x1 + 20);
                    tank.setY(y1 - 20);
                    tank.getEnemy().setX(x2 - 20);
                    tank.getEnemy().setY(y2 + 20);
                    break;
                default:
                    break;
            }
        }
        else if (Rect.intersects(tank.getRect(), power.getRect())) {
            power.activatePower();
            spawnHandler.removeCallbacks(spawnRunnable);
            powerHandler.postDelayed(powerRunnable, 10000);

            if(power.getTriple())
                bullet.setTriple(true);
        }

        tank.setRect();
        enemy.setRect();
    }

    //endregion

}  // end class
