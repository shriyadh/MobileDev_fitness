package edu.northeastern.team1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        // load things from onSavedInstance on orientation change
        loadSavedInstance(savedInstanceState);

    }

    private void loadSavedInstance(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("RETRIEVE_LINKS")) {
            if (listOfShows == null || listOfShows.size() == 0) {

                int len = savedInstanceState.getInt("SIZE_OF_LINKS");

                for (int i = 0; i < len; i++) {
                    // what are we getting from the savedinstance????
                    String title = savedInstanceState.getString("UNIQUE_ID" + i + "0");
                    String img_url = savedInstanceState.getString("UNIQUE_ID" + i + "1");
                    String description = savedInstanceState.getString("UNIQUE_ID" + i + "2");
                    int year = savedInstanceState.getInt("UNIQUE_ID" + i + "3");
                    double rating = savedInstanceState.getDouble("UNIQUE_ID" + i + "4");

                    Show add_show = new Show(title, description, img_url, rating, year);

                    listOfShows.add(add_show);
                }
            }
        }
        else {
            //nothing
            listOfShows = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveThis) {

        int len = listOfShows == null? 0: listOfShows.size();
        saveThis.putInt("NUM_LINKS_TO_RETRIEVE", len);
        for(int i =0; i < len; i++) {
            saveThis.putString("UNIQUE_ID"+ i + "0", listOfShows.get(i).getName());
            saveThis.putString("UNIQUE_ID" + i + "1", listOfShows.get(i).getPicture());
            saveThis.putString("UNIQUE_ID" + i + "2", listOfShows.get(i).getDescription());
            saveThis.putInt("UNIQUE_ID" + i + "3", listOfShows.get(i).getYear());
            saveThis.putDouble("UNIQUE_ID" + i + "4", listOfShows.get(i).getRating());

        }
        super.onSaveInstanceState(saveThis);
    }

    private void setUpRecycler(){
        showRecycler = findViewById(R.id.recyclerView);
        showRecycler.setHasFixedSize(true);

        // set layout
        showRecycler.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        adapter = new ShowAdapter(listOfShows, this);
        showRecycler.setAdapter(adapter);

        // set the listener for recycler view
        RecycleViewClickListener listener = new RecycleViewClickListener() {
            @Override
            public void onLinkClick(int position) {
                // add functionality for opening alert dialog box to display information
            }
        };
        adapter.setListenerLink(listener);

        // add divided b/w links
        DividerItemDecoration decor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        showRecycler.addItemDecoration(decor);

        // do we have swipe functionality for our recycler???????????????
    }
}
