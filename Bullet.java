package com.example.thomas.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {

    //region Variables

    //region Drawing

    private int x;
    private int y;
    private int width;
    private int height;
    private Rect rect;
    private Bitmap currentBitmap;

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
    int heading = 0;                 //where the shell is firing
    int speed = 650;               //shell speed

    //endregion

    private int screenX;
    private int screenY;
    private boolean isActive;
    private boolean triple;

    //endregion

    //region Constructor

    public Bullet(Context context, int screenX, int screenY) {
        isActive = false;
        this.screenX= screenX;
        this.screenY= screenY;

        width = screenX/100;
        height = screenX/100;
        this.rect = new Rect();

        currentBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shell);
        currentBitmap = Bitmap.createScaledBitmap(currentBitmap, width, height, false);
    }

    //endregion

    //region Public Accessors

    public Bitmap getBitmap()
    {
        return currentBitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Rect getRect()
    {
        return rect;
    }

    public void setRect(int x, int y){
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + width;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive()
    {
        isActive = false;
    }

    public float getImpactPointY() {
        if(heading == DOWN || heading == DOWNR || heading == DOWNL)
            return y + height;
        else
            return y;
    }

    public float getImpactPointX() {
        if(heading == RIGHT || heading == UPR || heading == DOWNR)
            return x + width;
        else
            return x;
    }

    //endregion

    public boolean shoot(Tank tank, int direction) {
        if(!isActive) {

            heading = direction;
            isActive = true;

            switch(heading) {
                case LEFT:
                    x = tank.getX();
                    y = tank.getY() + (tank.getHeight()/2);
                    break;
                case RIGHT:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY() + (tank.getHeight()/2);
                    break;
                case UP:
                    x = tank.getX() + (tank.getLength()/2);
                    y = tank.getY();
                    break;
                case DOWN:
                    x = tank.getX() + (tank.getLength()/2);
                    y = tank.getY() + tank.getHeight();
                    break;
                case UPL:
                    x = tank.getX();
                    y = tank.getY();
                    break;
                case UPR:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY();
                    break;
                case DOWNR:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY() + tank.getHeight();
                    break;
                case DOWNL:
                    x = tank.getX();
                    y = tank.getY() + tank.getHeight();
                    break;
            }


            return true;
        }

        //Bullet already active
        return false;
    }

    public boolean tripleShot1(Tank tank, int direction) {
        if (!isActive) {
            heading = direction;
            isActive = true;

            switch (heading) {
                case LEFT:
                case UP:
                    x = tank.getX();
                    y = tank.getY();
                    break;
                case RIGHT:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY();
                    break;
                case DOWN:
                    x = tank.getX();
                    y = tank.getY() + tank.getHeight();
                    break;
                case UPL:
                    x = tank.getX() + (tank.getLength()/4);
                    y = tank.getY();
                    break;
                case UPR:
                    x = (tank.getX() + tank.getLength()) - (tank.getLength()/4);
                    y = tank.getY();
                    break;
                case DOWNR:
                    x = tank.getX() + tank.getLength();
                    y = (tank.getY() + tank.getHeight()) - (tank.getHeight()/4);
                    break;
                case DOWNL:
                    x = tank.getX();
                    y = (tank.getY() + tank.getHeight()) - (tank.getHeight()/4);
                    break;
            }
            return true;
        }
        return false;
    }

    public boolean tripleShot2(Tank tank, int direction) {
        if (!isActive) {
            heading = direction;
            isActive = true;

            switch (heading) {
                case LEFT:
                    x = tank.getX();
                    y = tank.getY() + tank.getHeight();
                    break;
                case RIGHT:
                case DOWN:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY() + tank.getHeight();
                    break;
                case UP:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY();
                    break;
                case UPL:
                    x = tank.getX();
                    y = tank.getY() + (tank.getHeight()/4);
                    break;
                case UPR:
                    x = tank.getX() + tank.getLength();
                    y = tank.getY() + (tank.getHeight()/4);
                    break;
                case DOWNR:
                    x = (tank.getX() + tank.getLength()) - (tank.getHeight()/4);
                    y = tank.getY() + tank.getHeight();
                    break;
                case DOWNL:
                    x = tank.getX() + (tank.getHeight()/4);
                    y = tank.getY() + tank.getHeight();
                    break;
            }
            return true;
        }
        return false;
    }

    public void triple1Update(int fps){

        switch(heading)
        {
            case LEFT:
            case UP:
                x -= speed / fps;
                y -= speed / fps;
                break;
            case RIGHT:
                x += speed / fps;
                y -= speed / fps;
                break;
            case DOWN:
                x -= speed /fps;
                y += speed / fps;
                break;
            case UPL:
            case UPR:
                y -= speed / fps;
                break;
            case DOWNL:
                x += speed / fps;
                break;
            case DOWNR:
                x -= speed / fps;
                break;
            default:
                break;
        }
        setRect(x,y);
    }

    public void triple2Update(int fps){

        switch(heading)
        {
            case LEFT:
                x -= speed / fps;
                y += speed / fps;
                break;
            case RIGHT:
            case DOWN:
                x += speed / fps;
                y += speed / fps;
                break;
            case UP:
                x += speed / fps;
                y -= speed / fps;
                break;
            case UPL:
                x -= speed / fps;
                break;
            case UPR:
                x += speed / fps;
                break;
            case DOWNL:
            case DOWNR:
                y += speed / fps;
                break;
            default:
                break;
        }
        setRect(x,y);
    }

    public void update(int fps) {

        switch(heading)
        {
            case LEFT:
                x = x - speed / fps;
                break;
            case RIGHT:
                x = x + speed / fps;
                break;
            case UP:
                y = y - speed / fps;
                break;
            case DOWN:
                y = y + speed / fps;
                break;
            case UPL:
                x = x - speed / fps;
                y = y - speed / fps;
                break;
            case UPR:
                x = x + speed / fps;
                y = y - speed / fps;
                break;
            case DOWNL:
                x = x - speed / fps;
                y = y + speed / fps;
                break;
            case DOWNR:
                x = x + speed / fps;
                y = y + speed / fps;
                break;
            default:
                break;
        }

        setRect(x, y);
    }

    public void setTriple(boolean status){
        triple = status;
    }
}