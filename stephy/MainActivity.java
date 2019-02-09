package com.uottahacks.uottahacks2019;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.camerakit.CameraKitView;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CameraKitView cameraKitView;
    String thing = "hola";
    private Button txt;
    static List<EntityAnnotation> items;
    ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        txt = findViewById(R.id.text_size);
        photo = findViewById(R.id.img);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        //File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                        /*try {

                            /*FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }*/
                        Bitmap bmp = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, photo.getWidth(), photo.getHeight(), false));
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                // Convert photo to byte array
                                try{

                                    Vision.Builder visionBuilder = new Vision.Builder(
                                            new NetHttpTransport(),
                                            new AndroidJsonFactory(),
                                            null);

                                    visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyC_EGaLUZqyKwWRplW9kojnmDNjxbISDpk"));

                                    Vision vision = visionBuilder.build();

                                    //InputStream inputStream = getResources().openRawResource(R.raw.photo);
                                    //byte[] photoData = IOUtils.toByteArray(inputStream);
                                    //inputStream.close();
                                    com.google.api.services.vision.v1.model.Image inputImage = new com.google.api.services.vision.v1.model.Image();
                                    inputImage.encodeContent(capturedImage);

                                    Feature desiredFeature = new Feature();
                                    desiredFeature.setType("LABEL_DETECTION");

                                    AnnotateImageRequest request = new AnnotateImageRequest();
                                    request.setImage(inputImage);
                                    request.setFeatures(Arrays.asList(desiredFeature));

                                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();

                                    batchRequest.setRequests(Arrays.asList(request));
                                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                                    items = batchResponse.getResponses().get(0).getLabelAnnotations();
                                    //txt.setText(thing);
                                    Log.i(TAG, items.size()+"");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String temp = "";
                                            for(int i = 0; i < items.size(); i++){
                                                temp += items.get(i);
                                            }
                                            Log.i(TAG, temp);
                                            Toast.makeText(getApplicationContext(),
                                                    temp+"", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                                catch (java.io.IOException e){
                                    e.printStackTrace();

                                }
                            }
                        });
                    }
                });
            }
        });


        // Create new thread
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Convert photo to byte array
                try{

                    Vision.Builder visionBuilder = new Vision.Builder(
                            new NetHttpTransport(),
                            new AndroidJsonFactory(),
                            null);

                    visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyC_EGaLUZqyKwWRplW9kojnmDNjxbISDpk"));

                    Vision vision = visionBuilder.build();

                    InputStream inputStream = getResources().openRawResource(R.raw.photo);
                    byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                    com.google.api.services.vision.v1.model.Image inputImage = new com.google.api.services.vision.v1.model.Image();
                    inputImage.encodeContent(photoData);

                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("LABEL_DETECTION");

                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));

                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();

                    batchRequest.setRequests(Arrays.asList(request));
                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                    items = batchResponse.getResponses().get(0).getLabelAnnotations();
                    //txt.setText(thing);
                    Log.i(TAG, items.size()+"");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    items.get(0)+"", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                catch (java.io.IOException e){
                    e.printStackTrace();

                }
            }
        });*/


    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }
    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }
    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





}