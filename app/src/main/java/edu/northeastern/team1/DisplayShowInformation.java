package edu.northeastern.team1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DisplayShowInformation extends AppCompatDialogFragment {
    private TextView title;
    private TextView description;
    private TextView year;
    private TextView rating;
    private ImageView img;
    private int pos;
    private boolean displayingShow;

    private List<Show> listShows;

    public DisplayShowInformation(List<Show> lstshows, int position) {
        listShows = lstshows;
        pos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder inputBox = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.display_show_info, null);
        inputBox.setView(view);
        inputBox.setTitle("Show Information");
        inputBox.setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dg, int which) {
                displayingShow = false;
            }
        });
        img = view.findViewById(R.id.img_url);
        title = view.findViewById(R.id.title_txt);
        description = view.findViewById(R.id.description_txt);
        rating = view.findViewById(R.id.rating_txt);
        year = view.findViewById(R.id.year_txt);

        setView();
        displayingShow = true;

        return inputBox.create();
    }

    public void setView() {
        Show curr = listShows.get(pos);
        String name = "Title: " + curr.getName();
        String image = curr.getPicture();

        Picasso.get()
                .load(image)
                .into(img);


        title.setText(name);

        String des = "Description: " + curr.getDescription();
        des = des.replaceAll("<p>", "");
        des = des.replaceAll("</p>", "");
        des = des.replaceAll("<b>", "");
        des = des.replaceAll("</b>", "");

        description.setText(des);

        String rate = "Rating: " + curr.getRating();
        rating.setText(rate);

        String yr = "Year: " + curr.getYear();
        year.setText(yr);

    }

    public boolean getDisplayingShowStatus() {
        return displayingShow;
    }

    public int getPos() {
        return this.pos;
    }

}