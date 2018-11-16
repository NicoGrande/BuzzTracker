package com.github.buzztracker.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.buzztracker.R;
import com.github.buzztracker.model.Model;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Model model;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View progressView;
    private View loginFormView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == EditorInfo.IME_ACTION_DONE) || (id == EditorInfo.IME_NULL)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mEmailRegisterButton = findViewById(R.id.email_sign_in_button2);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        model = Model.getInstance();
        model.updateLocations(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                        startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                }
            }
        };
    }

    private void attemptLogin() {
        View focusView;
        focusView = model.getFirstIllegalLoginField(mEmailView, mPasswordView, this);
        if (focusView != null) {
            focusView.requestFocus();
        } else {
            Editable viewText = mEmailView.getText();
            String email = viewText.toString();

            viewText = mPasswordView.getText();
            String password = viewText.toString();
            showProgress(true);
            model.verifyLogin(email, password, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Toggles visibility of the login page with a loading progress spinner
     *
     * @param show whether you want the progress spinner displayed
     */
    public void showProgress(final boolean show) {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

