package com.example.ricecropkeeptwo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoneActivity extends AppCompatActivity {

    RecyclerView mrecyclerViewDone;
    ArrayList<SchedDone> doneArrayList;
    DoneAdapter doneAdapter;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_green));
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_green)));
        getSupportActionBar().setTitle("DONE SCHEDULES");

        mrecyclerViewDone = findViewById(R.id.recyclerViewDone);
        mrecyclerViewDone.setHasFixedSize(true);
        mrecyclerViewDone.setLayoutManager(new LinearLayoutManager(this));


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        doneArrayList = new ArrayList<SchedDone>();
        doneAdapter = new DoneAdapter(DoneActivity.this,doneArrayList);
        mrecyclerViewDone.setAdapter(doneAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myDoneSchedules").orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                doneArrayList.add(documentChange.getDocument().toObject(SchedDone.class));
                            }
                            doneAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(DoneActivity.this, ScheduleActivity.class));
    }
}