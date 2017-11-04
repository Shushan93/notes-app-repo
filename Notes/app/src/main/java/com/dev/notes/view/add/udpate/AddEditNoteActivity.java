package com.dev.notes.view.add.udpate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dev.notes.R;
import com.dev.notes.bases.BaseActivity;
import com.dev.notes.model.NoteModel;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.presenter.AddEditPresenterImpl;
import com.dev.notes.utils.Utils;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddEditNoteActivity extends BaseActivity implements AddEditNoteView {

    private static final String EXTRA_ID = "EXTRA_ID";
    public static final long CREATE_NEW_NOTE = -1;

    @BindView(R.id.toolbar_actionbar)
    Toolbar mActionBarToolbar;
    @BindView(R.id.note_color_txt)
    View noteColorTxt;
    @BindView(R.id.choose_color_layout)
    LinearLayout colorLayout;
    @BindView(R.id.set_date_layout)
    LinearLayout dateLayout;
    @BindView(R.id.set_date_sw)
    SwitchCompat setDateSw;
    @BindView(R.id.receive_notification_sw)
    SwitchCompat receiveNotificationSw;
    @BindView(R.id.choose_date_btn)
    Button chooseDateBtn;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.choose_time_btn)
    Button chooseTimeBtn;
    @BindView(R.id.note_title_edit)
    EditText noteTitleEdit;
    @BindView(R.id.note_context_edit)
    EditText noteContextEdit;

    private int selectedColor;
    private long id;
    private Calendar dateAndTime = Calendar.getInstance();
    private AddEditPresenterImpl presenter;

    public static Intent getStartIntent(final Context context, final long isbn) {
        Intent intent = new Intent(context, AddEditNoteActivity.class);
        intent.putExtra(EXTRA_ID, isbn);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_note_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mActionBarToolbar);
        initListeners();
        id = getIntent().getLongExtra(EXTRA_ID, -1);
        if (id != CREATE_NEW_NOTE) {
            presenter = new AddEditPresenterImpl(new RealmController(), id);
            enableViews(false);
        } else {
            presenter = new AddEditPresenterImpl(new RealmController());
            setDefaultValues();
        }
    }


    private void setDefaultValues() {
        chooseDateBtn.setText(Utils.getOnlyDateInFormat(dateAndTime));
        chooseTimeBtn.setText(Utils.getOnlyTimeInFormat(dateAndTime));
        selectedColor = Color.WHITE;
        noteColorTxt.setBackgroundColor(selectedColor);
    }

    private void initListeners() {
        setDateSw.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                dateLayout.setVisibility(View.VISIBLE);
                receiveNotificationSw.setEnabled(true);
            } else {
                dateLayout.setVisibility(View.GONE);
                receiveNotificationSw.setChecked(false);
                receiveNotificationSw.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (id >= 0) {
            getMenuInflater().inflate(R.menu.add_edit_note_toolbar_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            enableViews(true);
            item.setEnabled(false);
            return true;
        } else if (id == R.id.action_delete) {
            presenter.deleteNote(this.id);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enableViews(boolean b) {
        noteTitleEdit.setEnabled(b);
        noteContextEdit.setEnabled(b);
        colorLayout.setEnabled(b);
        chooseDateBtn.setEnabled(b);
        chooseTimeBtn.setEnabled(b);
        setDateSw.setEnabled(b);
        receiveNotificationSw.setEnabled(b);
        saveBtn.setEnabled(b);
    }

    @OnClick(R.id.save_btn)
    public void saveNote() {
        if (id < 0) {
            presenter.saveNote(noteTitleEdit.getText().toString(),
                    noteContextEdit.getText().toString(), setDateSw.isChecked(), dateAndTime,
                    receiveNotificationSw.isChecked(), selectedColor);
        } else {
            presenter.updateNote(id, noteTitleEdit.getText().toString(),
                    noteContextEdit.getText().toString(), setDateSw.isChecked(), dateAndTime,
                    receiveNotificationSw.isChecked(), selectedColor);
        }

    }

    @OnClick(R.id.choose_time_btn)
    public void showTimePickerDialog() {
        new TimePickerDialog(AddEditNoteActivity.this, timeCallBack,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    @OnClick(R.id.choose_date_btn)
    public void showDatePickerDialog() {
        new DatePickerDialog(AddEditNoteActivity.this, dateCallBack,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    @OnClick(R.id.choose_color_layout)
    public void showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(AddEditNoteActivity.this)
                .setTitle(R.string.choose_color)
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(R.string.ok, (dialog, selectedColor, allColors) -> changeNoteColor(selectedColor))
                .build()
                .show();
    }

    /**
     * This function are showing the color which the user select
     *
     * @param color are given from colorPicker dialog when user choose it
     */
    private void changeNoteColor(int color) {
        selectedColor = color;
        noteColorTxt.setBackgroundColor(color);
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
    public void showError(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoteDetails(NoteModel note) {
        noteTitleEdit.setText(note.getTitle());
        noteContextEdit.setText(note.getNoteContext());
        setDateSw.setChecked(note.isDateSet());
        chooseDateBtn.setText(Utils.getOnlyDateInFormat(note.getCalendar()));
        chooseTimeBtn.setText(Utils.getOnlyTimeInFormat(note.getCalendar()));
        receiveNotificationSw.setChecked(note.isHasNotification());
        changeNoteColor(note.getColor());
        dateAndTime = note.getCalendar();
        if (note.isDateSet()) {
            dateLayout.setVisibility(View.VISIBLE);
        } else {
            dateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void closeRealm() {
        presenter.closeRealm();
    }


    TimePickerDialog.OnTimeSetListener timeCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            chooseTimeBtn.setText(Utils.getOnlyTimeInFormat(dateAndTime));
        }
    };

    DatePickerDialog.OnDateSetListener dateCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            chooseDateBtn.setText(Utils.getOnlyDateInFormat(dateAndTime));
        }
    };

}
