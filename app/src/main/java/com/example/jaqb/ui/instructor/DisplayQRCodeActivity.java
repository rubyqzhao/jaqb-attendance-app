package com.example.jaqb.ui.instructor;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jaqb.R;
import com.example.jaqb.services.FireBaseDBServices;
import com.example.jaqb.services.QRCodeHelper;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author amanjotsingh
 *
 * This class is the activity to display the QR code and the 4 digit number code
 * to the instructor
 */
public class DisplayQRCodeActivity extends AppCompatActivity {

    private TextView qrCode;
    private FireBaseDBServices fireBaseDBServices;
    private String courseCode = "SER 515";
    private ImageView imageView;

    /**
     * @param savedInstanceState the saved application context passed into the activity
     *                           when it is created
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);
        int code = getIntent().getIntExtra("code", 1234);
        boolean isCodeValid = getIntent().getBooleanExtra("validCode", false);
        qrCode = (TextView) findViewById(R.id.qrcode);
        qrCode.setGravity(Gravity.CENTER);
        qrCode.setText(String.valueOf(code));
        imageView = (ImageView) findViewById(R.id.qrImage);
        if(!isCodeValid){
            fireBaseDBServices = FireBaseDBServices.getInstance();
            fireBaseDBServices.updateQRCode(code, courseCode);
        }
        Bitmap bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(String.valueOf(code))
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2).getQRCOde();
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
    }
}
