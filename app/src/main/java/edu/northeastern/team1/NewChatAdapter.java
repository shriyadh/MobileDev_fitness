package edu.northeastern.team1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatHolder>  {

    private List<Conversations> listOfUsers;
    private RecycleViewClickListener listener;

    public NewChatAdapter(List<Conversations> listOfUsers, Context con) {
        this.listOfUsers = listOfUsers;
    }

    public void setListenerLink(RecycleViewClickListener lst) {
        this.listener = lst;
    }

    @NonNull
    @Override
    public NewChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.new_chat_users, parent, false);

        return new NewChatHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatHolder holder, int position) {
        Conversations curr = listOfUsers.get(position);
        holder.username.setText(curr.getUser());
    }


    @Override
    public int getItemCount() {
        return listOfUsers.size();    }
}
