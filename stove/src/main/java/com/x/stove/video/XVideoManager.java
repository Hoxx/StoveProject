package com.x.stove.video;

import android.view.Surface;

/**
 * @author Houxingxiu
 * Date : 2018/11/2
 */
public class XVideoManager {


    private static XVideoManager    instance;
    private        XBaseVideoPlayer mVideoPlayer;
    private        BaseVideoView    mVideoView;


    public static XVideoManager getInstance() {
        if (instance == null) {
            synchronized (XVideoManager.class) {
                if (instance == null) {
                    instance = new XVideoManager();
                }
            }
        }
        return instance;
    }

    private XVideoManager() {
        mVideoPlayer = new XVideoPlayer();
    }

    /**
     * 设置视频View
     */
    public void setVideoView(BaseVideoView videoView) {
        mVideoView = videoView;
        initialize();
    }

    /**
     * 设置视频地址，并播放
     */
    public void start(String videoUrl) {
        mVideoPlayer.setVideoData(videoUrl);
        mVideoPlayer.prepareAndPlay();
    }

    /**
     * 初始化
     */
    private void initialize() {
        checkPlayer();
        checkView();
        initPlayerAction();
        initViewAction();
    }

    /**
     * 设置Player相关操作
     */
    private void initPlayerAction() {
        mVideoPlayer.setOnPlayerAction(new XBaseVideoPlayer.OnVideoPlayerAction() {
            @Override
            public void onVideoSize(int videoWidth, int videoHeight) {
                mVideoView.setViewSize(videoWidth, videoHeight);
            }

            @Override
            public void onVideoPlayDuration(long max, long progress) {
                mVideoView.setViewSeekBar(max, progress);
            }

            @Override
            public void onVideoPlayComplete() {
                mVideoView.setVideoPlayComplete();
            }

            @Override
            public void onVideoPlayError() {
                mVideoView.setVideoPlayError();
            }

            @Override
            public void onVideoSeekStart() {
                mVideoView.setVideoPause();

            }

            @Override
            public void onVideoSeekComplete() {
                mVideoView.setVideoPlay();

            }

            @Override
            public void onVideoLoading() {
                mVideoView.setViewShowLoading();
                mVideoView.setViewShowControl();
            }

            @Override
            public void onVideoLoadingComplete() {
                mVideoView.setViewHideLoading();
                mVideoView.setViewHideControl();
            }

            @Override
            public void onVideoAudioMute() {
                mVideoView.setViewAudioMute();
            }

            @Override
            public void onVideoAudioRestore() {
                mVideoView.setViewAudioRestore();
            }
        });
    }

    /**
     * 设置View相关操作
     */
    private void initViewAction() {
        mVideoView.setOnViewAction(new BaseVideoView.OnVideoViewAction() {
            @Override
            public void onViewAvailable(Surface surface) {
                mVideoPlayer.setVideoSurface(surface);
            }

            @Override
            public void onViewDestroyed() {

            }

            @Override
            public void onVideoPlay() {
                mVideoPlayer.setVideoPlay();
            }

            @Override
            public void onVideoPause() {
                mVideoPlayer.setVideoPause();
            }

            @Override
            public void onVideoSeekTo(long progress) {
                mVideoPlayer.setVideoSeekTo(progress);
            }

            @Override
            public void onVideoAudioMute() {
                mVideoPlayer.setVideoAudioMute();
            }

            @Override
            public void onVideoAudioRestore() {
                mVideoPlayer.setVideoAudioRestore();
            }

            @Override
            public boolean videoIsPlaying() {
                return mVideoPlayer.getVideoIsPlaying();
            }
        });
    }

    private void checkPlayer() {
        if (mVideoPlayer == null) {
            throw new NullPointerException("XVideoManager mVideoPlayer is null");
        }
    }

    private void checkView() {
        if (mVideoView == null) {
            throw new NullPointerException("XVideoManager mVideoView is null");
        }
    }

}
