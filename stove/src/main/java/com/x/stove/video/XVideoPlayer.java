package com.x.stove.video;

import android.text.TextUtils;
import android.view.Surface;

import com.meta.xyx.utils.LogUtil;

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
    //是否设置新的播放地址
    private boolean                              hasNewVideoUrl;
    //是否准备完成自动播放
    private boolean                              prepareAndPlay;
    //当前播放视频的时间总长度
    private long                                 videoDuration;
    //是否静音
    private boolean                              isMute;

    public XVideoPlayer() {
        initMediaPlayer();
        initMediaManager();
    }

    private void initMediaManager() {
    }

    private void initMediaPlayer() {
        if (mIMediaPlayer == null) {
            mIMediaPlayer = new IjkMediaPlayer();
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
        if (hasNewVideoUrl) {
            this.prepareAndPlay = false;
            mIMediaPlayer.prepareAsync();
            hasNewVideoUrl = false;
        }
    }

    @Override
    public void prepareAndPlay() {
        if (hasNewVideoUrl) {
            this.prepareAndPlay = true;
            mIMediaPlayer.prepareAsync();
            hasNewVideoUrl = false;
        }
    }

    @Override
    public void setVideoData(String url) {
        LogUtil.e("HXX-TAG-----设置的视频地址:" + url);
        try {
            if (TextUtils.isEmpty(url)) {
                LogUtil.s("HXX-TAG-----当前设置的视频地址为空");
                return;
            }
            String videoUrl = mIMediaPlayer.getDataSource();
            if (!TextUtils.isEmpty(videoUrl)) {
                if (TextUtils.equals(videoUrl, url)) {
                    LogUtil.s("HXX-TAG-----当前设置的视频地址重复");
                    return;
                } else {
//                    mIMediaPlayer.stop();
//                    mIMediaPlayer.reset();
                }
            }
            hasNewVideoUrl = true;
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
        mIMediaPlayer.stop();
    }

    @Override
    public void setVideoReset() {
        setVideoStop();
        mIMediaPlayer.reset();
    }

    @Override
    public void setVideoRelease() {
        setVideoReset();
        mIMediaPlayer.release();
    }

    @Override
    public void setVideoAudioMute() {
        mIMediaPlayer.setVolume(0f, 0f);
        isMute = true;
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoAudioMute();
        }
    }

    @Override
    public void setVideoAudioRestore() {
        mIMediaPlayer.setVolume(1f, 1f);
        isMute = false;
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

    @Override
    public boolean getVideoIsMute() {
        return isMute;
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
        if (LogUtil.isLog()) {
            LogUtil.s(this, "onError-i", i);
        }
        if (mOnVideoPlayerAction != null) {
            mOnVideoPlayerAction.onVideoPlayError();
        }
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        if (LogUtil.isLog()) {
            LogUtil.s(this, "onInfo-i", i);
            LogUtil.s(this, "onInfo-i1", i1);
            LogUtil.s(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START ? "媒体信息视频寻求渲染开始" : "");
            LogUtil.s(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START ? "媒体信息音频寻求渲染开始" : "");
            LogUtil.s(this, "onInfo-i", i == IMediaPlayer.MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE ? "媒体信息媒体准确的搜索完整" : "");
            LogUtil.s(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START ? "媒体信息视频寻求渲染开始" : "");
            LogUtil.s(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START ? "媒体信息音频寻求渲染开始" : "");
            LogUtil.s(this, "onInfo-i1", i1 == IMediaPlayer.MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE ? "媒体信息媒体准确的搜索完整" : "");
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        if (mOnVideoPlayerAction != null && i > 0 && i1 > 0) {
            mOnVideoPlayerAction.onVideoSize(i, i1);
        }
    }
}
