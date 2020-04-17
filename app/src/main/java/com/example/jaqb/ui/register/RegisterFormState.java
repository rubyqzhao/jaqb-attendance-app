package com.example.jaqb.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    /**
     * Constructor to show error if username or password is invalid
     * @param usernameError if the username is invalid
     * @param passwordError if password is invalid
     */
    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    /**
     * Form state if user registers without entering username or password
     * @param isDataValid is true if the data is valid, else false
     */
    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    /**
     * Return error if username is invalid
     * @return username error
     */
    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    /**
     * Return error if password is invalid
     * @return password error
     */
    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    /**
     * Return error if entered data is invalid
     * @return invalid data error
     */
    boolean isDataValid() {
        return isDataValid;
    }
}
