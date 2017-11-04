package com.dev.notes.presenter;

import android.content.Context;

import com.dev.notes.R;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.notification.NotificationScheduler;
import com.dev.notes.view.add.udpate.AddEditNoteView;

import java.util.Calendar;

public class AddEditPresenterImpl implements AddEditNotePresenter {

    private RealmController realmController;
    private AddEditNoteView view;
    private long noteId = -1;// creating new note

    /**
     * This constructor is called from creating new note page
     */
    public AddEditPresenterImpl(RealmController realmController) {
        this.realmController = realmController;
    }

    /**
     * This constructor is called for updating already existed note
     * @param noteId selected note id
     */
    public AddEditPresenterImpl(RealmController realmController, long noteId) {
        this.realmController = realmController;
        this.noteId = noteId;
    }

    @Override
    public void deleteNote(long id) {
        realmController.deleteNote(id);
        cancelNotification(id);
        view.finish();
    }

    @Override
    public void saveNote(String title, String content, boolean isDateSet,
                         Calendar calendar, boolean isNotifOn, int color) {

        if (title.isEmpty() || content.isEmpty()) {
            view.showError(R.string.err_required_fields);
            return;
        }
        if (isNotifOn && !isDateSet) {
            view.showError(R.string.err_date_not_set);
            return;
        }
        if (isNotifOn) {
            setNotification(calendar, title, content, noteId);
        }
        realmController.addNewNote(title, content, isDateSet, calendar, isNotifOn, color);
        view.finish();
    }

    private void setNotification(Calendar calendar, String title, String content, long noteId) {
        NotificationScheduler.setReminder((Context) view, calendar,
                title, content, noteId);
    }

    private void cancelNotification(long noteId) {
        NotificationScheduler.cancelReminder((Context) view, noteId);
    }

    @Override
    public void setView(Object view) {
        this.view = (AddEditNoteView) view;
        if (noteId >= 0) {
            this.view.showNoteDetails(realmController.getNote(noteId));
        }
    }

    @Override
    public void clearView() {
        view = null;
    }

    @Override
    public void closeRealm() {
        realmController.closeRealm();
    }

    @Override
    public void updateNote(long id, String title, String content, boolean isDateSet, Calendar calendar, boolean isNotifOn, int color) {

        if (title.isEmpty() || content.isEmpty()) {
            view.showError(R.string.err_required_fields);
            return;
        }
        if (isNotifOn && !isDateSet) {
            view.showError(R.string.err_date_not_set);
            return;
        }
        if (isNotifOn) {
            setNotification(calendar, title, content, noteId);
        } else {
            cancelNotification(noteId);
        }
        realmController.updateNote(id, title, content, isDateSet, calendar, isNotifOn, color);
        view.finish();
    }
}
