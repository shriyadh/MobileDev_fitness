package edu.northeastern.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Conversation_list extends AppCompatActivity implements NewChat.DgListener{

    private RecyclerView conversationRecycler;
    private List<Conversations> listOfUsers = new ArrayList<>();
    private ConvoAdapter adapter;
    private String loggedInUser;
    private DatabaseReference databaseReference;
    private TextView convTitle;
    private FloatingActionButton fBtn;
    private String findUser;

    private boolean exists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // get logged in user
        loggedInUser = intent.getStringExtra("Current_user");
        setContentView(R.layout.activity_conversation_list);
        convTitle = findViewById(R.id.Conversations);
        convTitle.setText("CONVERSATIONS FOR " + loggedInUser.toUpperCase(Locale.ROOT));
        // load things from onSavedInstance on orientation change
        init(savedInstanceState);

        // Write a message to the database
        runFirebase();
        createFloatingImplement();


    }

    public void runFirebase(){

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userName = databaseReference.child("members");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for(DataSnapshot userData : snapshot.getChildren()) {

                        List<String> users = new ArrayList<>();

                        for(DataSnapshot curr : userData.getChildren()) {
                            String a = curr.getKey();
                            users.add(a);
                        }

                        if(users.contains(loggedInUser)) {
                            String id = userData.getKey();
                            String add_user="";
                            for(String name : users){
                                if(!name.equals(loggedInUser)){
                                    add_user = name;
                                }
                            }

                            Conversations conversation = new Conversations(add_user,id);
                            listOfUsers.add(conversation);
                        }

                    }

                }
                else{
                    // no chats to display
                }

                adapter.notifyItemRangeInserted(0, listOfUsers.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        userName.addListenerForSingleValueEvent(eventListener);


    }

    private void createFloatingImplement(){

        fBtn = (FloatingActionButton) findViewById(R.id.floatingBtnAddLinks);
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating the alert dialog box
                NewChat input = new NewChat();
                input.show(getSupportFragmentManager(), "New Chat");
            }
        }

        )
        ;

        String chatUser = "mariah";
        DatabaseReference chatWith = this.databaseReference.child("testlogin").child(chatUser);

        //if username same as login, then try again
        // check username to see if user exists

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) { // if the user doesnt exist
                    Toast.makeText(getApplicationContext() , "Sorry! There is not user with that username. Please try messaging someone else", Toast.LENGTH_SHORT).show();
                }
                else {
                    exists = true; // user does exists
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("tag", databaseError.getMessage()); //Don't ignore errors!
            }
        };


        chatWith.addListenerForSingleValueEvent(eventListener); 


        // allow user to chat if the person exists
        /*
        if(exists) {
            Intent intent = new Intent(this, Conversation_list.class);
            intent.putExtra("Current_user", loggedInUser);
            intent.putExtra("Chat_buddy", )
            startActivity(intent);
        }

         */

    }



    public void init(Bundle savedInstanceState) {
        //loadSavedInstance(savedInstanceState);
        setUpRecycler();
    }

    private void setUpRecycler() {
        conversationRecycler = findViewById(R.id.recyclerViewMessages);
        conversationRecycler.setHasFixedSize(true);

        // set layout
        conversationRecycler.setLayoutManager(new LinearLayoutManager(this));

        //set adapter
        adapter = new ConvoAdapter(listOfUsers, this);
        conversationRecycler.setAdapter(adapter);

        // set the listener for recycler view
        RecycleViewClickListener listener = new RecycleViewClickListener() {
            @Override
            public void onLinkClick(int position) {
                // start message box activity
                String chatID = listOfUsers.get(position).getConversation_id();
                String clicked_user = listOfUsers.get(position).getConversation_id();
                Intent startChat = new Intent(getWindow().getContext(), Test.class);
                startChat.putExtra("Logged_user", loggedInUser);
                startChat.putExtra("Clicked user", clicked_user);
                startChat.putExtra("chatID", chatID);
                startActivity(startChat);

            }
        };
        adapter.setListenerLink(listener);

        // add divided b/w links
        DividerItemDecoration decor = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        conversationRecycler.addItemDecoration(decor);

    }


    @Override
    public void applyTexts(String user) {
        this.findUser = user;
    }
}