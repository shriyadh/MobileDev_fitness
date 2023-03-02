package edu.northeastern.team1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConversationMainActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private List<Message> messageList = new ArrayList<>();
    public List<DataSnapshot> allMsgRef = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private TextView chatName;
    public String chatId;
    public String curUser;
    public String chatUser;

    private ImageView dogsButton;
    private ImageView foodButton;
    private ImageView raceCarButton;
    private ImageView sunsetButton;

    private static final String SIZE_OF_MESSAGES = "SIZE_OF_MESSAGES";
    private static final String MESSAGE_INSTANCE_KEY = "MESSAGE_INSTANCE_KEY";

    private static final String DOGS_URL = "https://images.unsplash.com/photo-1444212477490-ca407925329e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8ZG9nc3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60";
    private static final String FOOD_URL = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60";
    private static final String RACE_CAR_URL = "https://images.unsplash.com/photo-1630446838167-4f63bc82e1cf?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8cmFjZSUyMGNhciUyMGYxfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=600&q=60";
    private static final String SUNSET_URL = "https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8c3Vuc2V0fGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=600&q=60";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);

        // grab chat_id and logged in user from previous activity
        Intent i = getIntent();
        chatId = i.getStringExtra("chatID");
        curUser = i.getStringExtra("Logged_user");
        chatUser = i.getStringExtra("Clicked user");

        init(savedInstanceState);

        this.chatName = findViewById(R.id.textViewUsername);
        chatName.setText(chatUser);

        this.dogsButton = findViewById(R.id.dogsButton);
        Picasso.get()
                .load(DOGS_URL)
                .into(dogsButton);
        this.foodButton = findViewById(R.id.foodButton);
        Picasso.get()
                .load(FOOD_URL)
                .into(foodButton);
        this.raceCarButton = findViewById(R.id.racecarButton);
        Picasso.get()
                .load(RACE_CAR_URL)
                .into(raceCarButton);
        this.sunsetButton = findViewById(R.id.sunsetButton);
        Picasso.get()
                .load(SUNSET_URL)
                .into(sunsetButton);
    }


    class runnableThread implements Runnable {
        private Integer cid;

        public void setCid(int cid) {this.cid = cid;}

        @Override
        public void run() {
            try{
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference messages = rootRef.child("messages").child(String.valueOf(this.cid));

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot eachMsg:snapshot.getChildren()) {
                                allMsgRef.add(eachMsg);
                            }
                        }
                        createMessageList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("error");
                        Log.d("tag", error.getMessage());
                    }
                };
                messages.addListenerForSingleValueEvent(eventListener);
            } catch (DatabaseException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int length = messageList == null ? 0 : messageList.size();

        outState.putInt(SIZE_OF_MESSAGES, length);
        for (int i = 0; i < length; i++) {
            outState.putLong(MESSAGE_INSTANCE_KEY + i + "0", messageList.get(i).getMid());
            outState.putString(MESSAGE_INSTANCE_KEY + i + "1", messageList.get(i).getSender());
            outState.putString(MESSAGE_INSTANCE_KEY + i + "2", messageList.get(i).getImage());
        }

        super.onSaveInstanceState(outState);
    }

    public void init(Bundle savedInstanceState) {
        loadSavedInstance(savedInstanceState);

        runnableThread runnableThread = new runnableThread();
        runnableThread.setCid(Integer.parseInt(chatId));
        new Thread(runnableThread).start();

        setUpRecycler();


    }

    private void loadSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SIZE_OF_MESSAGES)) {
            int length = savedInstanceState.getInt(SIZE_OF_MESSAGES);
            for (int i = 0; i < length; i++) {
                long mid = savedInstanceState.getLong(MESSAGE_INSTANCE_KEY + i + "0");
                String sentBy = savedInstanceState.getString(MESSAGE_INSTANCE_KEY + i + "1");
                String image = savedInstanceState.getString(MESSAGE_INSTANCE_KEY + i + "2");

                Message newMessage = new Message(mid, sentBy, image);
                messageList.add(newMessage);
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
            long mid = new Date().getTime();
            Message newMessage = new Message(mid, curUser, "dogs");
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
            messageRecycler.scrollToPosition(messageList.size() - 1);

            sendImage(newMessage);
            getOldCount("dogs");
        } else if (clickId == foodButton.getId()) {
            long mid = new Date().getTime();
            Message newMessage = new Message(mid, curUser, "food");
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
            messageRecycler.scrollToPosition(messageList.size() - 1);

            sendImage(newMessage);
            getOldCount("food");
        } else if (clickId == raceCarButton.getId()) {
            long mid = new Date().getTime();
            Message newMessage = new Message(mid, curUser, "race_car");
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
            messageRecycler.scrollToPosition(messageList.size() - 1);

            sendImage(newMessage);
            getOldCount("race_car");
        } else if (clickId == sunsetButton.getId()) {
            long mid = new Date().getTime();
            Message newMessage = new Message(mid, curUser, "sunset");
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
            messageRecycler.scrollToPosition(messageList.size() - 1);

            sendImage(newMessage);
            getOldCount("sunset");
        }
    }

    public void getOldCount(String sticker) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user_sticker_count = rootRef.child("sticker_count").child(curUser);
        DatabaseReference sticker_count = user_sticker_count.child(sticker);
        ValueEventListener eL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // grab old value
                Long val = snapshot.getValue(Long.class);
                // increment
                Long newVal = val + 1;
                // update db
                sticker_count.setValue(newVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        sticker_count.addListenerForSingleValueEvent(eL);
    }

    public void createMessageList() {
        for (DataSnapshot message: allMsgRef) {
            long mid = Long.parseLong(Objects.requireNonNull(message.getKey()));
            String sender = message.child("sender").getValue(String.class);
            String image = (message.child("image").getValue(String.class));
            Message newMessage = new Message(mid, sender, image);
            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
            messageRecycler.scrollToPosition(messageList.size() - 1);

        }
    }

    private void sendImage(Message message) {
        sendThread sendThread = new sendThread();
        sendThread.setMessage(message);
        new Thread(sendThread).start();
    }

    class sendThread implements Runnable {
        private Message message;
        public void setMessage(Message message) {
            this.message = message;
        }
        @Override
        public void run() {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference chatID = rootRef.child("messages").child(chatId);
            chatID.child(message.getMid().toString());
            DatabaseReference messageID = chatID.child(message.getMid().toString());
            messageID.child("image").setValue(message.getImage());
            messageID.child("sender").setValue(message.getSender());
        }
    }

}