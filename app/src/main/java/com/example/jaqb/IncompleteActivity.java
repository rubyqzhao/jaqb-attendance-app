package com.example.jaqb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity is used for the features that are not implemented in this application
 */
public class IncompleteActivity extends AppCompatActivity {

    TextView testTextView;

    /**
     * @param savedInstanceState saved application context passed into the activity
     *                           when it is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete);

        testTextView = findViewById(R.id.testTextView);
        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        testTextView.setText(str);
    }

}
