package edu.northeastern.team1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class WebServices extends AppCompatActivity {
    private RecyclerView showRecycler;
    private List<Show> listOfShows = new ArrayList<>();
    private ShowAdapter adapter;
    private EditText searchBar;
    private String url;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

        // load things from onSavedInstance on orientation change
        init(savedInstanceState);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        this.searchBar = findViewById(R.id.EditText_TV_searchbar);

        SwitchCompat filterSwitch = findViewById(R.id.year_new_to_old_switch);
        filterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                listOfShows.sort(Comparator.comparing(Show::getYear).reversed());
            } else {
                listOfShows.sort(Comparator.comparing(Show::getScore).reversed());
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void init(Bundle savedInstanceState) {
        loadSavedInstance(savedInstanceState);
        setUpRecycler();
    }

    public void parseSearchText(View v) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        listOfShows.clear();
        String enteredSearch = searchBar.getText().toString();
        enteredSearch = enteredSearch.replaceAll("\\s", "%20");
        url = "https://api.tvmaze.com/search/shows?q=" + enteredSearch;
        setUpRecycler();
        callApi(v);
    }

    public void callApi(View v) {
        runnableThread runnableThread = new runnableThread();
        runnableThread.setURL(url);
        new Thread(runnableThread).start();
    }


    class runnableThread implements Runnable {
        // name, picture, premiere date year, description, rating
        private String search;

        public void setURL(String data) {
            this.search = data;
        }

        @Override
        public void run() {
            try {
                // store search as a url
                URL url = new URL(search);

                // set up connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // app will read data from the URL connection
                conn.setDoInput(true);

                conn.connect();

                // get input from api and convert to string
                InputStream inputStream = conn.getInputStream();
                final String resp = StreamToString(inputStream);

                // Store array of objects that api returns
                JSONArray jArray = new JSONArray(resp);

                // loop through the array
                for (int i = 0; i < jArray.length(); i++) {
                    // store the first object in the array
                    JSONObject obj = jArray.getJSONObject(i);

                    // get show relevance score
                    double score = Double.parseDouble(obj.getString("score"));

                    // the object contains another object called "show"
                    // store the show object
                    JSONObject show = obj.getJSONObject("show");

                    // get show name
                    String name = show.getString("name");

                    // get show description
                    String description = show.getString("summary")
                            .replace("null", "N/A");

                    // get average rating
                    JSONObject rating = show.getJSONObject("rating");
                    String avg_rating = rating.getString("average")
                            .replace("null", "N/A");


                    // get image link
                    String img_link = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/" +
                            "No_image_available.svg/2048px-No_image_available.svg.png";
                    try {
                        JSONObject image = show.getJSONObject("image");
                        img_link = image.getString("medium");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //get premiere date

                    String date = show.getString("premiered").replace("null",
                            "N/A");
                    String year = date.equals("N/A") ?
                            "N/A" : date.split("-")[0];

                    //making sure shows are stored
                    //will need to store show objects instead
                    Show new_show = new Show(score, name, description, img_link, avg_rating, year);
                    listOfShows.add(new_show);
                }
                handler.post(() -> {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                });
                adapter.notifyItemRangeInserted(0, listOfShows.size());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String StreamToString(InputStream i) {
        Scanner s = new Scanner(i).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void loadSavedInstance(Bundle savedInstanceState) {
        // If Activity has already been opened
        if (savedInstanceState != null && savedInstanceState.containsKey("SIZE_OF_LINKS")) {
            int len = savedInstanceState.getInt("SIZE_OF_LINKS");

            for (int i = 0; i < len; i++) {
                // what are we getting from the savedinstance????
                double score = savedInstanceState.getDouble("UNIQUE_ID" + i + "0");
                String title = savedInstanceState.getString("UNIQUE_ID" + i + "1");
                String img_url = savedInstanceState.getString("UNIQUE_ID" + i + "2");
                String description = savedInstanceState.getString("UNIQUE_ID" + i + "3");
                String year = savedInstanceState.getString("UNIQUE_ID" + i + "4");
                String rating = savedInstanceState.getString("UNIQUE_ID" + i + "5");

                Show add_show = new Show(score, title, description, img_url, rating, year);

                listOfShows.add(add_show);
            }
        } else {
            //nothing
            listOfShows = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveThis) {
        int len = listOfShows == null ? 0 : listOfShows.size();

        saveThis.putInt("SIZE_OF_LINKS", len);
        for (int i = 0; i < len; i++) {
            saveThis.putDouble("UNIQUE_ID" + i + "0", listOfShows.get(i).getScore());
            saveThis.putString("UNIQUE_ID" + i + "1", listOfShows.get(i).getName());
            saveThis.putString("UNIQUE_ID" + i + "2", listOfShows.get(i).getPicture());
            saveThis.putString("UNIQUE_ID" + i + "3", listOfShows.get(i).getDescription());
            saveThis.putString("UNIQUE_ID" + i + "4", listOfShows.get(i).getYear());
            saveThis.putString("UNIQUE_ID" + i + "5", listOfShows.get(i).getRating());

        }
        super.onSaveInstanceState(saveThis);
    }

    private void setUpRecycler() {
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
                DisplayShowInformation display = new DisplayShowInformation(listOfShows, position);
                display.show(getSupportFragmentManager(), "Show Information");
                //Window window = display.getDialog().getWindow();
            }
        };
        adapter.setListenerLink(listener);

        // add divided b/w links
        DividerItemDecoration decor = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        showRecycler.addItemDecoration(decor);

    }

    @Override
    public void onBackPressed() {
        if (listOfShows.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to leave?\nResults will not be saved");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }
    }
}




