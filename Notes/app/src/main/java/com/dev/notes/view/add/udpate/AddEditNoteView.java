package com.dev.notes.view.add.udpate;

import android.support.annotation.StringRes;

import com.dev.notes.model.NoteModel;

public interface AddEditNoteView {
    void finish();
    void showError(@StringRes int error);
    void showNoteDetails(NoteModel note);
}
