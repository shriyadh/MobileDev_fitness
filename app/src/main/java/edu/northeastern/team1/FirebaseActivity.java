package edu.northeastern.team1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

//        // Obtain the FirebaseAnalytics instance.
//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        mFirebaseAnalytics.logEvent("Gino", null);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }
}