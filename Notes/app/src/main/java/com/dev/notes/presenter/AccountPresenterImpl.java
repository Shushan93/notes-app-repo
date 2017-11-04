package com.dev.notes.presenter;

import android.content.Context;

import com.dev.notes.R;
import com.dev.notes.model.NoteModel;
import com.dev.notes.model.User;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.notification.NotificationScheduler;
import com.dev.notes.utils.Utils;
import com.dev.notes.view.account.AccountView;

public class AccountPresenterImpl implements AccountPresenter {

    private final RealmController realmController;
    private AccountView view;

    public AccountPresenterImpl(RealmController realmController) {
        this.realmController = realmController;
    }

    @Override
    public void setView(Object view) {
        this.view = (AccountView) view;
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
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError(R.string.err_all_fields_are_required);
            return;
        }
        User user = realmController.login(username, Utils.md5(password));
        if (user != null) {
            view.signIn();
            view.finish();
            setThisUsersNotesNotifications();
        } else {
            view.showError(R.string.err_incorrect_username_or_pass);
        }
    }

    private void setThisUsersNotesNotifications() {
        for (NoteModel noteModel : realmController.getNotes()) {
            if (noteModel.isHasNotification()) {
                NotificationScheduler.setReminder((Context) view, noteModel.getCalendar(),
                        noteModel.getTitle(), noteModel.getNoteContext(), noteModel.getId());
            }
        }

    }

    @Override
    public void registration(String username, String password, String confirmPass) {
        if (username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            view.showError(R.string.err_all_fields_are_required);
            return;
        }
        if (!password.equals(confirmPass)) {
            view.showError(R.string.err_passwords_does_not_match);
            return;
        }
        if (realmController.registerUser(username, Utils.md5(password))) {
            view.signIn();
            view.finish();
            setThisUsersNotesNotifications();
        } else {
            view.showError(R.string.err_username_is_used);
        }
    }
}
