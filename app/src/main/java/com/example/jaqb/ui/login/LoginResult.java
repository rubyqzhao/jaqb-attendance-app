package com.example.jaqb.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 *
 * @author Joshua Drumm
 * @version 1.0
 */
class LoginResult {
    @Nullable
    private Integer error;

    /**
     * Constructor for the login result class
     * @param error login error
     */
    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    /**
     * Returns the login error if it occurs
     * @return  Integer of the error
     */
    @Nullable
    Integer getError() {
        return error;
    }
}
