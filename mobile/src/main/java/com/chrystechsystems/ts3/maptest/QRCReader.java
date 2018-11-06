package com.chrystechsystems.ts3.maptest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/*
 *  Whole goal of QRCReader is to launch a qr code
 *  reader activity and return the value that was scanned
 *  to the activity that launched it.
 *
 */
public class QRCReader extends AppCompatActivity {


    Intent data;
    SurfaceView cameraPreview;
    Context context = this;
//    make barcode reader
    BarcodeDetector barcodeDetector;

    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    //formal permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                         return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcreader);

        //make preview surface
        cameraPreview = findViewById(R.id.cameraPreview);

        //make barcode
        barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        //set up camera source
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .build();

        //new event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(QRCReader.this,
                            new String[]{Manifest.permission.CAMERA} ,
                            RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
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

        //get barcode running
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            //Not messing with it
            @Override
            public void release() {

            }


            //On Barcode being scanned, put data into var, and return it to indoor tracking
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                data = new Intent();
                data.setData(Uri.parse(qrcodes.valueAt(0).displayValue));
                setResult(RESULT_OK, data);
                finish();

            }
        });

    }

}
