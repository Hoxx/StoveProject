package com.x.stove;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.x.firerouter.FireRouter;
import com.x.firerouterannotation.FireRule;

@FireRule(alias = "AppMain")
public class AppMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        findViewById(R.id.tv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireRouter.startActivity("Other");
            }
        });

    }
}
