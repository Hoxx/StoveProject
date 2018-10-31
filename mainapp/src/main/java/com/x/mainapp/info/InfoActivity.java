package com.x.mainapp.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.x.mainapp.R;
import com.x.stoveannotation.FindId;
import com.x.stoveannotation.RouterRule;
import com.x.stoveinject.StoveInject;
import com.x.stoverouter.StoveRouter;

@RouterRule(alias = "InfoActivity")
public class InfoActivity extends AppCompatActivity {

    @FindId(R.id.btn_info_activity)
    public Button btn_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        StoveInject.bind(this);

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoveRouter.startActivity(InfoActivity.this, "OtherDescActivity");
            }
        });

    }
}
