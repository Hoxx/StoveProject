package com.x.otherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.x.stoveannotation.RouterRule;


@RouterRule(alias = "Other")
public class OtherMainActivity extends AppCompatActivity {


    public TextView tv_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);
        tv_other = findViewById(R.id.tv_other);
        if (getIntent() != null) {
            String data = getIntent().getStringExtra("SDATA");
            Log.e("TAG", "接收到的数据：" + data);
        }


    }
}
