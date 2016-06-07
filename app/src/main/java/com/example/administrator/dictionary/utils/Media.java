package com.example.administrator.dictionary.utils;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/5/21.
 */
public class Media {

    public static MediaPlayer mediaPlayer=new MediaPlayer();

    public Media(){
        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
    }


    public static void musicSound(final String mp3Url) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(mp3Url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMediaStop() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMediaRelease(){
        try {
            if(mediaPlayer!=null) {
                mediaPlayer.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
