package com.example.ricecropkeeptwo.weatherforecastmodel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NextDaysService {
    NextDaysService nextDaysService = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NextDaysService.class);

    @GET("data/2.5/forecast")
    Call<WeatherForecastResponse> NextDaysForecast(@Query("q") String q,
                                                   @Query("units") String units,
                                                   @Query("appid") String appid);
}
