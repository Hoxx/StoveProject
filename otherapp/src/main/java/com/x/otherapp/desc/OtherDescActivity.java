package com.x.otherapp.desc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.x.otherapp.R;
import com.x.stoveannotation.FindId;
import com.x.stoveannotation.RouterRule;
import com.x.stoveinject.StoveInject;
import com.x.stoverouter.StoveRouter;


@RouterRule(alias = "OtherDescActivity")
public class OtherDescActivity extends AppCompatActivity {


    public Button btn_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        btn_desc = findViewById(R.id.desc_btn);

        btn_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoveRouter.startActivity("Other");
            }
        });
    }
}
