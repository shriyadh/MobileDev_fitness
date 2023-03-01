package edu.northeastern.team1;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewChat  extends AppCompatDialogFragment {
    List<String> users = new ArrayList<>();
    private DatabaseReference ref;

    private RecyclerView recycler;


    public NewChat(){
        getUsers();

    }

    public void getUsers(){
        this.ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference allUsers = ref.child("testlogin");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userData : snapshot.getChildren()) {
                    String member = userData.getKey();
                    users.add(member);
                }
                Log.v("Users", String.valueOf(users.size()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("tag", error.getMessage());

            }
        }
            ;

        allUsers.addListenerForSingleValueEvent(eventListener);


    }




}
