package edu.northeastern.team1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_object, parent, false);
        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message currentMessage = messageList.get(position);
        holder.sentBy.setText(currentMessage.getSender());

        class runnableThread implements Runnable{

            @Override
            public void run() {
                try{
                    DatabaseReference image = FirebaseDatabase.getInstance()
                            .getReference("images").child(currentMessage.getImage());
                    image.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String imageUrl = snapshot.getValue(String.class);
                            Picasso.get()
                                    .load(imageUrl)
                                    .into(holder.image);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("Image Retrieval Error", error.getMessage());
                        }
                    });

                } catch (DatabaseException e){
                    e.printStackTrace();
                }

            }
        }


        runnableThread runnableThread = new runnableThread();
        new Thread(runnableThread).start();



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}


/**
 * @Override
 *     public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
 *         Message currentMessage = messageList.get(position);
 *         holder.sentBy.setText(currentMessage.getSender());
 *         DatabaseReference image = FirebaseDatabase.getInstance()
 *                 .getReference("images").child(currentMessage.getImage());
 *         image.addValueEventListener(new ValueEventListener() {
 *             @Override
 *             public void onDataChange(@NonNull DataSnapshot snapshot) {
 *                 String imageUrl = snapshot.getValue(String.class);
 *                 Picasso.get()
 *                         .load(imageUrl)
 *                         .into(holder.image);
 *             }
 *
 *             @Override
 *             public void onCancelled(@NonNull DatabaseError error) {
 *                 Log.d("Image Retrieval Error", error.getMessage());
 *             }
 *         });
 *     }
 */