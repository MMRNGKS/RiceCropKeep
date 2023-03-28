package com.example.ricecropkeeptwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<DailyWeather> dailyWeatherList;
    public RecyclerViewAdapter(Context context, List<DailyWeather> dailyWeatherList) {
        this.context = context;
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // split time and date, set them to controls
        String[] splitter = dailyWeatherList.get(position).getDateTime().split(" ");
        String[] date = splitter[0].split("-");
        String[] time = splitter[1].split(":");
        String dayAndMonth = date[2] + "/" + date[1];
        String hourAndMinute = time[0] + ":" + time[1];

        holder.lblDate.setText(dayAndMonth);
        holder.lblTime.setText(hourAndMinute);
        Picasso.get().load(dailyWeatherList.get(position).getImageUrl()).into(holder.ivStatus);
        holder.lblTemperature.setText(String.format(context.getString(R.string.degree), String.valueOf(dailyWeatherList.get(position).getTemperature())));
        holder.lblMainWeather.setText(dailyWeatherList.get(position).getMainWeather());
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList == null ? 0 : dailyWeatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lblDate;
        private TextView lblTime;
        private ImageView ivStatus;
        private TextView lblTemperature;
        private TextView lblMainWeather;

        public ViewHolder(View itemView) {
            super(itemView);
            lblDate = itemView.findViewById(R.id.lbl_date);
            lblTime = itemView.findViewById(R.id.lbl_time);
            ivStatus = itemView.findViewById(R.id.iv_status_forecast);
            lblTemperature = itemView.findViewById(R.id.lbl_temperature_forecast);
            lblMainWeather = itemView.findViewById(R.id.lbl_main_weather_forecast);
        }
    }
}
