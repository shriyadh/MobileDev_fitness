package edu.northeastern.team1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ConvoAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    private List<Conversations> listOfUsers;
    private RecycleViewClickListener listener;

    public ConvoAdapter(List<Conversations> listOfUsers, Context con) {
        this.listOfUsers = listOfUsers;
    }

    public void setListenerLink(RecycleViewClickListener lst) {
        this.listener = lst;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.show_link_obj, parent, false);

        return new ConversationViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversations curr = listOfUsers.get(position);
        //holder.title.setText(curr.getName());
        //holder.year.setText(curr.getYear());

        // insert sticker here
        //Picasso.get().load(curr.getPicture()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }


}
