package com.dev.notes.bases;

public interface BasePresenter {
    void setView(Object view);
    void clearView();
    void closeRealm(); 
}