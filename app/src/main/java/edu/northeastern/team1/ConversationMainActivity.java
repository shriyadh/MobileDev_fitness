package edu.northeastern.team1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ConversationMainActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    private TextView chatName;

    private Button dogsButton;
    private Button foodButton;
    private Button raceCarButton;
    private Button sunsetButton;

    private static final String SIZE_OF_MESSAGES = "SIZE_OF_MESSAGES";
    private static final String MESSAGE_INSTANCE_KEY = "MESSAGE_INSTANCE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);

        init(savedInstanceState);

        this.chatName = findViewById(R.id.textViewUsername);

        this.dogsButton = findViewById(R.id.dogsButton);
        this.foodButton = findViewById(R.id.foodButton);
        this.raceCarButton = findViewById(R.id.racecarButton);
        this.sunsetButton = findViewById(R.id.sunsetButton);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int length = messageList == null ? 0 : messageList.size();

        outState.putInt(SIZE_OF_MESSAGES, length);
        for (int i = 0; i < length; i++) {
            outState.putInt(MESSAGE_INSTANCE_KEY + i + "0", messageList.get(i).getMid());
            outState.putString(MESSAGE_INSTANCE_KEY + i + "1", messageList.get(i).getSentBy());
            outState.putString(MESSAGE_INSTANCE_KEY + i + "2", messageList.get(i).getImage());
        }

        super.onSaveInstanceState(outState);
    }

    public void init(Bundle savedInstanceState) {
        loadSavedInstance(savedInstanceState);
        setUpRecycler();
    }

    private void loadSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SIZE_OF_MESSAGES)) {
            int length = savedInstanceState.getInt(SIZE_OF_MESSAGES);
            for (int i = 0; i < length; i++) {
                Integer mid = savedInstanceState.getInt(MESSAGE_INSTANCE_KEY + i + "0");
                String sentBy = savedInstanceState.getString(MESSAGE_INSTANCE_KEY + i + "1");
                String image = savedInstanceState.getString(MESSAGE_INSTANCE_KEY + i + "2");

                Message newMessage = new Message(mid, sentBy, image);
            }
        }
    }

    private void setUpRecycler() {
        messageRecycler = findViewById(R.id.messageRecycler);
        messageRecycler.setHasFixedSize(true);

        // Set Layout
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        messageAdapter = new MessageAdapter(messageList, this);
        messageRecycler.setAdapter(messageAdapter);

        // Add Divider Decor
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        messageRecycler.addItemDecoration(dividerItemDecoration);
    }

    public void onClick(View view) {
        int clickId = view.getId();
        if (clickId == dogsButton.getId()) {

        } else if (clickId == foodButton.getId()) {

        } else if (clickId == raceCarButton.getId()) {

        } else if (clickId == sunsetButton.getId()) {

        }
    }
}