package com.example.ricecropkeeptwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignActivity extends AppCompatActivity {

    private EditText meditTextName, meditTextAge, meditTextSignUsername, meditTextSignPassword, meditTextSignPassword2;
    private Spinner mspinnerGender, mspinnerAddress;
    private Button msign_btn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private String gender;
    private String address;

    private static final String TAG = "Sign";

    ProgressBar mprogressBarSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        //getSupportActionBar().hide();
        meditTextName = findViewById(R.id.editTextName);
        meditTextAge = findViewById(R.id.editTextAge);

        meditTextSignUsername = findViewById(R.id.editTextSignUsername);
        meditTextSignPassword = findViewById(R.id.editTextSignPassword);
        meditTextSignPassword2 = findViewById(R.id.editTextSignPassword2);
        msign_btn = findViewById(R.id.sign_btn);

        mprogressBarSignIn = findViewById(R.id.progressBarSignIn);

        mspinnerGender = findViewById(R.id.spinnerGender);
        mspinnerAddress = findViewById(R.id.spinnerAddress);

        Spinners();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        msign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = meditTextName.getText().toString().trim();
                String age = meditTextAge.getText().toString().trim();

                String signUsername = meditTextSignUsername.getText().toString().trim();
                String signPassword = meditTextSignPassword.getText().toString().trim();
                String signPassword2 = meditTextSignPassword2.getText().toString().trim();

                if(signUsername.isEmpty() || signPassword.isEmpty() || fullName.isEmpty() || age.isEmpty() || address.isEmpty() || gender.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
                }
                else if(signUsername.length()<6){
                    Toast.makeText(getApplicationContext(), "Username Should Be Greater than 6 Characters", Toast.LENGTH_SHORT).show();
                }
                else if(signPassword.length()<8){
                    Toast.makeText(getApplicationContext(), "Password Should Be Greater than 7 Digits", Toast.LENGTH_SHORT).show();
                }
                else if(signPassword.equals(signPassword2) == false){
                    Toast.makeText(getApplicationContext(), "Password & Confirm Password not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    //register the user to firebase
                    mprogressBarSignIn.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Eyy");

                    firebaseAuth.fetchSignInMethodsForEmail(signUsername + "@ricecropkeeptwo.com")
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean check = !task.getResult().getSignInMethods().isEmpty();

                                    if(!check){
                                        firebaseAuth.createUserWithEmailAndPassword(signUsername + "@ricecropkeeptwo.com", signPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("myInfo").document(firebaseUser.getUid());
                                                Map<String, Object> info = new HashMap<>();
                                                info.put("fullname", fullName);
                                                info.put("age", age);
                                                info.put("gender", gender);
                                                info.put("address", address);
                                                info.put("password", signPassword);

                                                documentReference.set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "You Are Now Registered", Toast.LENGTH_SHORT).show();
                                                        firebaseAuth.signOut();
                                                        finish();
                                                        startActivity(new Intent(SignActivity.this, LoginActivity.class));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Registration Failed. Please turn on Wifi or Mobile Data", Toast.LENGTH_SHORT).show();
                                                        mprogressBarSignIn.setVisibility(View.INVISIBLE);
                                                    }
                                                });

                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Username Already Existed", Toast.LENGTH_SHORT).show();
                                        mprogressBarSignIn.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });
                }
            }
        });
        }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(SignActivity.this, LoginActivity.class));
    }

    private void Spinners() {
        List<String> list = new ArrayList<>();
        list.add(0,"Select Gender");
        list.add("Male");
        list.add("Female");
        list.add("Others");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerGender.setAdapter(adapter);
        mspinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Gender")){
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                    gender = "";
                }else{
                    gender = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + gender, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-generated method stub
            }
        });

        List<String> listAdd = new ArrayList<>();
        listAdd.add(0,"Select Address(City)");
        listAdd.add("Alaminos");
        listAdd.add("Angeles City");
        listAdd.add("Antipolo");
        listAdd.add("Bacolod");
        listAdd.add("Bacoor");
        listAdd.add("Bago");
        listAdd.add("Bais");
        listAdd.add("Balanga");
        listAdd.add("Batac");
        listAdd.add("Batangas City");
        listAdd.add("Bayawan");
        listAdd.add("Baybay");
        listAdd.add("Bayugan");
        listAdd.add("Biñan");
        listAdd.add("Bislig");
        listAdd.add("Bogo");
        listAdd.add("Borongan");
        listAdd.add("Butuan");
        listAdd.add("Cabadbaran");
        listAdd.add("Cabanatuan");
        listAdd.add("Cabuyao");
        listAdd.add("Cadiz");
        listAdd.add("Cagayan de Oro");
        listAdd.add("Calaca");
        listAdd.add("Calamba");
        listAdd.add("Calapan");
        listAdd.add("Calbayog");
        listAdd.add("Caloocan");
        listAdd.add("Candon");
        listAdd.add("Canlaon");
        listAdd.add("Carcar");
        listAdd.add("Catbalogan");
        listAdd.add("Cauayan");
        listAdd.add("Cavite City");
        listAdd.add("Cebu");
        listAdd.add("Cotabato City");
        listAdd.add("Dagupan");
        listAdd.add("Danao");
        listAdd.add("Dapitan");
        listAdd.add("Dasmariñas");
        listAdd.add("Davao City");
        listAdd.add("Digos");
        listAdd.add("Dipolog");
        listAdd.add("Dumaguete");
        listAdd.add("El Salvador");
        listAdd.add("Escalante");
        listAdd.add("Gapan");
        listAdd.add("General Santos");
        listAdd.add("General Trias");
        listAdd.add("Gingoog");
        listAdd.add("Guihulngan");
        listAdd.add("Himamaylan");
        listAdd.add("Ilagan");
        listAdd.add("Iligan");
        listAdd.add("Iloilo City");
        listAdd.add("Imus");
        listAdd.add("Iriga");
        listAdd.add("Isabela");
        listAdd.add("Kabankalan");
        listAdd.add("Kidapawan");
        listAdd.add("Koronadal");
        listAdd.add("La Carlota");
        listAdd.add("Lamitan");
        listAdd.add("Laoag");
        listAdd.add("Lapu-Lapu City");
        listAdd.add("Las Piñas");
        listAdd.add("Legazpi");
        listAdd.add("Ligao");
        listAdd.add("Lipa");
        listAdd.add("Lucena");
        listAdd.add("Maasin");
        listAdd.add("Mabalacat");
        listAdd.add("Makati");
        listAdd.add("Malabon");
        listAdd.add("Malaybalay");
        listAdd.add("Malolos");
        listAdd.add("Mandaluyong");
        listAdd.add("Mandaue");
        listAdd.add("Manila");
        listAdd.add("Marawi");
        listAdd.add("Marikina");
        listAdd.add("Masbate City");
        listAdd.add("Mati");
        listAdd.add("Meycauayan");
        listAdd.add("Meycauayan");
        listAdd.add("Muñoz");
        listAdd.add("Muntinlupa");
        listAdd.add("Naga");
        listAdd.add("Naga");
        listAdd.add("Navotas");
        listAdd.add("Olongapo");
        listAdd.add("Ormoc");
        listAdd.add("Oroquieta");
        listAdd.add("Ozamiz");
        listAdd.add("Pagadian");
        listAdd.add("Palayan");
        listAdd.add("Panabo");
        listAdd.add("Parañaque");
        listAdd.add("Pasay");
        listAdd.add("Pasig");
        listAdd.add("Passi");
        listAdd.add("Puerto Princesa");
        listAdd.add("Quezon City");
        listAdd.add("Roxas");
        listAdd.add("Sagay");
        listAdd.add("Samal");
        listAdd.add("San Carlos");
        listAdd.add("San Fernando");
        listAdd.add("San Jose");
        listAdd.add("San Jose del Monte");
        listAdd.add("San Juan");
        listAdd.add("San Pablo");
        listAdd.add("San Pedro");
        listAdd.add("Santa Rosa");
        listAdd.add("Santo Tomas");
        listAdd.add("Santiago");
        listAdd.add("Silay");
        listAdd.add("Sipalay");
        listAdd.add("Sorsogon City");
        listAdd.add("Surigao City");
        listAdd.add("Tabaco");
        listAdd.add("Tabuk");
        listAdd.add("Tacloban");
        listAdd.add("Tacurong");
        listAdd.add("Tagaytay");
        listAdd.add("Tagbilaran");
        listAdd.add("Taguig");
        listAdd.add("Tagum");
        listAdd.add("Talisay");
        listAdd.add("Tanauan");
        listAdd.add("Tandag");
        listAdd.add("Tangub");
        listAdd.add("Tanjay");
        listAdd.add("Tarlac City");
        listAdd.add("Tayabas");
        listAdd.add("Toledo");
        listAdd.add("Trece Martires");
        listAdd.add("Tuguegarao");
        listAdd.add("Tuguegarao");
        listAdd.add("Urdaneta");
        listAdd.add("Valencia");
        listAdd.add("Valenzuela");
        listAdd.add("Victorias");
        listAdd.add("Vigan");
        listAdd.add("Zamboanga City");

        ArrayAdapter<String> adapterAdd;
        adapterAdd = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listAdd);


        adapterAdd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerAddress.setAdapter(adapterAdd);
        mspinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Address(City)")){
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                    address = "";
                }else{
                    address = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + address, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-generated method stub
            }
        });
    }

}