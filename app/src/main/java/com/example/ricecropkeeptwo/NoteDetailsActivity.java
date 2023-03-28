package com.example.ricecropkeeptwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    private TextView mnotedetailstitle, mnotedetailscontent;
    FloatingActionButton medit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.notedetails_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mnotedetailstitle = findViewById(R.id.notedetailstitle);
        mnotedetailscontent = findViewById(R.id.notedetailscontent);
        medit_btn = findViewById(R.id.edit_btn);

        Intent data = getIntent();

        medit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("notecontent", data.getStringExtra("notecontent"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);
                Toast.makeText(getApplicationContext(), "Edit Mode", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mnotedetailstitle.setText(data.getStringExtra("title"));
        mnotedetailscontent.setText(data.getStringExtra("notecontent"));

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(NoteDetailsActivity.this, RecordActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}