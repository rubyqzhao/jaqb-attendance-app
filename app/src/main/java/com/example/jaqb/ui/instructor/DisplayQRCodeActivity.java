package com.example.jaqb.ui.instructor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;

/**
 * Activity to display the QR code generated for instructors.
 *
 * @author Amanjot Singh
 * @version 1.0
 */
public class DisplayQRCodeActivity extends AppCompatActivity {

    /**
     * Triggers when the user visits the QR page for the first time.
     * @param savedInstanceState    the previous state of the app
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);
    }
}
