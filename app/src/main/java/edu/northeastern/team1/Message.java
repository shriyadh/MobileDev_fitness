package edu.northeastern.team1;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Message {
    private Integer mid;
    private String sentBy;
    private String image;
    private DatabaseReference mDatabase;


    public Message(Integer mid, String sentBy, String image) {
        mDatabase = FirebaseDatabase.getInstance().getReference("messages");

        this.mid = mid;

        this.sentBy = sentBy;
        this.image = image;
    }

    public Integer getMid() {
        return mid;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getImage() {
        return image;
    }
}
