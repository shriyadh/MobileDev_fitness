package edu.northeastern.team1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView imgUrl;

    public MessageViewHolder(View view, RecycleViewClickListener listener) {
        super(view);
        this.username = view.findViewById(R.id.textViewUserSent);
        this.imgUrl = view.findViewById(R.id.imageViewStickerSent);
    }
}
