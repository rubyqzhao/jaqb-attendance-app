package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;

public class DisplayQRCodeActivity extends AppCompatActivity {

    private TextView qrCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);
        int code = getIntent().getIntExtra("code", 1234);
        qrCode = (TextView) findViewById(R.id.qrcode);
        qrCode.setText(String.valueOf(code));
    }
}
