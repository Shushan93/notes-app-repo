package com.dev.notes.model;

import com.dev.notes.utils.Utils;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NoteModel extends RealmObject{
    @PrimaryKey
    private long id;

    private String title;
    private String noteContext;
    private boolean isDateSet;
    private boolean hasNotification;
    private byte[] serializedCalendar;
    private int color;
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteContext() {
        return noteContext;
    }

    public void setNoteContext(String noteContext) {
        this.noteContext = noteContext;
    }

    public boolean isDateSet() {
        return isDateSet;
    }

    public void setDateSet(boolean dateSet) {
        isDateSet = dateSet;
    }

    public boolean isHasNotification() {
        return hasNotification;
    }

    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public Calendar getCalendar() {
        return Utils.deserializeCalendar(serializedCalendar);
    }

    public void setCalendar(Calendar calendar) {
        this.serializedCalendar = Utils.serializeCalendar(calendar);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
