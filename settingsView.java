package com.example.thomas.tankwar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;

public class settingsView extends AppCompatActivity {


    Tank tank;
    int images[];
    int diffbacks[];
    public static final String EXTRA_REPLY  = "android.example.thomas.tankwar.extra.REPLY";
    public static final String EXTRA_THING = "android.example.thomas.tankwar.extra.THING";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_view);
        Intent intent = getIntent();

        images = intent.getIntArrayExtra(MainActivity.EXTRA_MESSAGE);



        diffbacks = intent.getIntArrayExtra(MainActivity.DIFFBACKS_MESSAGE);

        configureBackButton();
        tankColorChange();
        changeBackground();
    }

    private void changeBackground() {
        Button backgroundChange = (Button) findViewById(R.id.backgroundIce);
        backgroundChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diffbacks = new int[]{
                        R.drawable.ice,

                };
                returnBack(v);
                finish();
            }
        });

    }

    private void tankColorChange() {
        Button tankColor = (Button) findViewById(R.id.tankColor);
        tankColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images = new int[]{
                        R.drawable.enemy1,
                        R.drawable.enemy2,
                        R.drawable.enemy3,
                        R.drawable.enemy4,
                        R.drawable.enemy5,
                        R.drawable.enemy6,
                        R.drawable.enemy7,
                        R.drawable.enemy8
                };
                returnReply(v);
                finish();
            }
        });
    }

    private void configureBackButton(){
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void returnReply(View view) {
        Intent replyIntent = new Intent();

        replyIntent.putExtra(EXTRA_REPLY, images);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void returnBack(View view) {
        Intent backgroundReply = new Intent();

        backgroundReply.putExtra(EXTRA_THING, diffbacks);
        setResult(RESULT_OK, backgroundReply);
        finish();
    }
}