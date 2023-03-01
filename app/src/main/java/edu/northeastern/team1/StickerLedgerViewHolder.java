package edu.northeastern.team1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StickerLedgerViewHolder extends RecyclerView.ViewHolder {
    public TextView count;
    public ImageView image;

    public StickerLedgerViewHolder(View view) {
        super(view);
        this.count = view.findViewById(R.id.use_count);
        this.image = view.findViewById(R.id.stickerImage);
    }
}
