package com.dev.notes.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class SplashScreen extends AppCompatActivity implements Handler.Callback {

    private static final long DEFAULT_SPLASH_SCREEN_TIMEOUT = 1000;
    private static final int MSG_SPLASH_SCREEN_TIMEOUT = 1;

    private Intent mainScreenIntent;

    private Handler handler = new Handler(this);

    @Override
    protected void onStart() {
        super.onStart();
        startSplashScreenTimer();
    }

    private void startSplashScreenTimer() {
        handler.sendEmptyMessageDelayed(MSG_SPLASH_SCREEN_TIMEOUT, getSplashScreenTimeout());
    }

    protected long getSplashScreenTimeout() {
        return DEFAULT_SPLASH_SCREEN_TIMEOUT;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SPLASH_SCREEN_TIMEOUT:
                mainScreenIntent = getMainScreenIntent();
                startActivity(mainScreenIntent);
                finish();
                return true;
        }
        return false;
    }

    @NonNull
    public abstract Intent getMainScreenIntent();
}
