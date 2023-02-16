package edu.northeastern.team1;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ShowViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView year;
    public ImageView poster;

    public  ShowViewHolder(View view , RecycleViewClickListener lst){
        super(view);
        this.title = view.findViewById(R.id.tv_name);
        this.year = view.findViewById(R.id.tv_show_year);
        this.poster = view.findViewById(R.id.imageView);


        // set onClick listener on specific obj
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getLayoutPosition();
                if(pos != RecyclerView.NO_POSITION){
                    lst.onLinkClick(pos);
                }
            }
        });


    }
}
