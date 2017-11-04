package com.dev.notes.presenter;

import android.content.Intent;

import com.dev.notes.bases.BasePresenter;

public interface MainPresenter extends BasePresenter {
    Intent getIntentToStartApp();
}
