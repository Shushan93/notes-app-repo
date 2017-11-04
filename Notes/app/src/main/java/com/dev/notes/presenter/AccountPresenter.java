package com.dev.notes.presenter;

import com.dev.notes.bases.BasePresenter;

public interface AccountPresenter extends BasePresenter {
    void login(String username, String password);
    void registration(String username, String pass, String confirmPass);
}
