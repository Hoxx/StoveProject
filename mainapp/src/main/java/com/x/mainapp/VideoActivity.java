package com.x.mainapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.x.stove.video.XVideoManager;
import com.x.stove.video.XVideoView;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class VideoActivity extends AppCompatActivity {


    private String videoUrl  = "http://mp4.vjshi.com/2018-07-20/17a793a595fa2b7a114b8f8cbfac5c01.mp4";
    private String videoUrl1 = "http://cdn.233xyx.com/upload/video/com.pipsqueakgames.shootthemoon/gandiaoyueliang.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        XVideoView videoView = findViewById(R.id.x_video_view);
        XVideoManager.getInstance().setVideoView(videoView);
        XVideoManager.getInstance().start(videoUrl);
    }

}
