package com.example.jaqb.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.jaqb.R;

/**
 * MOdel class to hold the Register view and check on its state
 */
public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();

    /**
     * Checks the state of login form
     * @return the state of login form
     */
    LiveData<RegisterFormState> getLoginFormState() {
        return loginFormState;
    }

    /**
     * Checks if the login is successful or not
     * @return result of login
     */
    LiveData<RegisterResult> getLoginResult() {
        return loginResult;
    }

    /**
     * Checks for any changes in login entry and do a validation
     * @param username the username entered
     * @param password the password entered
     */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
    }

    /**
     * Check if the username is a valid username
     * @param username the username entered
     * @return true if the username matches the criteria, else false
     */
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

    /**
     * Checks if password is valid
     * @param password the password entered
     * @return true if the password matches the criteria, else false
     */
    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
