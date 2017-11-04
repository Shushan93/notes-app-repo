package com.dev.notes.view.notes;

import com.dev.notes.model.NoteModel;

import io.realm.RealmList;

public interface NotesView {
    void showNotes(RealmList<NoteModel> notes);
    void showNoteDetailView(long id);
    void showAddNewNoteView();
    void goToLogin();
}
