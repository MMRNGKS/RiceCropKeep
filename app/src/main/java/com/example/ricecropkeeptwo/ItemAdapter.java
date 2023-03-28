package com.example.ricecropkeeptwo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataModel> arrayList;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public ItemAdapter(Context context, ArrayList<DataModel> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.row_item, null);
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView dateTextView = convertView.findViewById(R.id.dateTitle);
        TextView timeTextView = convertView.findViewById(R.id.timeTitle);
        final ImageView delImageView = convertView.findViewById(R.id.delete);
        final ImageView donImageView = convertView.findViewById(R.id.done);
        delImageView.setTag(position);
        donImageView.setTag(position);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final View finalConvertView = convertView;
        //On delete icon click remove item from list and database
        delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                Animation animSlideRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                animSlideRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Fires when animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // ...
                        //deleteItem(pos);
                        deleteItem(context, pos);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // ...
                    }
                });
                finalConvertView.startAnimation(animSlideRight);
            }
        });

        donImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int posi = (int) v.getTag();
                Animation animSlideRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
                animSlideRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Fires when animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // ...
                        doneItem(posi);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // ...
                    }
                });
                finalConvertView.startAnimation(animSlideRight);
            }
        });

        DataModel dataModel = arrayList.get(position);
        titleTextView.setText(dataModel.getTitle());
        dateTextView.setText(dataModel.getDate());
        timeTextView.setText(dataModel.getTime());
        return convertView;

    }

    //Remove item from list and database
    /*public void deleteItem(int position) {
        deleteItemFromDb(arrayList.get(position).getTitle(), arrayList.get(position).getDate(), arrayList.get(position).getTime());
        arrayList.remove(position);
        notifyDataSetChanged();
    }*/
    public void deleteItem(Context context,int position) {
        int alarmID = Integer.parseInt(arrayList.get(position).getAlarmID());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager!= null) {
            PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, alarmID, new Intent(context, MyNotificationPublisher.class), 0);
            alarmManager.cancel(mPendingIntent);
        }
        Log.d("ScheduleActivity", "AlarmID: " + alarmID);
        deleteItemFromDb(arrayList.get(position).getTitle(), arrayList.get(position).getDate(), arrayList.get(position).getTime());
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    //Delete item from database
    public void deleteItemFromDb(String name, String date,String time) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.deleteData(name, date, time);
            toastMsg("Deleted Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            toastMsg("Something went wrong");
        }
    }

    //Done item from list and database
    public void doneItem(int position) {
        doneItemFromDb(arrayList.get(position).getTitle(), arrayList.get(position).getDate(), arrayList.get(position).getTime());
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    //Done item from database
    public void doneItemFromDb(String name, String date,String time) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        try {
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myDoneSchedules").document();
            Map<String, Object> note = new HashMap<>();
            note.put("title", name);
            note.put("date", date);
            note.put("time", time);

            databaseHelper.deleteData(name, date, time);

            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    toastMsg("Mark Done Successfully!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMsg("Failed To Mark Done Schedule");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            toastMsg("Something went wrong");
        }
    }

    //Create and call toast messages when necessary
    public void toastMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
