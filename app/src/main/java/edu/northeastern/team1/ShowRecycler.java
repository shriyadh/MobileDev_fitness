package edu.northeastern.team1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShowRecycler extends AppCompatActivity {

    private RecyclerView showRecycler;
    private List<Show> listOfShows = new ArrayList<>();

    private RecycleViewClickListener listener;
    private ShowAdapter adapter;
    private RecyclerView.LayoutManager layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

        /// do we wanna load shit from savedinstance



    }
}
