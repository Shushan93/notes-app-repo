package com.dev.notes.presenter;

import com.dev.notes.bases.BasePresenter;

import java.util.Calendar;

public interface AddEditNotePresenter extends BasePresenter {
    void deleteNote(long id);
    void saveNote(String title, String context, boolean isDateSet, Calendar calendar, boolean isNotifOn, int color);
    void updateNote(long id, String title, String context, boolean isDateSet, Calendar calendar, boolean isNotifOn, int color);
}
