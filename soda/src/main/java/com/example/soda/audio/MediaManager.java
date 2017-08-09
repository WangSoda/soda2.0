package com.example.soda.audio;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.io.IOException;

/**
 * Created by soda on 2017/8/2.
 */

public class MediaManager {
    private static MediaPlayer mMediaPlayer;

    private static boolean isPause;

    public static void palySound(String filePath,
                                 OnCompletionListener onCompletionListener){
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else {
            mMediaPlayer.reset();
        }

//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        try {
            mMediaPlayer.setDataSource(filePath);
        } catch (IOException e) {
            Log.d("MediaManagerError","设置路径出错");
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.d("MediaManagerError","准备媒体出错");
            e.printStackTrace();
        }
        mMediaPlayer.start();

    }
}
