package com.x.stove.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @author Houxingxiu
 * Date : 2018/11/2
 */
public abstract class BaseVideoView extends FrameLayout implements TextureView.SurfaceTextureListener {

    protected OnVideoViewAction mVideoViewAction;
    private   View              controlView;
    private   int[]             screenSize;
    private   int[]             videoSize;
    private   ViewGroup         oldParent;
    private   boolean           isFullScreen;

    public BaseVideoView(Context context) {
        this(context, null);
    }

    public BaseVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenSize = new int[2];
        screenSize[0] = getResources().getDisplayMetrics().widthPixels;
        screenSize[1] = getResources().getDisplayMetrics().heightPixels;

        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        //创建显示视频的View
        TextureView textureView = new TextureView(getContext());
        textureView.setSurfaceTextureListener(this);
        addView(textureView, params);
        //创建显示控制器的View
        if (controlViewLayout() != 0) {
            controlView = LayoutInflater.from(getContext()).inflate(controlViewLayout(), this, false);
            addView(controlView, params);
        }
        initialize();
    }

    protected <T extends View> T findControlView(int viewResId) {
        if (controlView != null) {
            return controlView.findViewById(viewResId);
        }
        return null;
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

    protected int[] scaleVideoSize(float videoWidth, float videoHeight, float targetWidth, float targetHeight) {
        int[] size = new int[2];
        if (targetHeight <= 0 || targetWidth <= 0) {
            size[0] = (int) videoWidth;
            size[1] = (int) videoHeight;
        } else {
            float widthScale = videoWidth / targetWidth;
            float HeightScale = videoHeight / targetHeight;
            float scale = Math.max(widthScale, HeightScale);
            size[0] = (int) (videoWidth / scale);
            size[1] = (int) (videoHeight / scale);
        }
        return size;
    }

    public void setViewHideControl() {
        if (controlView != null) {
            controlView.animate().alpha(0f).setDuration(500).start();
        }
    }

    public void setViewShowControl() {
        if (controlView != null) {
            controlView.animate().alpha(1f).setDuration(500).start();
        }
        //正在播放视频，5秒后自动隐藏控制器View
        if (mVideoViewAction != null && mVideoViewAction.videoIsPlaying()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mVideoViewAction != null && mVideoViewAction.videoIsPlaying()) {
                        setViewHideControl();
                    }
                }
            }, 5000);
        }
    }

    /**
     * 全屏
     */
    public void setFullScreen(int videoWidth, int videoHeight) {
        if (getContext() instanceof Activity && !isFullScreen) {
            if (videoSize == null) {
                videoSize = new int[2];
            }
            videoSize[0] = videoWidth;
            videoSize[1] = videoHeight;
            Activity activity = (Activity) getContext();
            if (videoWidth > videoHeight) {
                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //横屏
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
            //全屏
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            oldParent = (ViewGroup) getParent();
            if (oldParent != null) {
                oldParent.removeView(this);
            }
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            int size[] = scaleVideoSize(videoWidth, videoHeight, screenSize[1], screenSize[0]);
            decorView.addView(this, new FrameLayout.LayoutParams(size[0], size[1]));
            isFullScreen = true;
        }
    }

    /**
     * 小屏
     */
    public void setSmallScreen() {
        if (getContext() instanceof Activity && isFullScreen) {
            Activity activity = (Activity) getContext();
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //竖屏
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            //取消全屏
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            if (oldParent != null) {
                oldParent.addView(this, new ViewGroup.LayoutParams(videoSize[0], videoSize[1]));
            }
            isFullScreen = false;
        }
    }

    public void setOnViewAction(OnVideoViewAction action) {
        this.mVideoViewAction = action;
    }

    abstract void initialize();

    abstract int controlViewLayout();

    abstract void setVideoPlay();

    abstract void setVideoPause();

    abstract void setVideoPlayComplete();

    abstract void setVideoPlayError();

    abstract void setViewShowLoading();

    abstract void setViewHideLoading();

    abstract void setViewAudioMute();

    abstract void setViewAudioRestore();

    abstract void setViewSeekBar(long max, long progress);

    abstract void setViewSize(int videoWidth, int videoHeight);

    public interface OnVideoViewAction {

        void onViewAvailable(Surface surface);

        void onViewDestroyed();

        void onVideoPlay();

        void onVideoPause();

        void onVideoSeekTo(long progress);

        void onVideoAudioMute();

        void onVideoAudioRestore();

        boolean videoIsPlaying();

    }

}
