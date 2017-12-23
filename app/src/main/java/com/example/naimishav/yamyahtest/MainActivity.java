package com.example.naimishav.yamyahtest;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private StorageReference storageReference;
    Uri downloadURL;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    private EditText name;
    private EditText age;
    private EditText gender;


    private Button registerButton;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization of the FirebaseAuth Object
        firebaseAuth = FirebaseAuth.getInstance();

        //Checking whether a user as already Logged In
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            //Starting the User Profile Activity if the user is already Logged in
            startActivity(new Intent(getApplicationContext(), Sucess.class));
        }

        //Initialization of the ProgressDialog object
        progressDialog = new ProgressDialog(this);

        //Retrieving EditText field values from the XML and storing them in java Variables
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);


        registerButton = (Button) findViewById(R.id.registerButton);



        //Adding the listener function to both the register button and the login redirection link (Text)
        registerButton.setOnClickListener(this);


    }

    private void registerNewUser() {

        //Converting EditText type variables to String type variables
        final String Name =name.getText().toString().trim();
        final String Age = age.getText().toString().trim();
        final String Gender = gender.getText().toString().trim();





        firebaseAuth.createUserWithEmailAndPassword(Age, Gender).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                //checks whether the user has been successfully registered
                if (task.isSuccessful()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users = database.getReference("users");
                    users.child(Name).child("Name").setValue(name);
                    users.child(Age).child("Age").setValue(age);
                    users.child(Gender).child("Gender").setValue(gender);

                    //Displays a registration successful message through Toast
                    Toast.makeText(MainActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    finish();
                    //Redirects to the User Profile Activity
                    startActivity(new Intent(getApplicationContext(), Sucess.class));
                } else {
                    //Displays a registration Unsuccessful message through Toast
                    Toast.makeText(MainActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onClick(View view) {

      //  Intent intent = new Intent(this, Sucess.class);
       // startActivity(intent);

        //when registerButton is clicked registerNewUser method is invoked
        if(view == registerButton){
            registerNewUser();
        }



    }

    protected void updateProfile(String name, String age,String gender) {

//        UploadFile();



        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("Age", age);
        data.put("Gender",gender);


        db.collection("Profile").document(mAuth.getUid().toString()).set(data);


    }



}