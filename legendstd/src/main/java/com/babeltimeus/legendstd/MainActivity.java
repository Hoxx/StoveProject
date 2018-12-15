package com.babeltimeus.legendstd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hxx.filelib.AssetFileManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AssetFileManager assetFileManager = new AssetFileManager(this);


        assetFileManager.copy("com.babeltimeus.legendstd");


    }
}
