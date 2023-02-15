package edu.northeastern.team1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowViewHolder> {

    private List<Show> listOfShows;
    private Context context;
    private RecycleViewClickListener listener;

    public ShowAdapter(List<Show> shows, Context con) {
        this.listOfShows = shows;
        this.context = context;
    }

    public void setListenerLink(RecycleViewClickListener lst) {
        this.listener = lst;

    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.show_link_obj, parent, false);

        return new ShowViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder( ShowViewHolder holder, int position) {
        Show curr = listOfShows.get(position);
        //holder.name.setText(curr.getName());

    }

    @Override
    public int getItemCount() {
        return listOfShows.size();
    }

}