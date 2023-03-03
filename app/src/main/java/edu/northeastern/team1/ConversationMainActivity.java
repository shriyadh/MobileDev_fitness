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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private DatabaseReference databaseReference;

    private ImageView dogsButton;
    private ImageView foodButton;
    private ImageView raceCarButton;
    private ImageView sunsetButton;

    private static final String SIZE_OF_MESSAGES = "SIZE_OF_MESSAGES";
    private static final String MESSAGE_INSTANCE_KEY = "MESSAGE_INSTANCE_KEY";

    private static final String DOGS = "dogs";
    private static final String FOOD = "food";
    private static final String RACE_CAR = "race_car";
    private static final String SUNSET = "sunset";

    private static String SERVER_KEY = "key=AAAAA-o_j1w:APA91bG-EIZHa2SWLK_sJawMhwOTWVlGqSSY0OfRUsHEItLB1qmCrEYgpjMXM-vyGbSVUXKbx-C_86-pwtTl9j3ZnD6DZ4BIxCKdohuYriz4dWfMimDH1c_w7ROc_JJgYNzpufkRRSJM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_main);

        // init a button for EACH image(sticker) in DB
        this.dogsButton = findViewById(R.id.dogsButton);
        this.foodButton = findViewById(R.id.foodButton);
        this.raceCarButton = findViewById(R.id.racecarButton);
        this.sunsetButton = findViewById(R.id.sunsetButton);

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        // threaded loading of images into bottom buttons (for sending)
        findImages();

        // grab chat_id and logged in user from previous activity
        Intent i = getIntent();
        chatId = i.getStringExtra("chatID");
        curUser = i.getStringExtra("Logged_user");
        chatUser = i.getStringExtra("Clicked user");

        init(savedInstanceState);

        this.chatName = findViewById(R.id.textViewUsername);
        chatName.setText(chatUser);

    }

    public void getFirebaseImages(){
        DatabaseReference images = databaseReference.child("images");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot curr : snapshot.getChildren()) {
                        String key = curr.getKey();
                        String url = (String) curr.getValue();

                        switch (key) {
                            case DOGS:
                                Picasso.get()
                                        .load(url)
                                        .into(dogsButton);
                                break;
                            case FOOD:
                                Picasso.get()
                                        .load(url)
                                        .into(foodButton);
                                break;
                            case RACE_CAR:
                                Picasso.get()
                                        .load(url)
                                        .into(raceCarButton);
                                break;
                            case SUNSET:
                                Picasso.get()
                                        .load(url)
                                        .into(sunsetButton);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        images.addListenerForSingleValueEvent(eventListener);
    }

    class imageThread implements Runnable {
        @Override
        public void run() {
            try {
                getFirebaseImages();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    private void findImages() {
        ConversationMainActivity.imageThread imageThread = new ConversationMainActivity.imageThread();
        new Thread(imageThread).start();
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
            sendMessageToDevice();
        }
    }


    /**
     * Button Handler; creates a new thread that sends off a message to the target(this) device
     *
     * @param
     */
    public void sendMessageToDevice() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference s = ref.child("token").child("cdjp7y6JTIOGnM1mamn2kn:APA91bHmTEbi6aJ0OFyGb6SglJd7tJO5FHfSz_WkFzZQKRZdAPn_BJX9pSEtq6mqs58aa8Nd5xifCT125WaWQ8zG4twCAiRQx7nNvbg5avVDz5CbUbWxBnbYq49R54DxWH0TE746DnMz");
        String a = s.getKey();
        System.out.println(a);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessageToDevice(a);
            }
        }).start();
    }


    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void sendMessageToDevice(String targetToken) {

        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("body", "You have received a new message from " + curUser + "!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");



            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);
            //System.out.println("HEREEEEEE22222");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        final String resp = Utils.fcmHttpConnection(SERVER_KEY, jPayload);
        //Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());

    }

}