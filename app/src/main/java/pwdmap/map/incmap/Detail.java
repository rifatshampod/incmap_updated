package pwdmap.map.incmap;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import pwdmap.map.incmap.Adapter.PlacesImage;
import pwdmap.map.incmap.Api.RetrofitClient;
import pwdmap.map.incmap.Interface.Api;
import pwdmap.map.incmap.Models.AddFeedbackModel;
import pwdmap.map.incmap.Models.AddRatingModel;
import pwdmap.map.incmap.Models.Datum;
import pwdmap.map.incmap.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pwdmap.map.incmap.Utils.Constants.RESPONSE;
import static pwdmap.map.incmap.Utils.Constants.SUCCESSFUL_CODE;

public class Detail extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    Button rating, track, feedback;
    PlacesImage adapter;
    Button submit, btnSubmitRating;
    ImageView dialogButton;
    ProgressDialog progress;
    EditText etDescription_one, etDescription_two, etDescription_three;
    String loc_id, description_one, description_two, destination_three;
    RatingBar ratingBar;
    Datum datum;
    float rating_no;
    pwdmap.map.incmap.databinding.ActivityDetailBinding binding;

    ImageView back;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = pwdmap.map.incmap.databinding.ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        initDialog();
        rating = (Button) findViewById(R.id.btn_rate_review);
        track = (Button) findViewById(R.id.btn_track);
        feedback = (Button) findViewById(R.id.btn_feedback);
        back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        datum = (Datum) getIntent().getSerializableExtra("Datum");
        Log.d("recived data", "onCreate: " + new Gson().toJson(datum));

        setParams();
        Log.d("loc", "onCreate: " + new Gson().toJson(datum.getImage()));

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedBackDialog(Detail.this);
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Detail.this);

            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                startActivity(intent);

                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", datum.getLat(), datum.getLon());


                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent1);
            }
        });


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the RecyclerView
//        adapter.setClickListener(this);


    }

    private void setParams() {
        binding.time.setText(datum.getTiming());
        binding.description.setText(datum.getDescription());
        binding.services.setText(datum.getService());
        binding.title.setText(datum.getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        URI uri = URI.create(datum.getImage());


        Glide.with(this).load(uri).apply(options).into(binding.image);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(datum.getLat(), datum.getLon());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(new MarkerOptions().position(latLng).title(datum.getName()));
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rate_review_dialog);


        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.iv_cancek);
        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingbar);
        btnSubmitRating = (Button) dialog.findViewById(R.id.btn_rating_submit);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rating_no = ratingBar.getRating();
                        progress.show();
                        uploadRating(dialog);
                    }
                }, 1500);
            }
        });

        dialog.show();


    }

    public void showFeedBackDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback_dialog);
        dialogButton = (ImageView) dialog.findViewById(R.id.iv_cancek);
        etDescription_one = (EditText) dialog.findViewById(R.id.etDescription1);
        etDescription_two = (EditText) dialog.findViewById(R.id.etDescription2);
        etDescription_three = (EditText) dialog.findViewById(R.id.etDescription3);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submit = (Button) dialog.findViewById(R.id.btn_submit);

        clicks(dialog);

        dialog.show();


    }

    private void clicks(Dialog dialog) {


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (CheckAllFields()) {
                            progress.show();
                            uploadFeedback(dialog);
                        }
                    }
                }, 1500);
            }
        });

    }

    private void uploadRating(Dialog dialog) {
        Api apiService = RetrofitClient.getInstance().create(Api.class);
        Call<AddRatingModel> call = apiService.add_Rating(datum.getId(), String.valueOf(rating_no));

        Log.d(RESPONSE, "Request Body= " + new Gson().toJson(call.request().body()));

        call.enqueue(new Callback<AddRatingModel>() {
            @Override
            public void onResponse(Call<AddRatingModel> call, Response<AddRatingModel> response) {
                progress.dismiss();
                Log.d(RESPONSE, new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == SUCCESSFUL_CODE) {
                            dialog.dismiss();
                            Toast.makeText(context, "Thanks for rating...", Toast.LENGTH_LONG).show();
                            Log.d(RESPONSE, "onResponse: " + response.body());
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
            public void onFailure(Call<AddRatingModel> call, Throwable t) {

            }
        });

    }

    private void uploadFeedback(Dialog dialog) {


        Api apiService = RetrofitClient.getInstance().create(Api.class);
        Call<AddFeedbackModel> call = apiService.addFeedback(datum.getId(), description_one, description_two, destination_three);

        Log.d(RESPONSE, "Request Body= " + new Gson().toJson(call.request().body()));
        call.enqueue(new Callback<AddFeedbackModel>() {
            @Override
            public void onResponse(Call<AddFeedbackModel> call, Response<AddFeedbackModel> response) {

                progress.dismiss();
                Log.d(RESPONSE, new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == SUCCESSFUL_CODE) {
                            dialog.dismiss();
                            Log.d(RESPONSE, "onResponse: " + response.body());
                            Toast.makeText(context, "Your Feedback submitted...", Toast.LENGTH_LONG).show();
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
                        dialog.dismiss();
                        Toast.makeText(context, "Your Feedback have been Submitted", Toast.LENGTH_LONG).show();
                        Log.d(RESPONSE, "onError: " + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFeedbackModel> call, Throwable t) {

            }
        });
    }

    private boolean CheckAllFields() {

        if (etDescription_one.getText().toString().trim().isEmpty()) {
            etDescription_one.setError("Feedback is required");
            return false;
        }

        if (etDescription_two.getText().toString().trim().isEmpty()) {
            etDescription_two.setError("2nd Feedback is required");
            return false;
        }
        if (etDescription_three.getText().toString().trim().isEmpty()) {
            etDescription_three.setError("Description must be  written");
            return false;
        }

        description_one = etDescription_one.getText().toString();
        description_two = etDescription_two.getText().toString();
        destination_three = etDescription_three.getText().toString();

        // after all validation return true.
        return true;
    }

    private void initDialog() {
        progress = new ProgressDialog(context);
        progress.setMessage("Processing...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);

    }

}