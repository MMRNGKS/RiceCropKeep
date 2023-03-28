package com.example.ricecropkeeptwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.ricecropkeeptwo.weatherforecastmodel.NextDaysService;
import com.example.ricecropkeeptwo.weatherforecastmodel.WeatherForecastResponse;
import com.example.ricecropkeeptwo.weathermodel.CurrentWeatherResponse;
import com.example.ricecropkeeptwo.weathermodel.WeatherService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Button mrecord_btn, msched_btn;
    private TextView mtextUser, mtextCity, mtextForecast;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private Animation animFadeIn;

    private ImageView ivStatus;
    private TextView lblTemperature;
    private TextView lblStatus;
    private RecyclerView rvNextDays;

    private WeatherForecastResponse forecast;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_green));
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_green)));
        getSupportActionBar().setTitle("HOME");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mtextUser = findViewById(R.id.textUser);
        mtextCity = findViewById(R.id.textCity);
        mtextForecast = findViewById(R.id.textForecast);

        String infoId = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(firebaseUser.getUid()).getId();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(infoId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    mtextUser.setText(documentSnapshot.getString("fullname"));
                    mtextCity.setText(documentSnapshot.getString("address"));
                    mtextForecast.setText(documentSnapshot.getString("address") + ", 5 Days / 3 Hour Forecast");
                }else{
                    Toast.makeText(getApplicationContext(), "User Info Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

        Map();                          // map between graphical controls and programmatic ones
        FetchCurrentWeatherFromApi();   // pull current weather from api and fill it in controls
        FetchWeatherForecastFromApi();  // pull weather forecast from api and fill it in controls

        mrecord_btn = findViewById(R.id.record_btn);
        msched_btn = findViewById(R.id.sched_btn);

        msched_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ScheduleActivity.class));
            }
        });

        mrecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RecordActivity.class));
            }
        });

    }

    private void Map() {    // map between graphical controls and programmatic ones
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        ivStatus = findViewById(R.id.iv_status);
        lblTemperature = findViewById(R.id.lbl_temperature);
        lblStatus = findViewById(R.id.lbl_status);

        rvNextDays = findViewById(R.id.rv_next_days);
    }

    private void FetchCurrentWeatherFromApi() { // get current weather from api

        String infoId = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(firebaseUser.getUid()).getId();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(infoId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    city = documentSnapshot.getString("address");

                    WeatherService.weatherService.CurrentWeatherInformation(city, "metric", "1795fb94835e76ba8fff6703771b0d66")
                            .enqueue(new Callback<CurrentWeatherResponse>() {
                                @Override
                                public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                                    CurrentWeatherResponse current = response.body();
                                    if (current != null) {
                                        UpdateStatus(current);
                                    }
                                }
                                @Override
                                public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                                    Toast toast = Toast.makeText(HomeActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT);
                                    toast.show();   // need fix this field later
                                }
                            });
                } else{
                    Toast.makeText(getApplicationContext(), "City Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void FetchWeatherForecastFromApi() {    // get weather forecast from api

        String infoId = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(firebaseUser.getUid()).getId();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(infoId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    city = documentSnapshot.getString("address");

                    NextDaysService.nextDaysService.NextDaysForecast(city, "metric", "1795fb94835e76ba8fff6703771b0d66")
                            .enqueue(new Callback<WeatherForecastResponse>() {
                                @Override
                                public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                                    forecast = response.body();
                                    if (forecast != null) {
                                        UpdateForecast(forecast);

                                    }
                                }
                                @Override
                                public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                                    Toast toast = Toast.makeText(HomeActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT);
                                    toast.show();   // need fix this field later
                                }
                            });
                } else{
                    Toast.makeText(getApplicationContext(), "City Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void UpdateStatus(CurrentWeatherResponse current) { // fill current weather data to controls
        Picasso.get().load("http://openweathermap.org/img/wn/" + current.getWeather().get(0).getIcon() + "@2x.png").into(ivStatus);
        ivStatus.startAnimation(animFadeIn);
        lblTemperature.setText(String.format(getString(R.string.degree), current.getMain().getTemp().toString()));
        lblTemperature.startAnimation(animFadeIn);
        lblStatus.setText(current.getWeather().get(0).getMain() + " (" +current.getWeather().get(0).getDescription() + ")");
        lblStatus.startAnimation(animFadeIn);

    }

    private void UpdateForecast(WeatherForecastResponse forecast) { // fill data from data adapter to recycler view forecast
        List<DailyWeather> dailyWeatherList = new Vector<>();
        for (int i = 0; i < forecast.getCnt(); i++) {
            DailyWeather current = new DailyWeather(forecast.getList().get(i).getDtTxt(),
                    "http://openweathermap.org/img/wn/" + forecast.getList().get(i).getWeather().get(0).getIcon() + ".png",
                    forecast.getList().get(i).getMain().getTemp(),
                    forecast.getList().get(i).getWeather().get(0).getMain());   // get necessary data and fill into class Daily Weather (handmade model)
            dailyWeatherList.add(current);  // add recent created instance to their list
        }
        // create a new layout manager for recycler view, HORIZONTAL for more suitable
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvNextDays.setLayoutManager(layoutManager); // set that layout to recycler view
        rvNextDays.setHasFixedSize(true);
        rvNextDays.setAdapter(new RecyclerViewAdapter(this, dailyWeatherList)); // officially fill data
        rvNextDays.startAnimation(animFadeIn);  // animation

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}