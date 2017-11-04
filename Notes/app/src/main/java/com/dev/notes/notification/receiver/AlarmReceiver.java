package com.dev.notes.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dev.notes.notification.NotificationScheduler;
import com.dev.notes.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("description");
        long noteId = intent.getLongExtra("noteId", 0);
        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                title, content, noteId);

    }
}