package com.example.jaqb.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.jaqb.IncompleteActivity;
import com.example.jaqb.MainActivity;
import com.example.jaqb.CheckInActivity;

import com.example.jaqb.R;
import com.example.jaqb.data.model.UserLevel;
import com.example.jaqb.services.FireBaseDBServices;

import java.util.Observable;
import com.example.jaqb.ui.student.CheckInActivity;

public class LoginActivity extends AppCompatActivity implements java.util.Observer {

    private LoginActivity loginActivity;
    private LoginViewModel loginViewModel;
    private FireBaseDBServices dbServices;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        loginActivity = this;

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        dbServices = FireBaseDBServices.getInstance();

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                //if (loginResult.getSuccess() != null) {
                //    updateUiWithUser(loginResult.getSuccess());
                //}
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
                Intent intent = new Intent(loginButton.getContext(), CheckInActivity.class);
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //loginViewModel.login(usernameEditText.getText().toString(),
                    //        passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.VISIBLE);
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                dbServices = FireBaseDBServices.getInstance();
                dbServices.loginUser(email, password, loginActivity);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        dbServices.seeIfStillLoggedIn(getApplicationContext());
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == null)
        {
            //Invalid credentials
            loadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Invalid Email or Password",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = null;
            switch((UserLevel) (arg))
            {
                case INSTRUCTOR:
                    intent = new Intent(getApplicationContext(), IncompleteActivity.class);
                    break;
                case ADMIN:
                    intent = new Intent(getApplicationContext(), IncompleteActivity.class);
                    break;
                default:
                    intent = new Intent(getApplicationContext(), CheckInActivity.class);
            }
            startActivity(intent);
        }
    }
}
