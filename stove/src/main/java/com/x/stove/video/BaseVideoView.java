package com.x.stove.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
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
    protected OnVideoSizeChange mOnVideoSizeChange;
    private   int[]             screenSize;
    private   int[]             videoSize;
    private   ViewGroup         oldParent;
    private   int               oldSystemUiVisibility;
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
        initScreenSize();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        //创建显示视频的View
        TextureView textureView = new TextureView(getContext());
        textureView.setSurfaceTextureListener(this);
        addView(textureView, params);
        initialize();
    }

    /**
     * 获取屏幕尺寸
     */
    private void initScreenSize() {
        if (screenSize == null) {
            screenSize = new int[2];
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowMgr.getDefaultDisplay().getRealMetrics(dm);
            } else {
                windowMgr.getDefaultDisplay().getMetrics(dm);
            }
        }
        // 获取宽度
        screenSize[0] = dm.widthPixels;
        // 获取高度
        screenSize[1] = dm.heightPixels;
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

    /**
     * 计算视频缩放尺寸
     *
     * @param videoWidth   视频宽度
     * @param videoHeight  视频高度
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 计算后的宽高
     */
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
            int[] size;
            if (videoWidth > videoHeight && getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //横屏
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                size = scaleVideoSize(videoWidth, videoHeight, screenSize[1], screenSize[0]);
            } else {
                size = scaleVideoSize(videoWidth, videoHeight, screenSize[0], screenSize[1]);
            }
            //全屏
            setActivityFullScreen(activity);
            oldParent = (ViewGroup) getParent();
            if (oldParent != null) {
                oldParent.removeView(this);
            }
            FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
            decorView.addView(this, new LayoutParams(size[0], size[1]));
            isFullScreen = true;
        }
    }

    /**
     * 设置Activity全屏
     */
    private void setActivityFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏虚拟按键，并且全屏
        oldSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        int systemUiVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 清除全屏
     */
    private void clearActivityFullScreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().getDecorView().setSystemUiVisibility(oldSystemUiVisibility);
    }

    /**
     * 小屏
     */
    public void setSmallScreen() {
        if (getContext() instanceof Activity && isFullScreen) {
            Activity activity = (Activity) getContext();
            //如果是横屏，切换回竖屏
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            //取消全屏
            clearActivityFullScreen(activity);
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            if (oldParent != null) {
                oldParent.addView(this, new ViewGroup.LayoutParams(videoSize[0], videoSize[1]));
            }
            isFullScreen = false;
        }
    }

    /**
     * 是否是全屏
     */
    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setOnVideoSizeChange(OnVideoSizeChange onVideoSizeChange) {
        mOnVideoSizeChange = onVideoSizeChange;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                if (controlView() != null) {
                    if (controlView().getAlpha() < 1f) {
                        setViewShowControl();
                    } else {
                        setViewHideControl();
                    }
                }
                break;
        }
        return true;
    }

    public void setViewHideControl() {
        if (controlView() != null) {
            controlView().animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
                @Override
                public void run() {
                    controlView().setVisibility(GONE);
                }
            }).start();
        }
    }

    public void setViewShowControl() {
        if (controlView() != null) {
            controlView().setVisibility(VISIBLE);
            controlView().animate().alpha(1f).setDuration(500).start();
        }
        //正在播放视频，5秒后自动隐藏控制器View
        if (mVideoViewAction != null && mVideoViewAction.videoIsPlaying()) {
            removeCallbacks(delayHideControl);
            postDelayed(delayHideControl, 5000);
        }
    }

    private Runnable delayHideControl = new Runnable() {
        @Override
        public void run() {
            if (mVideoViewAction != null && mVideoViewAction.videoIsPlaying()) {
                setViewHideControl();
            }
        }
    };

    public void setOnViewAction(OnVideoViewAction action) {
        this.mVideoViewAction = action;
    }

    abstract void initialize();

    abstract View controlView();

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

        boolean videoIsMute();

    }

    public interface OnVideoSizeChange {
        void onSizeChange(int width, int height);
    }

}
