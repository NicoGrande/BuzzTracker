package com.github.buzztracker.model;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.github.buzztracker.R;
import com.github.buzztracker.controllers.LoginActivity;
import com.github.buzztracker.controllers.MainScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.buzztracker.model.Constants.NOT_STARTED;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String email;
    private final String password;
    private final Context context;

    UserLoginTask(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final FirebaseAuth staticAuth = FirebaseAuth.getInstance();
        // keeps track of Firebase authentication success
        // is kinda weird but AtomicBoolean allows it to be modified from with lambda function
        final AtomicBoolean success = new AtomicBoolean(false);
        final AtomicBoolean finishedAttempt = new AtomicBoolean(false);
        try {
            staticAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                success.set(true);
                                finishedAttempt.set(true);
                            } else {
                                finishedAttempt.set(true);
                            }
                        }
                    });

            // waits 10 seconds to try to login with FirebaseAuth, fails if unable
            for (int i = 0; i < 10; i++) {
                if (finishedAttempt.get()) {
                    return success.get();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            return false;
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        Model.setLoginComplete(NOT_STARTED);
        ((LoginActivity) context).showProgress(false);
        if (success) {
            Toast toast = Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT);
            toast.show();
            LoginActivity.failedAttempts = 0;
            Intent myIntent = new Intent(context, MainScreenActivity.class);
            context.startActivity(myIntent);
        } else {
            LoginActivity.failedAttempts++;
            Toast toast = Toast.makeText(context,
                    R.string.error_incorrect_password, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCancelled() {
        Model.setLoginComplete(NOT_STARTED);
    }
}

