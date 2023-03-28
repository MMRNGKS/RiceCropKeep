package com.example.ricecropkeeptwo.weathermodel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    WeatherService weatherService = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService.class);

    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> CurrentWeatherInformation(@Query("q") String q,
                                                           @Query("units") String units,
                                                           @Query("appid") String appid);
}
