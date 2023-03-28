package com.example.ricecropkeeptwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    Intent data;
    EditText meditnotetitle, meditnotecontent;
    FloatingActionButton mupdate_btn;

    //FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.editnote_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        meditnotetitle = findViewById(R.id.editnotetitle);
        meditnotecontent = findViewById(R.id.editnotecontent);
        mupdate_btn = findViewById(R.id.update_btn);
        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mupdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newtitle = meditnotetitle.getText().toString();
                String newnotecontent = meditnotecontent.getText().toString();

                if(newtitle.isEmpty() || newnotecontent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Can't Updated Empty Fields", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newtitle);
                    note.put("notecontent", newnotecontent);

                    startActivity(new Intent(EditNoteActivity.this, RecordActivity.class));
                    finish();

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note Updated In Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Update Note In Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        String title = data.getStringExtra("title");
        String notecontent = data.getStringExtra("notecontent");
        meditnotetitle.setText(title);
        meditnotecontent.setText(notecontent);

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(EditNoteActivity.this, RecordActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}