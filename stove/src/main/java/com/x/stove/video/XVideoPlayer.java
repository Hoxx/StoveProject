package com.x.stove.video;

import android.view.Surface;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class XVideoPlayer implements XBaseVideoPlayer, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener {


    private IMediaPlayer                         mIMediaPlayer;
    private XBaseVideoPlayer.OnVideoPlayerAction mOnVideoPlayerAction;
    //是否准备完成自动播放
    private boolean                              prepareAndPlay;
    //当前播放视频的时间总长度
    private long                                 videoDuration;

    public XVideoPlayer() {
        initMediaPlayer();
        initMediaManager();
    }

    private void initMediaManager() {
    }

    private void initMediaPlayer() {
        if (mIMediaPlayer == null) {
            mIMediaPlayer = new IjkMediaPlayer();
            mIMediaPlayer.setScreenOnWhilePlaying(true);
            mIMediaPlayer.setOnPreparedListener(this);
            mIMediaPlayer.setOnVideoSizeChangedListener(this);
            mIMediaPlayer.setOnCompletionListener(this);
            mIMediaPlayer.setOnBufferingUpdateListener(this);
            mIMediaPlayer.setOnSeekCompleteListener(this);
            mIMediaPlayer.setOnErrorListener(this);
            mIMediaPlayer.setOnInfoListener(this);
        }
    }

    @Override
    public void prepare() {
        this.prepareAndPlay = false;
        mIMediaPlayer.prepareAsync();
    }

    @Override
    public void prepareAndPlay() {
        this.prepareAndPlay = true;
        mIMediaPlayer.prepareAsync();
    }

    @Override
    public void setVideoData(String url) {
        try {
            mIMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVideoSurface(Surface surface) {
        mIMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVideoPlay() {
        mIMediaPlayer.start();
    }

    @Override
    public void setVideoPause() {
        mIMediaPlayer.pause();
    }

    @Override
    public void setVideoStop() {

    }

    @Override
    public void setVideoAudioMute() {
        mIMediaPlayer.setVolume(0f, 0f);
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoAudioMute();
        }
    }

    @Override
    public void setVideoAudioRestore() {
        mIMediaPlayer.setVolume(1f, 1f);
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoAudioRestore();
        }
    }

    @Override
    public void setVideoSeekTo(long progress) {
        mIMediaPlayer.seekTo(progress);
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoSeekStart();
        }
    }

    @Override
    public void setOnPlayerAction(OnVideoPlayerAction action) {
        this.mOnVideoPlayerAction = action;
    }

    @Override
    public boolean getVideoIsPlaying() {
        return mIMediaPlayer.isPlaying();
    }

    //第三方播放器回调-----以下
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        int videoWidth = iMediaPlayer.getVideoWidth();
        int videoHeight = iMediaPlayer.getVideoHeight();
        if (mOnVideoPlayerAction != null && videoWidth > 0 && videoHeight > 0) {
            mOnVideoPlayerAction.onVideoSize(videoWidth, videoHeight);
        }
        videoDuration = iMediaPlayer.getDuration();
        long progress = iMediaPlayer.getCurrentPosition();
        if (mOnVideoPlayerAction != null && videoDuration > 0) {
            mOnVideoPlayerAction.onVideoPlayDuration(videoDuration, progress >= 0 ? progress : 0);
        }
        if (prepareAndPlay && !mIMediaPlayer.isPlaying()) {
            mIMediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoPlayComplete();
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        switch (i) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mOnVideoPlayerAction != null) {
                    mOnVideoPlayerAction.onVideoLoading();
                }
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (mOnVideoPlayerAction != null) {
                    mOnVideoPlayerAction.onVideoLoadingComplete();
                }
                break;
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoSeekComplete();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoPlayError();
        }
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        VLog.D(this, "onInfo-i", i);
        VLog.D(this, "onInfo-i1", i1);
        VLog.D(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START ? "媒体信息视频寻求渲染开始" : "");
        VLog.D(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START ? "媒体信息音频寻求渲染开始" : "");
        VLog.D(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE ? "媒体信息媒体准确的搜索完整" : "");
        VLog.D(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START ? "媒体信息视频寻求渲染开始" : "");
        VLog.D(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START ? "媒体信息音频寻求渲染开始" : "");
        VLog.D(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE ? "媒体信息媒体准确的搜索完整" : "");
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        if (mOnVideoPlayerAction != null && i > 0 && i1 > 0) {
            mOnVideoPlayerAction.onVideoSize(i, i1);
        }
    }
}
