package com.example.jaqb.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
