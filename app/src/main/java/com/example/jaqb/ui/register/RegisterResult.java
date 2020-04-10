package com.example.jaqb.ui.register;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {
    @Nullable
    private Integer error;

    /**
     * Returns error that might occur during registration
     * @param error holds the occurred error
     */
    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    /**
     * Gets the error code if happens
     * @return
     */
    @Nullable
    Integer getError() {
        return error;
    }
}
