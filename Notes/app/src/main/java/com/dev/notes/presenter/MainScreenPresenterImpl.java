package com.dev.notes.presenter;

import android.content.Intent;

import com.dev.notes.model.realm.RealmController;
import com.dev.notes.view.MainView;

public class MainScreenPresenterImpl implements MainPresenter {
    private final RealmController controller;
    private MainView view;

    public MainScreenPresenterImpl(RealmController controller) {
        this.controller = controller;
    }

    @Override
    public void setView(Object view) {
        this.view = (MainView) view;
    }

    @Override
    public void clearView() {
        view = null;
    }

    @Override
    public void closeRealm() {
        controller.closeRealm();
    }

    @Override
    public Intent getIntentToStartApp() {
        if (null == controller.getCurrentUser()) {
            return view.goToLoginPageOrHome(false);
        }
        return view.goToLoginPageOrHome(true);
    }
}
