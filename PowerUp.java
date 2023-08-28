package com.example.thomas.tankwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class PowerUp {

    //region Markers

    private int current;        //the current power up active: 0 jet mode, 1 invincibility, 2 triple shot
    private boolean active;    //if the powerup is active or not
    private boolean spawn;            //the marker that handles the spawning of the powerup
    private boolean shooting;         //the marker that handles the triple shot activity

    //endregion

    //region Drawing

    private final int iconH = 78;
    private final int iconW = 78;
    private Bitmap jetIcon;         //the picturebox to hold the powerup icons
    private Bitmap invincibleIcon;
    private Bitmap tripleIcon;
    private Bitmap currentBitmap;
    private Rect rect;
    private int x;
    private int y;
    private int originX;
    private int originY;
    private int screenX;
    private int screenY;

    //endregion

    //region Triple Shot

    public boolean shoot1 = false;    //the state of the first extra shot, if it is active or not
    public boolean shoot2 = false;    //the state of the first extra shot, if it is active or not

    //endregion

    //region References

    private Tank player;            //the tank who uses the powerups     //the game world
    private Bullet shell;            //the shell linked to triple shot
    private int invincibleImages[];     //array to hold id's of invinicbility sprites
    private int jetImages[];            //array to hold id's of jet sprites

    //endregion

    //region Constructor

    public PowerUp(Context context, int x, int y, int endX, int endY) {
        active = false;
        shooting = false;

        originX = x;
        originY = y;
        screenX = endX;
        screenY = endY;

        rect = new Rect();
        jetIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.jetmode);
        jetIcon = Bitmap.createScaledBitmap(jetIcon, iconW, iconH, false);

        invincibleIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.invincible);
        invincibleIcon = Bitmap.createScaledBitmap(invincibleIcon, iconW, iconH, false);

        tripleIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tripleshot);
        tripleIcon = Bitmap.createScaledBitmap(tripleIcon, iconW, iconH, false);

        jetImages = new int[]{
                R.drawable.jet1,
                R.drawable.jet2,
                R.drawable.jet3,
                R.drawable.jet4,
                R.drawable.jet5,
                R.drawable.jet6,
                R.drawable.jet7,
                R.drawable.jet8
        };

        invincibleImages = new int[]{
                R.drawable.gold1,
                R.drawable.gold2,
                R.drawable.gold3,
                R.drawable.gold4,
                R.drawable.gold5,
                R.drawable.gold6,
                R.drawable.gold7,
                R.drawable.gold8,
        };

        //moving icons and shells offscreen
        initialisePowerup();
    }

    //endregion

    //region public get methods

    /// <summary>
    /// public getter for variable active
    /// </summary>
    /// <returns>the number denoting the active powerup</returns>
    public int GetCurrent()
    {
        return current;
    }

    /// <summary>
    /// public getter for variable active
    /// </summary>
    /// <returns>returns if powerup is active or not</returns>
    public boolean getActive() {
        return active;
    }

    public boolean getTriple() {
        return shooting;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean getSpawn() {
        return spawn;
    }

    public Bitmap getBitmap() {
        return currentBitmap;
    }

    public Rect getRect(){
        return rect;
    }

    //endregion

    //region public set methods

    /// <summary>
    /// sets the playable tank to the potential user of the powerup
    /// </summary>
    /// <param name="tank">the players tank object</param>
    public void setPlayer(Tank tank)
    {
        player = tank;
    }

    /// <summary>
    /// sets the designated shell to the shell linked to triple shot
    /// </summary>
    /// <param name="bullet">the player tanks shell</param>
    public void setShell(Bullet bullet)
    {
        shell = bullet;
    }

    //endregion

    //region generating powerups

    /// <summary>
    /// when the powerup is spawned it randomly selects from the created abilities
    /// </summary>
    public void generatePower() {
        boolean safeSpawn;
        Random rnd = new Random();

        //random number used to spawn whichever powerup corresponds
        current = rnd.nextInt(3);

        current = 2;

        switch (current)
        {
            case 0:
                setJet();
                break;
            case 1:
                setInvincible();
                break;
            case 2:
                setTriple();
                break;
            default:
                break;
        }

        //loop continually runs if powerup is spawned off screen or inside a tank until it is not
        do{
            x = rnd.nextInt(screenX);
            y = rnd.nextInt(screenY);
            rect.top = y;
            rect.bottom = y + iconH;
            rect.left = x;
            rect.right = x + iconW;
            safeSpawn = spawnCollisions(rect);
        }while(!safeSpawn);

        spawn = true;
    }

    /// <summary>
    /// sets the icon to jet mode
    /// </summary>
    private void setJet()
    {
        currentBitmap = jetIcon;
    }

    /// <summary>
    /// sets the icon to invincibility
    /// </summary>
    private void setInvincible() {
        currentBitmap = invincibleIcon;
    }

    /**
     * sets the icon to triple shot
     */
    private void setTriple() {
        currentBitmap = tripleIcon;
    }

    //endregion

    //region using powerups

    /**
     * method handling when a powerup is picked up by the player
     */
    public void activatePower() {

        active = true;
        initialisePowerup();

        if(current == 0) {
            player.setBitmaps(jetImages);
            player.setSpeed(player.getSpeed()*2);
        }
        else if (current == 1)
            player.setBitmaps(invincibleImages);
        else if (current == 2){
            shooting = true;
        }
    }

    public void deactivate(){
        active = false;

        if(current == 0 || current == 1)
            player.reset();
        else
            shooting = false;
    }

    /// <summary>
    /// de spawns the powerup, hides it off screen
    /// </summary>
    public void initialisePowerup() {
        rect.top = -100;
        rect.bottom = rect.top + iconH;
        rect.left = -100;
        rect.right = rect.left + iconW;
        spawn = false;
    }

    //endregion

    //region TripleShot

    /// <summary>
    /// repeatable method to test collision for spawning a powerup
    /// </summary>
    /// <param name="bullet">the shell that is being fired</param>
    private boolean spawnCollisions(Rect rect) {

        //the shells position is checked against the border of the game
        if ((rect.left < originX) || (rect.right > screenX) || (rect.bottom > screenY) || (rect.top < originY))
            return false;
        //the shells position is checked against the picturebox border of the tank
        else if (Rect.intersects(rect,player.getRect()) || Rect.intersects(rect,player.getEnemy().getRect()))
            return false;

        return true;
    }

    //endregion
}
