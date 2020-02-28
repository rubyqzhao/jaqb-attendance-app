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

public class RegisterActivity extends AppCompatActivity implements java.util.Observer {

    private RegisterActivity registerActivity;
    private RegisterViewModel registerViewModel;
    private User newUser;
    private FireBaseDBServices dbServices;
    private ProgressBar loadingProgressBar;

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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

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
