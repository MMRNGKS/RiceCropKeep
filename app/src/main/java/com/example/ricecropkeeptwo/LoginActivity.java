package com.example.ricecropkeeptwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText meditTextUsername, meditTextPassword;
    private Button mlogin_btn;
    private TextView mcreateAccountTextView;

    private FirebaseAuth firebaseAuth;

    ProgressBar mprogressBarLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();

        meditTextUsername = findViewById(R.id.editTextUsername);
        meditTextPassword = findViewById(R.id.editTextPassword);
        mlogin_btn = findViewById(R.id.login_btn);
        mcreateAccountTextView = findViewById(R.id.createAccountTextView);

        mprogressBarLogIn = findViewById(R.id.progressBarLogIn);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }


        mcreateAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, SignActivity.class));
            }
        });

        mlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = meditTextUsername.getText().toString().trim();
                String Password = meditTextPassword.getText().toString().trim();

                if(Username.isEmpty() || Password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    //register the user to firebase
                    //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    mprogressBarLogIn.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(Username + "@ricecropkeeptwo.com", Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                mprogressBarLogIn.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
        }
    }