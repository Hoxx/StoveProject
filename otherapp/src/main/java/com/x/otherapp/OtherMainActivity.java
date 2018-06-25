package com.x.otherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.x.firerouterannotation.FireRule;

@FireRule(alias = "OtherActivity")
public class OtherMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);
        if (getIntent() != null) {
            String data = getIntent().getStringExtra("SDATA");
            Log.e("TAG", "接收到的数据：" + data);
        }

    }
}
