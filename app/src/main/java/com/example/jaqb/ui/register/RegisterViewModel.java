package com.example.jaqb.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.jaqb.R;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();

    LiveData<RegisterFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getLoginResult() {
        return loginResult;
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
