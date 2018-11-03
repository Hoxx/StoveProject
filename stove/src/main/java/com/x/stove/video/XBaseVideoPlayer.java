package com.x.stove.video;

import android.view.Surface;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public interface XBaseVideoPlayer {

    /**
     * 准备视频
     */
    void prepare();

    /**
     * 准备视频并播放
     */
    void prepareAndPlay();

    /**
     * 设置视频播放路径
     */
    void setVideoData(String url);

    /**
     * 设置显示Surface
     */
    void setVideoSurface(Surface surface);

    /**
     * 播放视频
     */
    void setVideoPlay();

    /**
     * 暂停视频
     */
    void setVideoPause();

    /**
     * 停止视频
     */
    void setVideoStop();

    /**
     * 重置播放器
     */
    void setVideoReset();

    /**
     * 释放播放器资源
     */
    void setVideoRelease();

    /**
     * 静音视频
     */
    void setVideoAudioMute();

    /**
     * 恢复视频音量
     */
    void setVideoAudioRestore();

    /**
     * 定位视频
     */
    void setVideoSeekTo(long progress);

    /**
     * 设置回调
     */
    void setOnPlayerAction(OnVideoPlayerAction action);

    /**
     * 获取视频是否正在播放
     */
    boolean getVideoIsPlaying();

    /**
     * 获取视频是否静音
     */
    boolean getVideoIsMute();

    interface OnVideoPlayerAction {

        void onVideoSize(int videoWidth, int videoHeight);

        void onVideoPlayDuration(long max, long progress);

        void onVideoPlayComplete();

        void onVideoPlayError();

        void onVideoSeekStart();

        void onVideoSeekComplete();

        void onVideoLoading();

        void onVideoLoadingComplete();

        void onVideoAudioMute();

        void onVideoAudioRestore();

    }
}

