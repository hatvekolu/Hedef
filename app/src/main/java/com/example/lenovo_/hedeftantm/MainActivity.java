package com.example.lenovo_.hedeftantm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo_.hedeftantm.Helper.FilePath;
import com.example.lenovo_.hedeftantm.Helper.HttpRequestImageLoadTask;
import com.example.lenovo_.hedeftantm.Helper.HttpRequestLongOperation;
import com.example.lenovo_.hedeftantm.Helper.ReadURL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    TextView konum;
    Button konumAl, resimCek,giris;
    ImageView image;
    double lati=0,longi=0,markerLati,markerLongi;
    String yer="";
    String websiteURL   = "http://modayakamoz.com/image_upload";
    String apiURL       = "http://modayakamoz.com/image_upload/api"; // Without ending slash
    String apiPassword  = "qw2e3erty6uiop";
    String currentImagePath = "";
    String currentImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        konum = (TextView) findViewById(R.id.textView);
        konumAl = (Button) findViewById(R.id.button2);
        resimCek = (Button) findViewById(R.id.button);
        giris = (Button) findViewById(R.id.button4);
        image = (ImageView) findViewById(R.id.imageView2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        new getMarkers().execute();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
        checkPermissionRead();
        checkPermissionWrite();

        resimCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

            }
        });
        konumAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                new getMarkers().execute();
            }
        });
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadImage();
            }
            });

    }
    private void checkPermissionRead(){
        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
    } // checkPermissionRead

    /*- Check permission Write ---------------------------------------------------------- */
// Pops up message to user for writing
    private void checkPermissionWrite(){
        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
    } // checkPermissionWrite


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Set image

            image.setImageURI(selectedImageUri);

            // Save image
            String destinationFilename = FilePath.getPath(this, selectedImageUri);

            // Dynamic text
            TextView textViewDynamicText = (TextView) findViewById(R.id.textViewDynamicText); // Dynamic text

            // URL
            String urlToApi = apiURL + "/image_upload.php";
            Map mapData = new HashMap();
            mapData.put("inp_api_password", apiPassword);
            mapData.put("cari", "Hasan");

            HttpRequestLongOperation task = new HttpRequestLongOperation(this, urlToApi, "post_image", mapData, destinationFilename, textViewDynamicText, new HttpRequestLongOperation.TaskListener() {
                @Override
                public void onFinished(String result) {
                    // Do Something after the task has finished
                    imageUploadResult();
                }
            });
            task.execute();

        }
    }
    public void imageUploadResult() {
        // Dynamic text
        TextView textViewDynamicText = (TextView)findViewById(R.id.textViewDynamicText);
        String dynamicText = textViewDynamicText.getText().toString();

        // Split
        int index = dynamicText.lastIndexOf('/');
        try {
            currentImagePath = dynamicText.substring(0, index);
        }
        catch (Exception e){
            Toast.makeText(this, "path: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            currentImage = dynamicText.substring(index,dynamicText.length());
        }
        catch (Exception e){
            Toast.makeText(this, "image: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // Load new image
        // Todo: loadImage();

    } // imageUploadResult
    /*- Load image ------------------------------------------------------------------ */
    public void loadImage(){

        // Load image


        if(!(currentImagePath.equals("")) && !(currentImage.equals(""))){

            String loadImage = websiteURL + "/" + currentImagePath + "/" + currentImage;
            new HttpRequestImageLoadTask(this, loadImage, image).execute();

        }

    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                lati = location.getLatitude();
                longi = location.getLongitude();

            } else {
                konum.setText("Bulunamadı");
                Toast.makeText(getApplicationContext(), "İnternet ve GPS'in açık olduğundan emin olun", Toast.LENGTH_SHORT).show();

            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permisions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permisions, grantResult);
        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati,longi),14));

        for (int i=1;i<4;i++){
            LatLng marker1=new LatLng(lati+(0.002*i*i),longi+(-0.002*i*i));
            googleMap.addMarker(new MarkerOptions().title("Hedef Tanıtım "+i).position(marker1));

        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                konum.setText("X:"+lati+" Y: "+longi);
                yer=marker.getTitle();
                markerLati=marker.getPosition().latitude;
                markerLongi=marker.getPosition().longitude;
                return false;
            }
        });

    }





    private class getMarkers extends AsyncTask<String, String, String> {
        BlankFragment progress;

        @Override
        protected void onPreExecute() {

            FragmentManager fm =getSupportFragmentManager();
            progress = new BlankFragment();
            progress.show(fm, "");
        }

        @Override
        protected String doInBackground(String... values)
        {

            ReadURL readURL=new ReadURL();
            try{
                String data="http://localhost/missdress/json/get_konum.php?latitude="+lati+"&longitude="+longi;


                return "0";
            }
            catch (Exception e){
                return "HATA";
            }

        }

        @Override
        protected void onPostExecute(String results)
        {
            if (!results.equals("HATA")){
                MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(MainActivity.this);
            }

            progress.dismiss();


        }
    }

}
