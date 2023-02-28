package edu.northeastern.team1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseActivity extends AppCompatActivity {
    private EditText curUser;
    private Button loginbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        myRef.setValue("Hello, World!");
    }

    public void logIn(View view) {
        String user = String.valueOf(curUser.getText());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getUsers = database.getReference("users");

    }

}