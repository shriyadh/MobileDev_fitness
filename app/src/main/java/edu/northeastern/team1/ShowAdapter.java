package edu.northeastern.team1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ShowAdapter extends RecyclerView.Adapter<ShowViewHolder> {

    private List<Show> listOfShows;
    private Context context;
    private RecycleViewClickListener listener;
    private String title;
    private String year;
    private String img;

    public ShowAdapter(List<Show> shows, Context con) {
        this.listOfShows = shows;
        this.context = con;
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
        holder.title.setText(curr.getName());
        holder.year.setText(curr.getYear());
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(curr.getPicture())
                        .into(holder.poster);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);

    }

    @Override
    public int getItemCount() {
        return listOfShows.size();
    }

}
