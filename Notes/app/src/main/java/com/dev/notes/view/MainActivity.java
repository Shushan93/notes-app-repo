package com.dev.notes.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dev.notes.R;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.presenter.MainScreenPresenterImpl;
import com.dev.notes.view.account.LoginRegisterActivity;
import com.dev.notes.view.notes.NotesListActivity;

public class MainActivity extends SplashScreen implements MainView{

    MainScreenPresenterImpl presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainScreenPresenterImpl(new RealmController());
        presenter.setView(this);
    }

    @NonNull
    @Override
    public Intent getMainScreenIntent() {
        return presenter.getIntentToStartApp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.clearView();
    }

    @Override
    public Intent goToLoginPageOrHome(boolean hasCurrentUser) {
        Intent intent;
        if (hasCurrentUser) {
            intent = new Intent(this, NotesListActivity.class);
        } else {
            intent = new Intent(this, LoginRegisterActivity.class);
        }
        return intent;
    }
}
