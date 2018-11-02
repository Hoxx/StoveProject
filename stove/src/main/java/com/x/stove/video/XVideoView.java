package com.x.stove.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.x.stove.R;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class XVideoView extends BaseVideoView {

    private int[] screenSize;
    private int[] videoSize;

    public XVideoView(Context context) {
        super(context);
    }

    public XVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void initialize() {
        screenSize = new int[2];
        screenSize[0] = getResources().getDisplayMetrics().widthPixels;
        screenSize[1] = getResources().getDisplayMetrics().heightPixels;

        findControlView(R.id.tv_screen).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFullScreen(videoSize[0], videoSize[1]);
            }
        });
        findControlView(R.id.tv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSmallScreen();
            }
        });

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mVideoViewAction != null) {
            mVideoViewAction.onViewAvailable(new Surface(surface));
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mVideoViewAction != null) {
            mVideoViewAction.onViewDestroyed();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    int controlViewLayout() {
        return R.layout.video_control;
    }

    @Override
    public void setVideoPlay() {

    }

    @Override
    public void setVideoPause() {

    }

    @Override
    public void setVideoPlayComplete() {

    }

    @Override
    public void setVideoPlayError() {

    }

    @Override
    public void setViewShowLoading() {

    }

    @Override
    public void setViewHideLoading() {

    }

    @Override
    public void setViewAudioMute() {

    }

    @Override
    public void setViewAudioRestore() {

    }

    @Override
    public void setViewSeekBar(long max, long progress) {
        VLog.D(this, "setViewSeekBar", "max--" + max + "--progress--" + progress);
    }

    @Override
    public void setViewSize(int videoWidth, int videoHeight) {
        VLog.D(this, "setViewSize", "videoWidth--" + videoWidth + "--videoHeight--" + videoHeight);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        videoSize = scaleVideoSize(videoWidth, videoHeight, screenSize[0], screenSize[1]);
        layoutParams.width = videoSize[0];
        layoutParams.height = videoSize[1];
        setLayoutParams(layoutParams);
    }

}
