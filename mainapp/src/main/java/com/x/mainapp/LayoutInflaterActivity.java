package com.x.mainapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hxx.filelib.AssetFileManager;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class LayoutInflaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inflater_activity);

        AssetFileManager assetFileManager = new AssetFileManager(this);

        assetFileManager.copy("copy_to_data_data");
    }
}
