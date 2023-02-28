package edu.northeastern.team1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;


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
        curUser = findViewById(R.id.username);


        myRef.setValue("Mariah!");
    }

    public void logIn(View view) {
        String user = String.valueOf(curUser.getText()); //current username
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getUsers = database.getReference("users"); // ref to all the users
        DatabaseReference current = getUsers.child(user);
        Log.v("CUR", String.valueOf(current));


    }

}