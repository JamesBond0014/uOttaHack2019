package com.example.ibrahim.receyable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

//SnapChat Stuff
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.snapchat.kit.sdk.SnapCreative;
import com.snapchat.kit.sdk.creative.api.SnapCreativeKitApi;
import com.snapchat.kit.sdk.creative.exceptions.SnapMediaSizeException;
import com.snapchat.kit.sdk.creative.media.SnapMediaFactory;
import com.snapchat.kit.sdk.creative.media.SnapPhotoFile;
import com.snapchat.kit.sdk.creative.models.SnapContent;
import com.snapchat.kit.sdk.creative.models.SnapLiveCameraContent;

//Stephs stuff
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
import android.widget.LinearLayout;
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
import com.snapchat.kit.sdk.creative.models.SnapPhotoContent;








import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.EasyPermissions;



//GELOCATION API

//https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyCtRKhTcOZ_c5pigRpmrPgMFwcjEz780uQ

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private String API= "https://maps.googleapis.com/maps/api/geocode/json?";
    private CameraKitView cameraKitView;
    String thing = "hola";
    private ImageView recyleBtn;
    private ImageView pingBtn;
    private ImageView closeBtn,closeBtn2;
    private ImageView snapBtn;
    private double lat,lng;
    StringRequest stringRequest;
    RequestQueue queue;
    String addressOfDirt;
    static List<EntityAnnotation> items;
    ImageView photo;

    GoogleApiClient mGoogleApiClient;

    boolean checkIfSnapCode;


    private TextView descrptionTitle;
    private LinearLayout itemRecyle;

    private Button sendBtn;
    private  File pathToImg;


//    ABDO's STUFF

    SnapCreativeKitApi snapCreativeKitApi;
    boolean photoPicker;

    private static final int WRITE_STORAGE_PERM = 123;



