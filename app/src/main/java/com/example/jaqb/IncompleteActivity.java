package com.example.jaqb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class IncompleteActivity extends AppCompatActivity {

    TextView textView = findViewById(R.id.testTextView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete);

        //Test Code
        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        textView.setText(str);
    }
}
