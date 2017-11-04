package com.dev.notes.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.notes.R;
import com.dev.notes.bases.BaseActivity;
import com.dev.notes.model.realm.RealmController;
import com.dev.notes.presenter.AccountPresenterImpl;
import com.dev.notes.view.notes.NotesListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginRegisterActivity extends BaseActivity implements AccountView{

    private AccountPresenterImpl presenter;
    @BindView(R.id.username_edit)
    EditText usernameEdit;
    @BindView(R.id.password_edit)
    EditText passEdit;
    @BindView(R.id.confirm_password_edit)
    EditText confirmPassEdit;
    @BindView(R.id.confirm_password_edit_layout)
    TextInputLayout confirmPassEditLayout;
    @BindView(R.id.go_to_register_txt)
    TextView goToRegisterTxt;
    private boolean isRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        presenter = new AccountPresenterImpl(new RealmController());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void closeRealm() {
        presenter.closeRealm();
    }

    @Override
    public void showError(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.clearView();
    }

    @Override
    public void signIn() {
        startActivity(new Intent(this, NotesListActivity.class));
    }

    @OnClick(R.id.go_to_register_txt)
    public void goToRegister() {
        isRegisterPage = true;
        setRegistrationViews();
    }

    private void setRegistrationViews() {
        confirmPassEditLayout.setVisibility(View.VISIBLE);
        goToRegisterTxt.setVisibility(View.GONE);
    }

    @OnClick(R.id.login_btn)
    public void onSignInClick() {
        if (isRegisterPage) {
            presenter.registration(usernameEdit.getText().toString(), passEdit.getText().toString(), confirmPassEdit.getText().toString());
        } else {
            presenter.login(usernameEdit.getText().toString(), passEdit.getText().toString());
        }
    }
}
