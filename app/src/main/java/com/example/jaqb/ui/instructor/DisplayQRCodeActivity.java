package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;

public class DisplayQRCodeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);
        int qrCode = getIntent().getIntExtra("QR_Code", 0);
        if(qrCode == 0){
            // QR code already exists, read from the storage and display
            Toast.makeText(getApplicationContext(), "Code already generated", Toast.LENGTH_LONG).show();
        }
        else if(qrCode == 1){
            // create QR code and display in the screen
            Toast.makeText(getApplicationContext(), "New code generated", Toast.LENGTH_LONG).show();
        }
    }
}
