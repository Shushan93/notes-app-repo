package com.dev.notes.view;

import android.content.Intent;

public interface MainView {
    Intent goToLoginPageOrHome(boolean hasCurrentUser);
}
