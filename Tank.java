package com.example.thomas.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


public class Tank {

    //region Variables

    //region Drawing

    Rect rect;
    private int defImages[];
    private Bitmap bitmapUp;
    private Bitmap bitmapLeft;
    private Bitmap bitmapRight;
    private Bitmap bitmapDown;
    private Bitmap bitmapUpR;
    private Bitmap bitmapUpL;
    private Bitmap bitmapDownR;
    private Bitmap bitmapDownL;
    private Bitmap currentBitmap;
    private int length;
    private int height;
    private int x;
    private int y;

    //endregion

    //region Movement

    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int UP = 3;
    public final int DOWN = 4;
    public final int UPL = 5;
    public final int UPR = 6;
    public final int DOWNR = 7;
    public final int DOWNL = 8;
    private int trajectory;
    private boolean moving = false;
    private int tankSpeed;

    //endregion

    private Context context;
    private boolean isEnemy = false;
    private Tank enemy;

    //endregion

    //region Constructor

    public Tank(Context context, int screenX, int screenY){
        rect = new Rect();

        this.context = context;

        trajectory = 3;

        length = screenX/15;
        height = screenY/15;

        x = screenX/2;
        y = screenY/2;

        defImages = new int[]{
                R.drawable.tankleft,
                R.drawable.tankright,
                R.drawable.tankup,
                R.drawable.tankdown,
                R.drawable.tankupl,
                R.drawable.tankupr,
                R.drawable.tankdownr,
                R.drawable.tankdownl
        };

        reset();
        currentBitmap = bitmapUp;
        setRect();
    }

    //endregion

    //region Public Accessors

    //allows setting of a tank class to be the enemy
    public void isEnemy(){

        trajectory = 4;

        defImages = new int[]{
                R.drawable.enemy1,
                R.drawable.enemy2,
                R.drawable.enemy3,
                R.drawable.enemy4,
                R.drawable.enemy5,
                R.drawable.enemy6,
                R.drawable.enemy7,
                R.drawable.enemy8,
        };

        setBitmaps(defImages);
        currentBitmap = bitmapDown;

        isEnemy = true;

        y -= 200;
        setRect();
    }

    public Tank getEnemy(){
        return enemy;
    }

    public void setEnemy(Tank tank){
        enemy = tank;
    }

    public Rect getRect(){
        return rect;
    }

    public void setRect() {
        rect.top = y;
        rect.bottom = y + currentBitmap.getHeight();
        rect.left = x;
        rect.right = x + currentBitmap.getWidth();
    }

    public Bitmap getBitmap(){
        return currentBitmap;
    }

    public void setDefImages(int[] bitmaps){
        defImages = bitmaps;
        reset();
    }

    public void setBitmaps(int[] bitmaps){
        bitmapLeft = BitmapFactory.decodeResource(context.getResources(), bitmaps[0]);
        bitmapLeft = Bitmap.createScaledBitmap(bitmapLeft, height, length, false);

        bitmapRight = BitmapFactory.decodeResource(context.getResources(), bitmaps[1]);
        bitmapRight = Bitmap.createScaledBitmap(bitmapRight, height, length, false);

        bitmapUp = BitmapFactory.decodeResource(context.getResources(), bitmaps[2]);
        bitmapUp = Bitmap.createScaledBitmap(bitmapUp, length, height, false);

        bitmapDown = BitmapFactory.decodeResource(context.getResources(), bitmaps[3]);
        bitmapDown = Bitmap.createScaledBitmap(bitmapDown, length, height, false);

        bitmapUpL = BitmapFactory.decodeResource(context.getResources(), bitmaps[4]);
        bitmapUpL = Bitmap.createScaledBitmap(bitmapUpL, (int)(length*1.5), height, false);

        bitmapUpR = BitmapFactory.decodeResource(context.getResources(), bitmaps[5]);
        bitmapUpR = Bitmap.createScaledBitmap(bitmapUpR, (int)(length*1.5), height, false);

        bitmapDownR = BitmapFactory.decodeResource(context.getResources(), bitmaps[6]);
        bitmapDownR = Bitmap.createScaledBitmap(bitmapDownR, (int)(length*1.5), height, false);

        bitmapDownL = BitmapFactory.decodeResource(context.getResources(), bitmaps[7]);
        bitmapDownL = Bitmap.createScaledBitmap(bitmapDownL, (int)(length*1.5), height, false);
    }

//    public void setDefImages(int[] bitmaps){
//        defImages = bitmaps;
//        reset();
//    }

    public void reset(){
        setBitmaps(defImages);
        setSpeed(350);
    }

    public int getSpeed(){
        return tankSpeed;
    }

    public void setSpeed(int speed) {
        tankSpeed = speed;
    }

    public int getX(){
        return x;
    }

    public void setX(int newX) { x = newX; }

    public int getY(){
        return y;
    }

    public void setY(int newY) { y = newY; }

    public int getLength(){
        return currentBitmap.getWidth();
    }

    public int getHeight(){
        return currentBitmap.getHeight();
    }

    public int getTrajectory(){
        return trajectory;
    }

    public void setTrajectory(int state){
        trajectory = state;
        moving = true;
    }

    //endregion

    public void stopTank(){
        moving = false;
    }

    public void update(int fps){

        if(moving)
        {
            switch(trajectory)
            {
                case LEFT:
                    x = x - tankSpeed / fps;
                    currentBitmap = bitmapLeft;
                    break;
                case RIGHT:
                    x = x + tankSpeed / fps;
                    currentBitmap = bitmapRight;
                    break;
                case UP:
                    y = y - tankSpeed / fps;
                    currentBitmap = bitmapUp;
                    break;
                case DOWN:
                    y = y + tankSpeed / fps;
                    currentBitmap = bitmapDown;
                    break;
                case UPL:
                    x = x - tankSpeed / fps;
                    y = y - tankSpeed/ fps;
                    currentBitmap = bitmapUpL;
                    break;
                case UPR:
                    x = x + tankSpeed / fps;
                    y = y - tankSpeed/ fps;
                    currentBitmap = bitmapUpR;
                    break;
                case DOWNL:
                    x = x - tankSpeed / fps;
                    y = y + tankSpeed/fps;
                    currentBitmap = bitmapDownL;
                    break;
                case DOWNR:
                    x = x + tankSpeed / fps;
                    y = y + tankSpeed/ fps;
                    currentBitmap = bitmapDownR;
                    break;
                default:
                    break;
            }

            setRect();
        }
    }

}