package edu.northeastern.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Conversation_list extends AppCompatActivity {

    private RecyclerView conversationRecycler;
    private List<Conversations> listOfUsers = new ArrayList<>();
    private ConvoAdapter adapter;
    private String loggedInUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // get logged in user
        loggedInUser = intent.getStringExtra("Current_user");
        setContentView(R.layout.activity_conversation_list);

        // load things from onSavedInstance on orientation change
        init(savedInstanceState);

        // Write a message to the database
        runFirebase();


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
                        System.out.println("here" + users.get(0) );
                        System.out.println("here" + users.get(1) );

                        Conversations conversation = new Conversations("name","0");
                        listOfUsers.add(conversation);


                    }



                }

                adapter.notifyItemRangeInserted(0, listOfUsers.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        userName.addListenerForSingleValueEvent(eventListener);


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
                // add functionality for opening alert dialog box to display information
               // DisplayShowInformation display = new DisplayShowInformation(listOfUsers, position);
               // display.show(getSupportFragmentManager(), "Show Information");
                //Window window = display.getDialog().getWindow();
            }
        };
        adapter.setListenerLink(listener);

        // add divided b/w links
        DividerItemDecoration decor = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        conversationRecycler.addItemDecoration(decor);

    }




}