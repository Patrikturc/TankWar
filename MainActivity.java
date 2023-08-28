package com.example.thomas.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    TankWarView tankWarView;
    int images[];
    public static final int IMAGE_REQUEST = 1;
    public static final String EXTRA_MESSAGE = "android.example.thomas.tankwar.extra.MESSAGE";

    int diffbacks[];
    public static final  int DIFFBACKS_REQUEST = 1;
    public static final String DIFFBACKS_MESSAGE = "android.example.thomas.tankwar.extra.MESSAGE";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        images = new int[]{
                R.drawable.tankleft,
                R.drawable.tankright,
                R.drawable.tankup,
                R.drawable.tankdown,
                R.drawable.tankupl,
                R.drawable.tankupr,
                R.drawable.tankdownl,
                R.drawable.tankdownr,
        };

        diffbacks = new int[]{
                R.drawable.ice

        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        configurePlay();
        configureSettings();
        tankWarView = new TankWarView(this, size.x, (size.y-(size.y/10)), images,diffbacks);
        //setContentView(tankWarView);
    }

    private void configureSettings() {
        Button settingsButton = (Button) findViewById(R.id.button4);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settingsView.class);
                intent.putExtra(EXTRA_MESSAGE, images);
                intent.putExtra(DIFFBACKS_MESSAGE,diffbacks);
                startActivityForResult(intent, IMAGE_REQUEST);
                startActivityForResult(intent, DIFFBACKS_REQUEST);





            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                images = data.getIntArrayExtra(settingsView.EXTRA_REPLY);
            }
        }
        else if(requestCode == DIFFBACKS_REQUEST){
            if(requestCode == RESULT_OK){
                diffbacks = data.getIntArrayExtra(settingsView.EXTRA_THING);
            }
        }
    }

    private void configurePlay(){
        Button playButton = (Button) findViewById(R.id.button3);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tankWarView.setImages(images);
                setContentView(tankWarView);
            }
        });
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        tankWarView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        tankWarView.pause();
    }
}