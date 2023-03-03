package edu.northeastern.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickerLedgerActivity extends AppCompatActivity {
    private String loggedInUser;
    private RecyclerView stickerRecycler;
    private StickerLedgerAdapter adapter;
    private List<StickerLedger> stickerLedgerList = new ArrayList<>();
    private Map<String, String> imageMap;
    private Map<String, String> useCounts;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_sticker_ledger);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.imageMap = new HashMap<>();
        this.useCounts = new HashMap<>();

        // get logged in user
        loggedInUser = intent.getStringExtra("Current_user");
        setUpRecycler(savedInstanceState);

        findImages();
        finduseCounts();
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
                        imageMap.put(key, url);
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
        imageThread imageThread = new imageThread();
        new Thread(imageThread).start();
    }

    public void getFirebaseUseCounts(){
        DatabaseReference sticker_count = databaseReference.child("sticker_count");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot curr : snapshot.getChildren()) {
                        if (curr.getKey().equalsIgnoreCase(loggedInUser)) {
                            for(DataSnapshot ds : curr.getChildren()) {
                                String key = ds.getKey();
                                String count = ds.getValue().toString();
                                useCounts.put(key, count);

                                String url = imageMap.get(key);
                                StickerLedger sL = new StickerLedger(url, "Sent count: " + count);
                                stickerLedgerList.add(sL);
                            }
                        }
                    }

                }
                adapter.notifyItemRangeInserted(0, stickerLedgerList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        sticker_count.addListenerForSingleValueEvent(eventListener);
    }

    class useCountsThread implements Runnable {
        @Override
        public void run() {
            try {
                getFirebaseUseCounts();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    private void finduseCounts() {
        useCountsThread useCountsThread = new useCountsThread();
        new Thread(useCountsThread).start();
    }

    private void setUpRecycler(Bundle savedInstanceState) {
        stickerRecycler = findViewById(R.id.recyclerViewStickers);
        stickerRecycler.setHasFixedSize(true);

        // set layout
        stickerRecycler.setLayoutManager(new LinearLayoutManager(this));

        //set adapter
        adapter = new StickerLedgerAdapter(stickerLedgerList, this);
        stickerRecycler.setAdapter(adapter);

        // add divided b/w links
        DividerItemDecoration decor = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        stickerRecycler.addItemDecoration(decor);

    }

}