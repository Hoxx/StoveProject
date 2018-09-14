package com.x.mainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.x.stoveannotation.FindId;
import com.x.stoveannotation.RouterRule;
import com.x.stoveinject.StoveInject;
import com.x.stoverouter.StoveRouter;
import com.x.stoverouter.onPutExtra;

@RouterRule(alias = "Main")
public class MainActivity extends AppCompatActivity implements View.OnClickListener, onPutExtra {

    @FindId(R.id.btn_main)
    public Button btn_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StoveInject.bind(this);

        btn_main.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main:
                StoveRouter.startActivity("InfoActivity", this);
                break;
        }
    }

    @Override
    public void onExtra(String alias, Intent intent) {
        switch (alias) {
            case "InfoActivity":
                intent.putExtra("SDATA", "测试数据传输");
                break;
        }
    }
}
