package com.dev.notes.presenter;

import com.dev.notes.bases.BasePresenter;

public interface NotesPresenter extends BasePresenter {
    void onNoteClick(long id);
    void onAddNewNoteClick();
    void logout();
}
