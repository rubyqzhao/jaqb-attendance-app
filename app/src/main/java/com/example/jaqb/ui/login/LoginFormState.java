package com.example.jaqb.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 *
 * @author Joshua Drumm
 * @version 1.0
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    /**
     * Constructor for the login form state if errors
     * @param usernameError errors for the username entry
     * @param passwordError errors for the password entry
     */
    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    /**
     * Constructor for the login form state
     * @param isDataValid   boolean for valid data
     */
    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    /**
     * Returns any errors for username if they occur.
     * @return  Integer for error
     */
    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    /**
     * Returns any errors for password if they occur.
     * @return  Integer for error
     */
    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    /**
     * Returns the current state of whether data is valid or not
     * @return  boolean isDataValid
     */
    boolean isDataValid() {
        return isDataValid;
    }
}
