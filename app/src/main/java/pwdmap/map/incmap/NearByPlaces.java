package pwdmap.map.incmap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pwdmap.map.incmap.Api.RetrofitClient;
import pwdmap.map.incmap.GpsTracker.GpsTracker;
import pwdmap.map.incmap.Interface.Api;
import pwdmap.map.incmap.Models.AddLocationModel;
import pwdmap.map.incmap.Models.Datum;
import pwdmap.map.incmap.Models.NearestLocationModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pwdmap.map.incmap.Utils.Constants.RESPONSE;
import static pwdmap.map.incmap.Utils.Constants.SUCCESSFUL_CODE;


public class NearByPlaces extends AppCompatActivity {

    SupportMapFragment mapFragment;
    ImageView add_location;
    String latitude, longitude;
    ProgressDialog progress;
    boolean current_location = false;
    ImageView image_loc;
    double current_lat;
    double current_lon;
    Datum datum;

    /*File Path*/
    String filePath;

    /*Fused Location*/
    FusedLocationProviderClient fusedLocationProviderClient;
    GpsTracker gpsTracker;
    Context context;
    double lati = 74.3211479;
    double lngi = 31.520199899;
    double lat;
    double lng;
    String name, location, description, service, timing;

    private Bitmap bitmap;
    List<Datum> data = new ArrayList<>();
    GoogleMap map;

    EditText et_Name, et_Location, et_Description, et_Service, et_timings;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        initDialog();

        statusCheck();

        back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_location = (ImageView) findViewById(R.id.iv_add_location);

