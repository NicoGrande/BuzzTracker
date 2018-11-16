package com.github.buzztracker.model;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.github.buzztracker.R;

class Login {

    View getFirstIllegalLoginField(AutoCompleteTextView emailView,
                                          EditText passwordView, Context context) {
        Editable viewText = emailView.getText();
        String email = viewText.toString();

        viewText = passwordView.getText();
        String password = viewText.toString();
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            Toast toast = Toast.makeText(context, R.string.error_field_required,
                    Toast.LENGTH_SHORT);
            toast.show();
            focusView = passwordView;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Toast toast = Toast.makeText(context, R.string.error_field_required,
                    Toast.LENGTH_SHORT);
            toast.show();
            focusView = emailView;
        } else if (!Verification.isPotentialEmail(email)) {
            Toast toast = Toast.makeText(context, R.string.error_invalid_email,
                    Toast.LENGTH_SHORT);
            toast.show();
            focusView = emailView;
        }
        return focusView;
    }

    void verifyLogin(String email, String password, Context context) {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        UserLoginTask userLoginTask = new UserLoginTask(email, password, context);
        userLoginTask.execute((Void) null);
    }
}
