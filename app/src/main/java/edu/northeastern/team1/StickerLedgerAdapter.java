package edu.northeastern.team1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class StickerLedgerAdapter extends RecyclerView.Adapter<StickerLedgerViewHolder> {
    private List<StickerLedger> stickerLedgerList;

    public StickerLedgerAdapter(List<StickerLedger> StickerLedger, Context con) {
        this.stickerLedgerList = StickerLedger;
    }

    @NonNull
    @Override
    public StickerLedgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sticker_obj, parent, false);

        return new StickerLedgerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerLedgerViewHolder holder, int position) {
        StickerLedger curr = stickerLedgerList.get(position);
        holder.count.setText(curr.getCount());
        Picasso.get()
                .load(curr.getUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return stickerLedgerList.size();
    }
}
