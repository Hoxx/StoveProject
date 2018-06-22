package com.x.stove;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.x.firerouter.FireRouter;
import com.x.firerouter.onPutExtra;
import com.x.firerouterannotation.FireRule;

@FireRule(alias = "AppMain")
public class AppMainActivity extends AppCompatActivity implements View.OnClickListener, onPutExtra {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        findViewById(R.id.tv_main).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                FireRouter.startActivity("Other", this);
                break;
        }
    }

    @Override
    public void onExtra(String alias, Intent intent) {
        switch (alias) {
            case "Other":
                intent.putExtra("SDATA", "测试数据传输");
                break;
        }
    }
}
