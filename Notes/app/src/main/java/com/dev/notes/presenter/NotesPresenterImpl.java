package com.dev.notes.presenter;

import android.content.Context;

import com.dev.notes.model.realm.RealmController;
import com.dev.notes.notification.NotificationScheduler;
import com.dev.notes.view.notes.NotesView;

public class NotesPresenterImpl implements NotesPresenter {

    private final RealmController realmController;
    private NotesView view;
    private boolean isAlreadyShown;

    public NotesPresenterImpl(RealmController realmController) {
        this.realmController = realmController;
    }

    @Override
    public void setView(Object view) {
        this.view = (NotesView) view;
        showNotesIfNeeded();
    }

    @Override
    public void clearView() {
        view = null;
    }

    private void showNotesIfNeeded() {
        if (!isAlreadyShown) {
            view.showNotes(realmController.getNotes());
            isAlreadyShown = true;
        }
    }

    @Override
    public void closeRealm() {
        realmController.closeRealm();
    }

    @Override
    public void onNoteClick(long id) {
        view.showNoteDetailView(id);
    }

    @Override
    public void onAddNewNoteClick() {
        view.showAddNewNoteView();
    }

    @Override
    public void logout() {
        realmController.logout();
        view.goToLogin();
        NotificationScheduler.cancelAll((Context) view);
    }
}