        gpsTracker = new GpsTracker(context);
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        clicks();
        /*Map*/
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

    }

    private void clicks() {
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(NearByPlaces.this);
            }
        });

    }

    private void initDialog() {
        progress = new ProgressDialog(context);
        progress.setMessage("Processing...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);

    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(NearByPlaces.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //When Permission Granted then
            progress.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentLocation();

                }
            }, 1000);

        } else {
            //when permission denied

            ActivityCompat.requestPermissions(NearByPlaces.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();

                if (location != null) {

                    progress.dismiss();
                    try {
                        Toast.makeText(NearByPlaces.this, "Nearby PWD Places are retrieved", Toast.LENGTH_LONG).show();
                        //initialize geocoder
                        Geocoder geocoder = new Geocoder(NearByPlaces.this, Locale.getDefault());
                        //initialize address list
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        current_lat = addresses.get(0).getLatitude();
                        current_lon = addresses.get(0).getLongitude();
                        Log.d(RESPONSE, "onComplete: " + addresses.get(0).getLongitude());
                        Log.d(RESPONSE, "onComplete: " + addresses.get(0).getLatitude());
                        Log.d(RESPONSE, "onComplete: " + addresses.get(0).getCountryName());
                        Log.d(RESPONSE, "onComplete: " + addresses.get(0).getPostalCode());
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {

                                map = googleMap;
                                nearestLocations(lat, lng);

                                LatLng latLng = new LatLng(lat, lng);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("Gulberg 3"));


                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {
                                        String tittle = marker.getTitle();
                                        Intent i = new Intent(context, Detail.class);
                                        return false;
                                    }
                                });


                            }
                        });


                        current_location = true;
                        lat = addresses.get(0).getLatitude();
                        lng = addresses.get(0).getLongitude();

                    } catch (IOException e) {
                        e.printStackTrace();
//                        Toast.makeText()
                    }

                }
            }
        });
    }


    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_location_dialog);


        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.iv_cancek);
        image_loc = (ImageView) dialog.findViewById(R.id.imageLocation);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit);
        et_Name = (EditText) dialog.findViewById(R.id.et_name);
        et_Location = (EditText) dialog.findViewById(R.id.et_location);
        et_Description = (EditText) dialog.findViewById(R.id.description);
        et_Service = (EditText) dialog.findViewById(R.id.et_Service);
        et_timings = (EditText) dialog.findViewById(R.id.et_timings);
        AppCompatButton btnUploadImage = (AppCompatButton) dialog.findViewById(R.id.btnUPLOADiMAGE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //Upload Image
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with((Activity) context)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();

            }
        });

        /*Location*/
        et_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_Location.setText("Fetched Your Current Locaton");
                                progress.dismiss();

                            }
                        }, 3000);
                    }
                }, 3000);
            }
        });

        /*Submit*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (CheckAllFields()) {
                            progress.show();
                            uploadLocation();
                            dialog.dismiss();
                        }
                    }
                }, 1500);
            }
        });
        dialog.show();

    }

    private void uploadLocation() {
        MultipartBody.Part body1;
        if (!filePath.equalsIgnoreCase("")) {
            File file = new File(filePath);
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), file);
            body1 = MultipartBody.Part.createFormData("file", file.getName(), reqFile1);
            RequestBody m_name = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody current_latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(current_lat));
            RequestBody current_longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(current_lon));
            RequestBody m_description = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody m_service = RequestBody.create(MediaType.parse("text/plain"), service);
            RequestBody m_timming = RequestBody.create(MediaType.parse("text/plain"), timing);

            Api apiService = RetrofitClient.getInstance().create(Api.class);
            Call<AddLocationModel> call = apiService.add_location(m_name, current_latitude, current_longitude, m_description, m_service, m_timming, body1);

            Log.d(RESPONSE, "Request Body= " + new Gson().toJson(call.request().body()));
            call.enqueue(new Callback<AddLocationModel>() {
                @Override
                public void onResponse(Call<AddLocationModel> call, Response<AddLocationModel> response) {
                    progress.dismiss();
                    Log.d(RESPONSE, new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == SUCCESSFUL_CODE) {
                                Log.d(RESPONSE, "onResponse: " + response.body());
                                Toast.makeText(context, "Your Location have been posted...", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        try {
                            assert response.errorBody() != null;
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errorMsg = jObjError.getString("message");
                            Log.d(RESPONSE, "onError: " + new Gson().toJson(jObjError));

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(RESPONSE, "onError: " + e);
                        }
                    }
                }


                @Override
                public void onFailure(Call<AddLocationModel> call, Throwable t) {
                    Log.d(RESPONSE, "onFailure: " + t.toString());
                }
            });
        }


    }

    private boolean CheckAllFields() {

        if (et_Name.getText().toString().trim().isEmpty()) {
            et_Name.setError("Name is required");
            return false;
        }

        if (et_Location.getText().toString().trim().isEmpty()) {
            et_Location.setError("Location is required");
            return false;
        }
        if (et_Description.getText().toString().trim().isEmpty()) {
            et_Description.setError("Description must be  written");
            return false;
        }
        if (et_Service.getText().toString().trim().isEmpty()) {
            et_Service.setError("Services must be written");
            return false;
        }
        if (et_timings.getText().toString().trim().isEmpty()) {
            et_timings.setError("Timing must be written");
            return false;
        }

        name = et_Name.getText().toString();
        location = et_Location.getText().toString();
        description = et_Description.getText().toString();
        service = et_Service.getText().toString();
        timing = et_timings.getText().toString();

        // after all validation return true.
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {


            Uri fileUri = data.getData();
            assert data != null;
            filePath = Objects.requireNonNull(data.getData()).getPath();
            Glide.with(this).load(filePath).centerCrop().into(image_loc);

//Image Uri will not be null for RESULT_OK
// Uri fileUri = data.getData();
////You can also get File Path from intent
//            assert data != null;
//            filePath = Objects.requireNonNull(data.getData()).getPath();
//            Glide.with(this).load(filePath).centerCrop().into(add);

//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//        } else {
//            Log.d(TAG, "Task Cancelled: ");
        }

    }

    private void nearestLocations(double latitude, double longitude) {

//        lat = String.valueOf(latitude);
//        lng = String.valueOf(longitude);

        Api apiService = RetrofitClient.getInstance().create(Api.class);
        Call<NearestLocationModel> call = apiService.nearest_locations(latitude, longitude);
        Log.d(RESPONSE, "Request Body= " + new Gson().toJson(call.request().body()));

        call.enqueue(new Callback<NearestLocationModel>() {
            @Override
            public void onResponse(Call<NearestLocationModel> call, Response<NearestLocationModel> response) {
                Log.d(RESPONSE, new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getStatus() == SUCCESSFUL_CODE) {
                            data.addAll(response.body().getData());

                            for (Datum data : data) {
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(data.getLat(), data.getLon()))
                                        .title(data.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


                            }
                            String id = data.get(0).getId();
                            Log.d("id", "onResponse: " + id);
                            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    double selected_lat = marker.getPosition().latitude;
                                    double selected_lon = marker.getPosition().longitude;
                                    Log.d(RESPONSE, "onMarkerClick: " + selected_lat + "" + "" + selected_lon);
                                    for (int i = 0; i < data.size(); i++) {
                                        if (selected_lat == data.get(i).getLat() && selected_lon == data.get(i).getLon()) {
                                            datum = new Datum();
                                            datum = data.get(i);
                                            Log.d("DATA", "onMarkerClick: " + new Gson().toJson(datum));
                                            break;
                                        }
                                    }
                                    if (selected_lat != current_lat && selected_lat != current_lon) {
                                        Intent i = new Intent(context, Detail.class);
                                        i.putExtra("Datum", (Serializable) datum);
                                        startActivity(i);
                                        return false;
                                    }
                                    return true;
                                }
                            });
                        }

                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMsg = jObjError.getString("message");
                        Log.d(RESPONSE, "onError: " + new Gson().toJson(jObjError));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(RESPONSE, "onError: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<NearestLocationModel> call, Throwable t) {

                Log.d(RESPONSE, "onFailure: " + t.toString());
            }
        });
    }

    /*To check Location is on or off*/
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else {
            checkPermission();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                     /*   String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                        if(!provider.contains("gps")){ //if gps is disabled
                            final Intent poke = new Intent();
                            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                            poke.setData(Uri.parse("3"));
                            sendBroadcast(poke);*/

//                        checkPermission();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        statusCheck();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusCheck();
    }
}