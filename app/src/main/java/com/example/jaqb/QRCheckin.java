package com.example.jaqb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QRCheckin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcheckin);
    }

    public void qrButtonOnClick(View view) {
        Intent intent = new Intent(QRCheckin.this, IncompleteActivity.class);
        startActivity(intent);
    }

    public void codeButtonOnClick( View view) {
        Intent intent = new Intent(QRCheckin.this, IncompleteActivity.class);
        startActivity(intent);
    }
}
