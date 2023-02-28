package edu.northeastern.team1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ConversationMainActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;


    private Button dogsButton;
    private Button foodButton;
    private Button raceCarButton;
    private Button sunsetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);
    }
}