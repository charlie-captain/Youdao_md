package com.example.administrator.dictionary.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.base.NotificationSearch;

/**
 * Created by Administrator on 2016/5/21.
 */
public class NtfService extends Service {

    private static boolean isStart=false;

    public static boolean isStart() {
        return isStart;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        NotificationCompat.Builder notification=new NotificationCompat.Builder(this);
        RemoteViews remoteView= new RemoteViews(getPackageName(), R.layout.notification);

        Intent intent=new Intent(this, NotificationSearch.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //notification.setContentIntent(pendingIntent);
        notification.setContent(remoteView).setContentIntent(pendingIntent).setSmallIcon(R.drawable.search_red);

        startForeground(1,notification.build());
        isStart=true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isStart=false;
    }


}
