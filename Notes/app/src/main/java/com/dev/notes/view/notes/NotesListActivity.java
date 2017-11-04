package com.dev.notes.view.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dev.notes.R;
import com.dev.notes.bases.BaseActivity;
import com.dev.notes.model.NoteModel;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.presenter.NotesPresenterImpl;
import com.dev.notes.view.account.LoginRegisterActivity;
import com.dev.notes.view.add.udpate.AddEditNoteActivity;
import com.dev.notes.view.notes.adapter.NoteListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NotesListActivity extends BaseActivity implements NotesView, NoteListAdapter.OnNoteItemClickListener {

    @BindView(R.id.notes_list)
    RecyclerView notesRecycler;

    @BindView(R.id.toolbar_actionbar)
    Toolbar mActionBarToolbar;

    private NoteListAdapter adapter;
    private NotesPresenterImpl presenter;
    private boolean isList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        ButterKnife.bind(this);
        setSupportActionBar(mActionBarToolbar);
        presenter = new NotesPresenterImpl(new RealmController());
        initNotesList();
    }

    private void initNotesList() {
        adapter = new NoteListAdapter();
        adapter.setOnNoteItemClickListener(this);
        setLayoutManager(new LinearLayoutManager(this));
        isList = true;
    }

    private void setLayoutManager(RecyclerView.LayoutManager linearManager) {
        notesRecycler.setLayoutManager(linearManager);
        notesRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.clearView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_toolbat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            presenter.onAddNewNoteClick();
            return true;
        } else if (id == R.id.action_logout) {
            presenter.logout();
            return true;
        } else if (id == R.id.action_change_style) {
            if (isList) {
                setLayoutManager(new GridLayoutManager(this, 2));
                isList = false;
                item.setIcon(R.drawable.list_icon);
            } else {
                setLayoutManager(new LinearLayoutManager(this));
                isList = true;
                item.setIcon(R.drawable.table_icon);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void closeRealm() {
        presenter.closeRealm();
    }

    @Override
    public void showNotes(RealmList<NoteModel> notes) {
        adapter.setNotes(notes);
    }

    @Override
    public void showNoteDetailView(long id) {
        startActivity(AddEditNoteActivity.getStartIntent(this, id));
    }

    @Override
    public void showAddNewNoteView() {
        startActivity(AddEditNoteActivity.getStartIntent(this, AddEditNoteActivity.CREATE_NEW_NOTE));
    }

    @Override
    public void goToLogin() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onNoteClick(long id) {
        presenter.onNoteClick(id);
    }
}
