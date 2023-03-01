package edu.northeastern.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class conversation_list extends AppCompatActivity {

    private RecyclerView conversationRecycler;
    private List<Conversations> listOfUsers = new ArrayList<>();
    private ConvoAdapter adapter;
    private EditText searchBar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        // load things from onSavedInstance on orientation change
        init(savedInstanceState);


        //this.searchBar = findViewById(R.id.EditText_TV_searchbar);

        // Write a message to the database
        runFirebase();


    }

    public void runFirebase(){
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        //Log.d("u", "users" + users);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfUsers.clear();
                for(DataSnapshot userData : snapshot.child("users_shriya").getChildren()) {

                    final String name = userData.getValue(String.class);
                    Conversations convo = new Conversations(name,"0");
                    listOfUsers.add(convo);
                }
                Log.d("RYN", "here " + listOfUsers.size());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void init(Bundle savedInstanceState) {
        //loadSavedInstance(savedInstanceState);
        setUpRecycler();
    }

    private void setUpRecycler() {
        conversationRecycler = findViewById(R.id.recyclerView);
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