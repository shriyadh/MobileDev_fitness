package edu.northeastern.team1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void webServicesIntent(View view) {
        Intent intent = new Intent(this, WebServices.class);
        startActivity(intent);
    }

    public void firebaseActivity(View view) {
        Intent intent = new Intent(this, FirebaseActivity.class);
        startActivity(intent);
    }

    public void aboutInformation(View view) {
        Intent intent = new Intent(this, AboutTeam.class);
        startActivity(intent);
    }

    public void testConversation(View view){
        Intent intent = new Intent(this, ConversationMainActivity.class);
        startActivity(intent);
    }
}