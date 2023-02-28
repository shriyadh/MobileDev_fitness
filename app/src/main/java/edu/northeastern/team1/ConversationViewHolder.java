package edu.northeastern.team1;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ConversationViewHolder extends RecyclerView.ViewHolder {

    // add views jere

    public ConversationViewHolder(View view, RecycleViewClickListener lst) {
        super(view);
        // find ids and attach

        // set onClick here
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getLayoutPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    lst.onLinkClick(pos);
                }
            }
        });
    }
}
