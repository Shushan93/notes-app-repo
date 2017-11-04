package com.dev.notes.view.account;

import android.support.annotation.StringRes;

public interface AccountView {
    void showError(@StringRes int message);
    void signIn();
    void finish();
}
