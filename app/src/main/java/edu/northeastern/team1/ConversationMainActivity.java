package edu.northeastern.team1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConversationMainActivity extends AppCompatActivity {
    private RecyclerView messageRecycler;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    public List<DataSnapshot> allMsgRef = new ArrayList<>();

    private int chatID;
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

    public void retreiveData(View v){
        runnableThread runnableThread = new runnableThread();
        runnableThread.setCid(1);
        new Thread(runnableThread).start();
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
                System.out.println(messages);



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
            outState.putInt(MESSAGE_INSTANCE_KEY + i + "0", messageList.get(i).getMid());
            outState.putString(MESSAGE_INSTANCE_KEY + i + "1", messageList.get(i).getSender());
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
            retreiveData(view);
        } else if (clickId == foodButton.getId()) {

        } else if (clickId == raceCarButton.getId()) {

        } else if (clickId == sunsetButton.getId()) {

        }
    }

    public void createMessageList() {
        for (DataSnapshot message: allMsgRef) {
            int mid = Integer.parseInt(Objects.requireNonNull(message.getKey()));
            String sender = message.child("sender").getValue(String.class);
            String image = message.child("image").getValue(String.class);

            Message newMessage = new Message(mid, sender, image);

            System.out.println("New Message:" + newMessage);

            messageList.add(newMessage);
            messageAdapter.notifyItemInserted(messageList.size());
        }
//        System.out.println("Message List" + messageList);
    }

}