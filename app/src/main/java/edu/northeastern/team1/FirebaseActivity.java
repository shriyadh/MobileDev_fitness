package edu.northeastern.team1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseActivity extends AppCompatActivity {
    private EditText curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        curUser = findViewById(R.id.username);
    }

    public void logIn(View view) {
        String user = String.valueOf(curUser.getText()); //current username
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userName = rootRef.child("testlogin").child(user);

        // check username and create and store into firebase if not already exist
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    rootRef.child("testlogin").child(user).setValue("");
                }
                else{
                    // allow user to log in
                    // Intent intent = new Intent(this,);
                    // startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("tag", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userName.addListenerForSingleValueEvent(eventListener);


    }

}