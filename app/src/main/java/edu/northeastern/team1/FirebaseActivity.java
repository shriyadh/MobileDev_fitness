package edu.northeastern.team1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FirebaseActivity extends AppCompatActivity {
    private EditText curUser;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        curUser = findViewById(R.id.username);
    }

    public String createUser(String user){
        // checking for special characters
        // https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character
        Pattern special = Pattern.compile ("[!,@#$%&*()+=|<>?{}\\[\\]~-]");
        Matcher m = special.matcher(user);

        if(m.find()) { // if any special characters found
            Toast.makeText(getApplicationContext() , "Username cannot contain any special characters!", Toast.LENGTH_SHORT).show();
            return null;
        }
        //check if username is all numbers
        boolean digits = allNumbers(user);
        if(digits){ // username was all numbers
            Toast.makeText(getApplicationContext() , "Username must have at least one letter!", Toast.LENGTH_SHORT).show();
            return null;
        }
        //check for username with spaces
        if(user.contains(" ")) {
            this.user = this.user.replaceAll("\\s+", "_"); // add hyphen if there are spaces
            Toast.makeText(getApplicationContext() , "Your username has been changed to: " + this.user, Toast.LENGTH_SHORT).show();
            return this.user;
        }
        return user;
    }

    public void setStickerLedger(String username) {
        DatabaseReference sticker_count = FirebaseDatabase.getInstance()
                                                          .getReference().child("sticker_count");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sticker_count.child(username).child("dogs").setValue(0);
                sticker_count.child(username).child("food").setValue(0);
                sticker_count.child(username).child("race_car").setValue(0);
                sticker_count.child(username).child("sunset").setValue(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        sticker_count.addListenerForSingleValueEvent(eventListener);
    }

    public void logIn(View view) {
        this.user = String.valueOf(curUser.getText()).toLowerCase();
        this.user = user.strip(); //current username
        String verifiedName = createUser(user);
        if (verifiedName==null){
            return;
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userName = rootRef.child("users").child(user);


        // check username and create and store into firebase if not already exist
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    rootRef.child("users").child(user).setValue("");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("tag", databaseError.getMessage()); //Don't ignore errors!
            }
        };


        userName.addListenerForSingleValueEvent(eventListener);


        setStickerLedger(user);
        // allow user to log in

         Intent intent = new Intent(this , Conversation_list.class);
         intent.putExtra("Current_user", user);
         startActivity(intent);




    }

    // geeksforgeeks
    public boolean allNumbers(String name){

        for (int i = 0; i < name.length(); i++) {

            if (!Character.isDigit(name.charAt(i))) {
                return false;
            }
        }
        // all characters were numbers
        return true;
    }


}