package edu.northeastern.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GenericLifecycleObserver;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;

public class Conversation_list extends AppCompatActivity implements NewChat.DgListener{

    private RecyclerView conversationRecycler;
    private List<Conversations> listOfUsers = new ArrayList<>();
    private ConvoAdapter adapter;
    private String loggedInUser;
    private DatabaseReference databaseReference;
    private TextView convTitle;
    private FloatingActionButton fBtn;
    private String findUser;


    private static String SERVER_KEY = "key=AAAAA-o_j1w:APA91bG-EIZHa2SWLK_sJawMhwOTWVlGqSSY0OfRUsHEItLB1qmCrEYgpjMXM-vyGbSVUXKbx-C_86-pwtTl9j3ZnD6DZ4BIxCKdohuYriz4dWfMimDH1c_w7ROc_JJgYNzpufkRRSJM";
    private static String CLIENT_REGISTRATION_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // get logged in user
        loggedInUser = intent.getStringExtra("Current_user");
        Log.v("User","user"+loggedInUser);
        setContentView(R.layout.activity_conversation_list);
        convTitle = findViewById(R.id.Conversations);
        convTitle.setText("CONVERSATIONS FOR " + loggedInUser.toUpperCase(Locale.ROOT));
        // load things from onSavedInstance on orientation change
        init(savedInstanceState);

        // Write a message to the database
        run_fbThread();
        createFloatingImplement();

        // Generate the token

        //using firebasedemo example code
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Conversation_list.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    if (CLIENT_REGISTRATION_TOKEN == null) {
                        CLIENT_REGISTRATION_TOKEN = task.getResult();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child("token").child(CLIENT_REGISTRATION_TOKEN).setValue(loggedInUser);

                    }
                    //Log.v("CLITENT TOKENNNNN", "HERE:"+CLIENT_REGISTRATION_TOKEN);
                    Toast.makeText(Conversation_list.this, "CLIENT_TOKEN IS: " + CLIENT_REGISTRATION_TOKEN, Toast.LENGTH_SHORT).show();


                }
            }
        });

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to leave?\n");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.child("token").child(CLIENT_REGISTRATION_TOKEN).setValue("");
                CLIENT_REGISTRATION_TOKEN = null;
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void runFirebase(){

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userName = databaseReference.child("members");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for(DataSnapshot userData : snapshot.getChildren()) {
                        System.out.println(userData);
                        List<String> users = new ArrayList<>();

                        for(DataSnapshot curr : userData.getChildren()) {
                            String a = curr.getKey();
                            users.add(a);
                        }
                        //System.out.println(users+loggedInUser);

                        System.out.println(users.contains(loggedInUser));


                        if(users.contains(loggedInUser)) {
                            System.out.println("YES");

                            String id = userData.getKey();
                            String add_user="";
                            for(String name : users){
                                if(!name.equals(loggedInUser)){
                                    add_user = name;
                                    System.out.println(add_user);
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
                conversationRecycler.scrollToPosition(listOfUsers.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        userName.addListenerForSingleValueEvent(eventListener);


    }

    class fbThread implements Runnable {
        @Override
        public void run() {
            try {
                runFirebase();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    private void run_fbThread() {
        Conversation_list.fbThread fbThread = new Conversation_list.fbThread();
        new Thread(fbThread).start();
    }

    private void createFloatingImplement() {

        fBtn = (FloatingActionButton) findViewById(R.id.floatingBtnStartChat);
        fBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // creating the alert dialog box
                                        NewChat input = new NewChat();
                                        input.show(getSupportFragmentManager(), "New Chat");

                                    }
                                }

        );
        System.out.println("Before crash");
    }

    class newChatThread implements Runnable{

        @Override
        public void run() {
            try{
                startNewChat();
            } catch (DatabaseException e){
                e.printStackTrace();
            }
        }
    }

    public void startNewChatVThread(){
        newChatThread chatThread = new newChatThread();
        new Thread(chatThread).start();
    }


    public void startNewChat(){
        String chatUser = this.findUser;
        chatUser = chatUser.toLowerCase(Locale.ROOT);
        chatUser = chatUser.strip();


        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatWith = db1.child("users").child(chatUser);

        //if username same as login, then try again
        // check username to see if user exists

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // if the user doesnt exist
                    Toast.makeText(getApplicationContext(), "Sorry! There is not user with that username. Please try messaging someone else", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference inMember = db.child("members");
                    ValueEventListener eventListener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String secondUser = "";

                            if (snapshot.exists()) {
                                boolean flag = true;
                                for (DataSnapshot userData : snapshot.getChildren()) {

                                    List<String> users = new ArrayList<>();

                                    for (DataSnapshot curr : userData.getChildren()) {
                                        String a = curr.getKey();
                                        users.add(a);
                                    }

                                    if (users.contains(loggedInUser)) {
                                        String firstUser = userData.getKey();


                                        for (String name : users) {
                                            if (!name.equals(loggedInUser)) {
                                                secondUser = name;
                                            }
                                        }

                                        if (secondUser.equals(findUser)) {
                                            Toast.makeText(getApplicationContext(), "You already have a chat with this user!", Toast.LENGTH_LONG).show();
                                            flag = false;
                                            break;
                                        }
                                    }
                                }
                                if(flag) {
                                    // create unique chat ID
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference getlastID = db.child("total_chatID");
                                    System.out.println(getlastID);
                                    String lastIDKey = getlastID.getKey();


                                    ValueEventListener eventListener3 = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String key = snapshot.getKey();
                                            Long lastID = snapshot.getValue(Long.class);
                                            Long newID = lastID + 1;
                                            String newID_str = newID.toString();

                                            Conversations newChat = new Conversations(findUser, newID_str);
                                            listOfUsers.add(newChat);
                                            adapter.notifyItemInserted(listOfUsers.size());
                                            conversationRecycler.scrollToPosition(listOfUsers.size()-1);
                                            String lastAdded = listOfUsers.get(listOfUsers.size() -1).getConversation_id();
                                            Long addThis = Long.parseLong(lastAdded);


                                            // update db here
                                            db.child("total_chatID").setValue(addThis);

                                            db.child("chats").child(lastAdded).child("created").setValue(new Date().getTime());
                                            db.child("members").child(lastAdded).child(loggedInUser).setValue("");
                                            db.child("members").child(lastAdded).child(findUser).setValue("");
                                            System.out.println("HEREEE");

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    getlastID.addListenerForSingleValueEvent(eventListener3);




                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    inMember.addListenerForSingleValueEvent(eventListener1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("tag", databaseError.getMessage()); //Don't ignore errors!
            }

        };
        chatWith.addListenerForSingleValueEvent(eventListener);


    }

    public void init(Bundle savedInstanceState) {
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
                String clicked_user = listOfUsers.get(position).getUser();
                Intent startChat = new Intent(getWindow().getContext(), ConversationMainActivity.class);
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


    public void startHistoryIntent(View view){
        Intent history = new Intent(this, StickerLedgerActivity.class);
        history.putExtra("Current_user", loggedInUser);
        startActivity(history);
    }

    @Override
    public void applyTexts(String user) {

        this.findUser = user;
        findUser = findUser.strip();
        findUser = findUser.toLowerCase(Locale.ROOT);
        startNewChatVThread();
    }

    
}