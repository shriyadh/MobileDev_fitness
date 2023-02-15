package edu.northeastern.team1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.List;

public class DisplayShowInformation extends AppCompatDialogFragment {
    private TextView title;
    private TextView description;
    private TextView year;
    private TextView rating;
    private ImageView img;
    private int pos;

    private List<Show> listShows;
   // private DgListener dglistener;

    public DisplayShowInformation(List<Show> lstshows, int position){
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
                // do nothing
            }
        });

        title = view.findViewById(R.id.title_txt);
        description = view.findViewById(R.id.description_txt);
        rating = view.findViewById(R.id.rating_txt);
        year = view.findViewById(R.id.year_txt);

        setView();

        return inputBox.create();
    }

    public void setList(List<Show> lstshows) {
        this.listShows = lstshows;
    }

    public void setPos(int position){
        this.pos = position;

    }

    public void setView(){
        Show curr = listShows.get(pos);
        title.setText(curr.getName().toString());
        description.setText(curr.getDescription().toString());
        rating.setText(curr.getRating().toString());
        year.setText(curr.getYear().toString());
        //title.setText(curr.getName().toString());




    }



}