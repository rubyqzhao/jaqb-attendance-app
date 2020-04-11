package com.example.jaqb.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.jaqb.R;

/**
 * Model class for the login view to display the login form and state.
 * Uses functions to determine whether login data is valid.
 *
 * @author Joshua Drumm
 * @version 1.0
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    /**
     * Returns the login form state data
     * @return  loginFormState
     */
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    /**
     * Returns the login result data
     * @return  loginResult
     */
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * Determines whether the username and password being entered is valid. Sets
     * an error message if the username is invalid or the password is too short.
     * @param username  user inputted text for login username
     * @param password  user inputted text for login password
     */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**
     * A placeholder username validation check.
     * @param username user inputted text for login username
     * @return boolean for valid username
     */
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
     * A placeholder password validation check.
     * @param password user inputted text for login password
     * @return boolean for valid password
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
