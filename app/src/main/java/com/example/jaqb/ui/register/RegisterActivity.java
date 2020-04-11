package com.example.jaqb.ui.register;

import android.app.Activity;

import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaqb.IncompleteActivity;
import com.example.jaqb.ui.login.LoginActivity;
import com.example.jaqb.ui.student.CheckInActivity;
import com.example.jaqb.IncompleteActivity;
import com.example.jaqb.R;
import com.example.jaqb.data.model.User;
import com.example.jaqb.services.FireBaseDBServices;

import java.util.Observable;

/**
 * A view Class for the User registration in the database
 */
public class RegisterActivity extends AppCompatActivity implements java.util.Observer {

    private RegisterActivity registerActivity;
    private RegisterViewModel registerViewModel;
    private User newUser;
    private FireBaseDBServices dbServices;
    private ProgressBar loadingProgressBar;


    /**
     * Triggers when the user first accesses the activity. Initializes values
     * and gets data from the firebase database.
     * @param savedInstanceState the previous state of app
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = new RegisterViewModel();
        registerActivity = this;

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText firstNameEditText = findViewById(R.id.firstName);
        final EditText lastNameEditText = findViewById(R.id.lastName);
        final Button registerButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        dbServices = FireBaseDBServices.getInstance();

        newUser = new User();
        registerViewModel.getLoginFormState().observe(this, new Observer<RegisterFormState>() {
            /**
             * Triggers when the state of form changes
             * @param registerFormState the previous state of form
             */
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
            }
        });

        registerViewModel.getLoginResult().observe(this, new Observer<RegisterResult>() {
            /**
             * Triggers when the result of login changes
             * @param registerResult the previous result of view
             */
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                //if (registerResult.getSuccess() != null) {
                //    updateUiWithUser(registerResult.getSuccess());
                //}
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
                Intent intent = new Intent(registerButton.getContext(), IncompleteActivity.class);
                startActivity(intent);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            /**
             * This method is called to notify you that, within s, the count characters beginning
             * at start are about to be replaced by new text with length after.
             * @param s the character sequence
             * @param start
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            /**
             * This method is called to notify you that, within s, the count characters
             * beginning at start have just replaced old text that had length before.
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            /**
             * This method is called to notify you that, somewhere within s,
             * the text has been changed.
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            /**
             * Called when an action is being performed.
             * @param v The view that was clicked
             * @param actionId Identifier of the action. This will be either the identifier you
             *                 supplied, or EditorInfo#IME_NULL if being called due to the enter
             *                 key being pressed.
             * @param event If triggered by an enter key,
             *              this is the event; otherwise, this is null.
             * @return boolean, Return true if you have consumed the action, else false.
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //registerViewModel.login(usernameEditText.getText().toString(),
                    //        passwordEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Triggers when the view Item is clicked and set values for User as Username, password
             * first name and last name
             * @param v the View
             */
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                newUser.setUserName(usernameEditText.getText().toString());
                newUser.setPassword(passwordEditText.getText().toString());
                newUser.setFirstName(firstNameEditText.getText().toString());
                newUser.setLastName(lastNameEditText.getText().toString());
                dbServices.registerUser(newUser, registerActivity);
            }
        });
    }

    /**
     * Private function that triggers if login fails
     * @param errorString
     */
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Update the user about the value entered
     * @param o Observation made
     * @param arg the argument on which observation is made
     */
    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println(arg);
        if(arg == null)
        {
            //Invalid credentials
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Email is already taken",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
