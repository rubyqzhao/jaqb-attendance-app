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

public class QRCheckin extends AppCompatActivity implements LocationListener {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textView;
    Boolean value = false;
    EditText editText;
    LocationManager locationManager;
    String data;
    Integer codeFound = 0;
    double Long;
    double Lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcheckin);

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
                } catch(IOException e) {
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()   {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                
                if(qrCodes.size()!=0) {

                    Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    codeFound++;
                    runActivity(qrCodes, codeFound);
                }
            }
        });


    }

    public void codeButtonOnClick( View view) {
        //Not yet implemented
        if(checkValues() == true)
            value = true;
        Intent intent = new Intent(QRCheckin.this, IncompleteActivity.class);
        intent.putExtra("data", editText.getText().toString());
        startActivity(intent);
    }

    public boolean checkValues() {
        //Implement all the checks (location, class and time) from database here

        double val = Math.random()*2;
        if(val > 1) return true;
        return false;
    }

    public boolean checkLocation(){
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

    public void runActivity(SparseArray<Barcode> qrCodes, Integer codefound) {
        if(codefound == 1){
            Intent intent = new Intent(QRCheckin.this, IncompleteActivity.class);
            intent.putExtra("data", qrCodes.valueAt(0).displayValue);
            finish();
            startActivity(intent);
            codeFound = 0;
        }
    }
}
