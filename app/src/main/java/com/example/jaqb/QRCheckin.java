package com.example.jaqb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author Bharat Goel
 *
 * This Activity scans the QR code and verify the time and location of the student to decide
 * whether to mark attendance or not
 */

public class QRCheckin extends AppCompatActivity implements LocationListener {

    private static final double ALLOWED_DISTANCE = 50;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textView;
    EditText editText;
    LocationManager locationManager;
    String currentQR;
    double Long;
    double Lat;
    double currentLongitude;
    double currentLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcheckin);

        Intent receiveIntent = this.getIntent();
        currentLongitude = receiveIntent.getDoubleExtra("courseLongitude", 0.0);
        currentLatitude = receiveIntent.getDoubleExtra("courseLatitude", 0.0);
        currentQR = receiveIntent.getStringExtra("courseQR");

        editText = findViewById(R.id.codeArea);

        surfaceView = findViewById(R.id.cameraView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        CameraSource.Builder builder = new CameraSource.Builder(this, barcodeDetector);
        builder.setRequestedPreviewSize(640, 480);
        builder.setAutoFocusEnabled(true);
        cameraSource = builder.build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    barcodeDetector.setProcessor(null);
                    checkValues(qrCodes.valueAt(0).displayValue);
                }
            }
        });
    }

    public void codeButtonOnClick(View view) throws ParseException {
        String textCode = editText.getText().toString();
        checkValues(textCode);
    }

    public void checkValues(String code) {

        /*Location Check starts*/
        checkLocation();
        boolean distOk = checkDist();
        /*Location Check ends*/

        /*Time Check starts
        boolean timeOk = checkTime();
        Time Check ends*/

        /*QR check* starts*/
        boolean codeOk = (currentQR.equals(code));
        /*QR check ends*/

        takeDecision(distOk, codeOk);
    }

    private void takeDecision(boolean distOk, boolean codeOk) {
        if (distOk && codeOk) {
            Toast.makeText(this, "CheckIn Successful", Toast.LENGTH_LONG).show();
            /*todo: Do other necessary updates*/
            finish();
        }
        if (distOk && !codeOk) {
            Toast.makeText(this, "Invalid QR code. Please try with updated QR code", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!distOk && codeOk) {
            Toast.makeText(this, "You are to far from the class. Please get in class and try again", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!distOk && !codeOk) {
            Toast.makeText(this, "Neither you are in class nor QR is valid", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /*
    private boolean checkTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "22-01-2015 10:20:56";
        Date date = sdf.parse(dateInString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long classTime = calendar.getTimeInMillis();

        calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        if(Math.abs(classTime - currentTime) == 1000*60*15)
            return true;
        return false;

    }*/

    private boolean checkDist() {
        final int R = 6371;

        double latDist = Math.toRadians(Lat - currentLatitude);
        double longDist = Math.toRadians(Long - currentLongitude);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2)
                + Math.cos(Math.toRadians(currentLatitude)) * Math.cos(Math.toRadians(Lat))
                * Math.sin(longDist / 2) * Math.sin(longDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = R * c * 1000;

        if (dist <= ALLOWED_DISTANCE) {
            return true;
        }
   
        return false;
    }


    public boolean checkLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        assert location != null;
        onLocationChanged(location);

        /* Get Location from database and compare */
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        Long = location.getLongitude();
        Lat = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
