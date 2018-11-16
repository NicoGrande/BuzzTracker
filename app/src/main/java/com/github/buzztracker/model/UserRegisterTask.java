package com.github.buzztracker.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.github.buzztracker.R;
import com.github.buzztracker.controllers.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.buzztracker.model.Constants.NOT_STARTED;

class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

    private final User newUser;
    private final String userType;
    private final Context context;

    UserRegisterTask(User newUser, String userType, Context context) {
        this.newUser = newUser;
        this.userType = userType;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        FirebaseAuth staticAuth = FirebaseAuth.getInstance();
        // keeps track of registration progress
        final AtomicBoolean success = new AtomicBoolean(false);
        final AtomicBoolean finishedAttempt = new AtomicBoolean(false);

        try {
            staticAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = database.getReference()
                                        .child(Constants.FIREBASE_CHILD_USERS)
                                        .child(userType);
                                databaseReference.push().setValue(userType);
                                success.set(true);
                                finishedAttempt.set(true);
                            } else {
                                finishedAttempt.set(true);
                            }
                        }
                    });
            // waits 10 seconds to try to register with FirebaseAuth, fails if unable
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
        Model.setRegisterComplete(NOT_STARTED);
        ((RegistrationActivity) context).showProgress(false);
        if (success) {
            Toast toast = Toast.makeText(context,
                    R.string.account_creation_success, Toast.LENGTH_SHORT);
            toast.show();

        } else {
            Toast toast = Toast.makeText(context,
                    R.string.error_user_already_exists, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCancelled() {
        Model.setRegisterComplete(NOT_STARTED);
    }
}
