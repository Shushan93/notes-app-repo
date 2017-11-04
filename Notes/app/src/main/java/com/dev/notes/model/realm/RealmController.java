package com.dev.notes.model.realm;

import com.dev.notes.model.NoteModel;
import com.dev.notes.model.User;
import com.dev.notes.utils.Utils;

import java.util.Calendar;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmList;

public class RealmController {

    private Realm realm;

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public void closeRealm() {
        realm.close();
    }

    /**
     * @return all NoteModel objects for logged in user
     */
    public RealmList<NoteModel> getNotes() {
        User user = realm.where(User.class).equalTo("isLoggedIn", true).findFirst();
        if (user == null) {
            return new RealmList<>();
        }
        return user.getNotes();
    }

    /**
     * @return null if there is no signed in user
     */
    @Nullable
    public User getCurrentUser() {
        return realm.where(User.class).equalTo("isLoggedIn", true).findFirst();
    }

    /**
     * @return user object if there is such user in db
     */
    public User login(String username, String pass) {
        User user = realm.where(User.class).equalTo("username", username)
                .equalTo("password", pass).findFirst();
        if (user != null) {
            realm.executeTransaction(realm -> {
                user.setLoggedIn(true);
                realm.copyToRealmOrUpdate(user);
            });
        }
        return user;
    }

    /**
     * @param username entered username
     * @param pass entered password
     * @return false if the username already is used by other user
     */
    public boolean registerUser(String username, String pass) {
        User userForCheck = realm.where(User.class).equalTo("username", username).findFirst();
        if (userForCheck == null) {
            realm.executeTransaction(realm -> {
                Number currentIdNum = realm.where(User.class).max("id");
                int nextId = Utils.getNextId(currentIdNum);
                User user = new User();
                user.setUsername(username);
                user.setPassword(pass);
                user.setLoggedIn(true);
                user.setId(nextId);
                realm.copyToRealmOrUpdate(user);
            });
            return true;
        }
        return false;
    }

    public NoteModel getNote(long id) {
        return realm.where(NoteModel.class).equalTo("id", id).findFirst();
    }

    public void updateNote(long id, String title, String content, boolean isDateSet, Calendar calendar,
                           boolean isNotifOn, int color) {
        realm.executeTransaction(realm -> {
            NoteModel note = new NoteModel();
            note.setTitle(title);
            note.setNoteContext(content);
            note.setDateSet(isDateSet);
            note.setCalendar(calendar);
            note.setHasNotification(isNotifOn);
            note.setColor(color);
            note.setId(id);
            note.setUser(getCurrentUser());
            realm.copyToRealmOrUpdate(note);
        });
    }

    private User addUser(NoteModel noteModel) {
        User user = getCurrentUser();
        if (user != null) {
            user.getNotes().add(noteModel);
        }
        return user;
    }

    public void deleteNote(long id) {
        realm.executeTransaction(realm -> {
            NoteModel noteForDelete = realm.where(NoteModel.class).equalTo("id", id).findFirst();
            if (noteForDelete != null) {
                noteForDelete.deleteFromRealm();
            }
        });
    }

    public void addNewNote(String title, String content, boolean isDateSet, Calendar calendar,
                           boolean isNotifOn, int color) {
        realm.executeTransaction(realm -> {
            // increment index
            Number currentIdNum = realm.where(NoteModel.class).max("id");
            int nextId = Utils.getNextId(currentIdNum);
            NoteModel note = new NoteModel();
            note.setTitle(title);
            note.setNoteContext(content);
            note.setDateSet(isDateSet);
            note.setCalendar(calendar);
            note.setHasNotification(isNotifOn);
            note.setColor(color);
            note.setId(nextId);
            note.setUser(addUser(note));
            realm.copyToRealmOrUpdate(note); // using insert API
        });
    }

    public void logout() {
        realm.executeTransaction(realm -> {
            User user = realm.where(User.class).equalTo("isLoggedIn", true).findFirst();
            if (user != null) {
                user.setLoggedIn(false);
                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}
