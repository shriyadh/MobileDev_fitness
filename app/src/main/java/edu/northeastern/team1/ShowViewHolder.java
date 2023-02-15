package edu.northeastern.team1;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ShowViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView year;
    public ImageView poster;
    // add button for switch


    public  ShowViewHolder(View view , RecycleViewClickListener lst){
        super(view);
        this.title = view.findViewById(R.id.tv_name);
        this.year = view.findViewById(R.id.tv_show_year);
        this.poster = view.findViewById(R.id.imageView);


        // this.search = view.findViewById(R.id.);
        // this.switch = view.findViewById(R.id.);
        // set onClick listener on specific obj


    }
}