//    STEPHS STUFF


    String results;
    String temp = "";
    String tag_url = "https://c6c885db.ngrok.io/"; //INTERCHANGABLE.





    private int LOCATION_PERM = 1;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            @SuppressLint("MissingPermission") Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            Log.i("TEST:", location.getLongitude()+"");
            Log.i("DONKEY:", location.getLatitude()+"");
            lng = location.getLongitude();
            lat = location.getLatitude();
        } else {
            //does not have permissions
            EasyPermissions.requestPermissions(this, "We want location", LOCATION_PERM, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }




        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        recyleBtn = findViewById(R.id.recyleBtn);
        closeBtn = findViewById(R.id.closeBtn);
        closeBtn2 = findViewById(R.id.closeBtn2);
        pingBtn = findViewById(R.id.pingBtn);
        photo = findViewById(R.id.imgView);
        addressOfDirt  = "";
        snapBtn = findViewById(R.id.snapBtn);

        sendBtn = findViewById(R.id.sendBtn);
            itemRecyle = findViewById(R.id.infoBoxHr);

        descrptionTitle = findViewById(R.id.description);
        //latlng=40.714224,-73.961452&key=AIzaSyCtRKhTcOZ_c5pigRpmrPgMFwcjEz780uQ\n"



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();


        queue = Volley.newRequestQueue(this);








        //Event Listeners on Receyle Btn
        recyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(final CameraKitView cameraKitView, final byte[] capturedImage) {
//                        File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
//                        pathToImg = savedPhoto;
//                        try {
//                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
//                            outputStream.write(capturedImage);
//                            outputStream.close();
//                        } catch (java.io.IOException e) {
//                            e.printStackTrace();
//                        }



                        Bitmap bmp = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, photo.getWidth(), photo.getHeight(), false));
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                // Convert photo to byte array
                                try {

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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            temp = "";
                                            for(int i = 0; i < items.size(); i++){
                                                temp += items.get(i).getDescription() + ",";
                                            }
                                            Log.i(TAG, temp);
                                            Toast.makeText(getApplicationContext(),
                                                    temp+"", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    String url = "";
                                    if(temp.length() != 0) {
                                        url = tag_url + temp.substring(0, temp.length() - 1);
                                    }
                                    // Request a string response from the provided URL.
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if(!response.isEmpty()){
                                                        results = response;
                                                        Log.i(TAG, results);
                                                        Log.i("YaDonkey",results+"");
                                                        String title = "";
                                                        String descrption = "";
                                                        ArrayList<String> titles = new ArrayList<>();
                                                        ArrayList<String> descriptions = new ArrayList<>();
                                                        try{
                                                            Log.i("YADONKEY11", response);
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            Log.i("YADONKEY1", jsonObject.toString());
                                                            for(int i = 0; i < jsonObject.getJSONArray("stuff").length(); i++){
                                                                //JSONObject temp = new JSONObject(jsonObject.getJSONArray("stuff").get(i).toString().substring(0, jsonObject.getJSONArray("stuff").length()-1));
                                                                JSONObject obj = new JSONObject(jsonObject.getJSONArray("stuff").get(i).toString());
                                                                descriptions.add(obj.get("description").toString());
                                                                titles.add(obj.get("title").toString());
                                                                //list.add(jsonObject.getJSONArray("stuff").get(i).toString());
                                                                Log.i("YADONKEY", titles.get(i));
                                                                title += titles.get(i) + "\n";
                                                                descrption += descriptions.get(i) + "\n";
                                                                Log.i("YADONKEY", descriptions.get(i));

                                                            }


                                                            descrptionTitle.setText(descrption);
                                                            itemRecyle.setVisibility(View.VISIBLE);
                                                        }
                                                        catch(Throwable t){
                                                            System.out.println("Error: " + t);
                                                        }




                                                        cameraKitView.setVisibility(View.INVISIBLE);
                                                        itemRecyle.setVisibility(View.VISIBLE);
                                                        descrptionTitle.setText(descrption);
                                                        closeBtn.setVisibility(View.VISIBLE);
                                                        pingBtn.setEnabled(false);
                                                        snapBtn.setEnabled(false);
                                                        recyleBtn.setEnabled(false);
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            results = "Sorry! Nothing found.";
                                            Log.i(TAG, results);
                                        }
                                    });






                                    // Add the request to the RequestQueue.
                                    queue.add(stringRequest);
                                    //Log.i(TAG, results);

                                } catch (java.io.IOException e) {
                                    e.printStackTrace();

                                }
                            }
                        });
                    }
                });
            }
        });

        pingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +lat+ ","+lng+"&key=AIzaSyCtRKhTcOZ_c5pigRpmrPgMFwcjEz780uQ";



                // Request a string response from the provided URL.
                stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.i("YaDonkey", response);
                                JSONObject jObject = null;
                                try {
                                    jObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JSONArray jArray = null;
                                try {
                                    jArray = jObject.getJSONArray("results");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                for (int i=0; i < jArray.length(); i++)
                                {
                                    try {
                                        JSONObject oneObject = jArray.getJSONObject(i);
                                        // Pulling items from the array
                                        addressOfDirt = oneObject.getString("formatted_address");
                                        Log.i("myFriend", addressOfDirt+"");
                                        break;

                                    } catch (JSONException e) {
                                        // Oops
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERROR:", error +"");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        Bitmap bmp = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, photo.getWidth(), photo.getHeight(), false));

                        File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                        pathToImg = savedPhoto;
                        Log.i("IMAGEE?", pathToImg+"");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }

                        //Hide Camera
                        cameraKitView.setVisibility(View.INVISIBLE);
                        pingBtn.setEnabled(false);
                        closeBtn2.setVisibility(View.VISIBLE);
                        sendBtn.setVisibility(View.VISIBLE);
                        recyleBtn.setEnabled(false);
                        snapBtn.setEnabled(false);

                    }
                });


            }
        });


            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    cameraKitView.setVisibility(View.VISIBLE);
                    pingBtn.setEnabled(true);
                    recyleBtn.setEnabled(true);
                    snapBtn.setEnabled(true);
                    closeBtn2.setVisibility(View.INVISIBLE);

                    String emailAddress = "311@ottawa.ca";
                    String emailURI = "mailto:" + emailAddress;
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(emailURI));

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Please pickup recyables at " + addressOfDirt);

                    String emailBody = "\n Dear Staff/Admin,\n " +
                            "\n Please pick up the recyclables at the following location:\n"
                            +"\n " + addressOfDirt;
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pathToImg));//pngFile
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                    startActivity(Intent.createChooser(emailIntent, "Email Client ..."));


                    sendBtn.setVisibility(View.INVISIBLE);


                }
            });







        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PRESSED CLOSE BTN", "DONKEY");
                cameraKitView.setVisibility(View.VISIBLE);
                recyleBtn.setEnabled(true);
                pingBtn.setEnabled(true);
                snapBtn.setEnabled(true);
                closeBtn.setVisibility(View.INVISIBLE);
                itemRecyle.setVisibility(View.INVISIBLE);

            }
        });


        //This is for Pinging location, the "X" icon button to close.
        closeBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("PRESSED CLOSE BTN", "DONKEY");
                    cameraKitView.setVisibility(View.VISIBLE);
                    pingBtn.setEnabled(true);
                    recyleBtn.setEnabled(true);
                    snapBtn.setEnabled(true);
                    closeBtn2.setVisibility(View.INVISIBLE);
                    sendBtn.setVisibility(View.INVISIBLE);

                }
            });


        //FEATURE 3 , ABDO

            //Btns
            snapBtn = findViewById(R.id.snapBtn);
            EasyImage.configuration(this)
                    .setAllowMultiplePickInGallery(false);

            snapCreativeKitApi = SnapCreative.getApi(this);


            //Event Listeners
            snapBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    snapBtn.setEnabled(false);
                    recyleBtn.setEnabled(false);
                    pingBtn.setEnabled(false);
                    Log.d("recycle clicked", "hi");
                    startPhotoSend();
                    snapBtn.setEnabled(true);
                    recyleBtn.setEnabled(true);
                    pingBtn.setEnabled(true);


                }

            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(photoPicker) {
            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    photoPicker = false;
                    //Some error handling
                }

                @Override
                public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                    //Handle the images
                    photoPicker = false;
                    onPhotosReturned(imagesFiles);
                }
            });
        }
    }


    private void onPhotosReturned(List<File> imageFiles) {
        Log.d("photo Returned", "Photo returned");
        SnapMediaFactory snapMediaFactory = SnapCreative.getMediaFactory(this);
        if(imageFiles.size() == 1) {
            SnapPhotoFile photoFile;
            try {
                photoFile = snapMediaFactory.getSnapPhotoFromFile(imageFiles.get(0));

            } catch (SnapMediaSizeException e) {
                e.printStackTrace();
                return;
            }
            SnapPhotoContent snapPhotoContent = new SnapPhotoContent(photoFile);
            snapPhotoContent.setCaptionText("I just Recycled!");
            snapPhotoContent.setAttachmentUrl("www.google.com");

            snapCreativeKitApi.send(snapPhotoContent);
            Log.d("Snapchat", "Photo sent to snapchat");
        }
    }

    private void startPhotoSend() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this, perms)) {
            //has permissions
            EasyImage.openGallery(MainActivity.this, 0);
            photoPicker = true;
        } else {


            checkIfSnapCode = true;

            //does not have permissions
            Log.d("no perms", "permission denied");
            EasyPermissions.requestPermissions(this, getString(R.string.app_permission_text), WRITE_STORAGE_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
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



            if(requestCode == 123){
                EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

            }
            else{
                cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);

            }





    }



}




