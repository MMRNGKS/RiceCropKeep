package com.example.ricecropkeeptwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.myViewholder> {

    Context contexter;
    ArrayList<SchedDone> doneArrayList;

    public DoneAdapter(Context contexter, ArrayList<SchedDone> doneArrayList) {
        this.contexter = contexter;
        this.doneArrayList = doneArrayList;
    }

    @NonNull
    @Override
    public DoneAdapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(contexter).inflate(R.layout.item_done,parent,false);
        return new myViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneAdapter.myViewholder holder, int position) {

        SchedDone schedDone = doneArrayList.get(position);

        holder.schedtitle.setText(schedDone.title);
        holder.scheddate.setText(schedDone.date);
        holder.schedtime.setText(schedDone.time);

    }

    @Override
    public int getItemCount() {
        return doneArrayList.size();
    }

    public static class myViewholder extends RecyclerView.ViewHolder{

        TextView schedtitle, scheddate, schedtime;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            schedtitle = itemView.findViewById(R.id.sched_title);
            scheddate = itemView.findViewById(R.id.sched_date);
            schedtime = itemView.findViewById(R.id.sched_time);
        }
    }
}
