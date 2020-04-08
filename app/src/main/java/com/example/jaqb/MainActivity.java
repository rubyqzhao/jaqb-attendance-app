package com.example.jaqb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.jaqb.ui.login.LoginActivity;
import com.example.jaqb.ui.register.RegisterActivity;

/**
 * This activity loads the first page of the application for login/registration page
 */

public class MainActivity extends AppCompatActivity {

    /**
     * @param savedInstanceState saved application context passed into the activity when it is
     *                           created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * @param view Event listener for the login button on home page
     */
    public void loginButtonOnClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * @param view Event listener for the register button on home page
     */
    public void registerButtonOnClick(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
