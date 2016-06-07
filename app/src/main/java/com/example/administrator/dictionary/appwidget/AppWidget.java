package com.example.administrator.dictionary.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.base.NotificationSearch;

/**
 * Created by Administrator on 2016/5/22.
 */
public class AppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent=new Intent(context, NotificationSearch.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.appwidget_ly);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_ly,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
