package com.x.stove.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.meta.xyx.R;

/**
 * @author Houxingxiu
 * Date : 2018/11/1
 */
public class XVideoView extends BaseVideoView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private int[] currentViewSize;
    private int[] videoSize;

    private FrameLayout video_control;
    private ImageView   video_voice;
    private ImageView   video_play;
    private ProgressBar video_progress;
    private ImageView   video_full_screen;
    private SeekBar     video_seek;

    private boolean seekVideoFromUser;
    private long    seekVideoFromUserProgress;

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
        currentViewSize = new int[2];

//        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.video_view_control, this, false);
//
//        video_control = rootView.findViewById(R.id.video_control);
//        video_voice = rootView.findViewById(R.id.video_voice);
//        video_play = rootView.findViewById(R.id.video_play);
//        video_progress = rootView.findViewById(R.id.video_progress);
//        video_full_screen = rootView.findViewById(R.id.video_full_screen);
//        video_seek = rootView.findViewById(R.id.video_seek);
//
//        video_voice.setOnClickListener(this);
//        video_play.setOnClickListener(this);
//        video_full_screen.setOnClickListener(this);
//
//        video_seek.setOnSeekBarChangeListener(this);
//
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        params.gravity = Gravity.CENTER;
//        addView(rootView, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_full_screen:
                if (isFullScreen()) {
                    setSmallScreen();
                    video_full_screen.setImageResource(R.drawable.item_video_full_screen);
                } else {
                    setFullScreen(videoSize[0], videoSize[1]);
                    video_full_screen.setImageResource(R.drawable.full_screen_videosmall_screen);
                }
                break;
            case R.id.video_voice:
                if (mVideoViewAction != null) {
                    if (mVideoViewAction.videoIsMute()) {
                        mVideoViewAction.onVideoAudioRestore();
                    } else {
                        mVideoViewAction.onVideoAudioMute();
                    }
                }
                break;
            case R.id.video_play:
                if (mVideoViewAction != null) {
                    if (mVideoViewAction.videoIsPlaying()) {
                        mVideoViewAction.onVideoPause();
                    } else {
                        mVideoViewAction.onVideoPlay();
                    }
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekVideoFromUser = fromUser;
        seekVideoFromUserProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekVideoFromUser && mVideoViewAction != null) {
            mVideoViewAction.onVideoSeekTo(seekVideoFromUserProgress);
        }
    }

    @Override
    View controlView() {
        return video_control;
    }

    @Override
    public void setVideoPlay() {
        if (isFullScreen()) {
            video_play.setImageResource(R.drawable.full_screen_video_play);
        } else {
            video_play.setImageResource(R.drawable.item_video_play);
        }
    }

    @Override
    public void setVideoPause() {
        if (isFullScreen()) {
            video_play.setImageResource(R.drawable.full_screen_video_pause);
        } else {
            video_play.setImageResource(R.drawable.item_video_pause);
        }
    }

    @Override
    public void setVideoPlayComplete() {
        setVideoPlay();
        setViewShowControl();
    }

    @Override
    public void setVideoPlayError() {
        setVideoPlay();
        setViewShowControl();
    }

    @Override
    public void setViewShowLoading() {
        video_progress.setVisibility(VISIBLE);
    }

    @Override
    public void setViewHideLoading() {
        video_progress.setVisibility(GONE);
    }

    @Override
    public void setViewAudioMute() {
        video_voice.setImageResource(R.drawable.full_screen_video_off_voice);
    }

    @Override
    public void setViewAudioRestore() {
        video_voice.setImageResource(R.drawable.full_screen_video_on_voice);
    }

    @Override
    public void setViewSeekBar(long max, long progress) {
        video_seek.setMax((int) max);
        video_seek.setProgress((int) progress);
    }

    @Override
    public void setViewSize(int videoWidth, int videoHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        videoSize = scaleVideoSize(videoWidth, videoHeight, currentViewSize[0], currentViewSize[1]);
        //        if (videoWidth > videoHeight) {
        //            videoSize = scaleVideoSize(videoWidth, videoHeight, currentViewSize[0], currentViewSize[1]);
        //        } else {
        //            if (videoSize == null) {
        //                videoSize = new int[2];
        //            }
        //            videoSize[0] = currentViewSize[1];
        //            videoSize[1] = currentViewSize[1];
        //        }
        layoutParams.width = videoSize[0];
        layoutParams.height = videoSize[1];
        setLayoutParams(layoutParams);
        if (mOnVideoSizeChange != null) {
            mOnVideoSizeChange.onSizeChange(videoSize[0], videoSize[1]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        currentViewSize[0] = getMeasuredWidth();
        currentViewSize[1] = getMeasuredHeight();
    }
}
