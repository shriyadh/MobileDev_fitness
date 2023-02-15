package edu.northeastern.team1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class WebServices extends AppCompatActivity {
    ArrayList<String> shows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

    }

    public void callApi(View v){
        String str = "https://api.tvmaze.com/search/shows?q=sunny%20in";
        runnableThread runnableThread = new runnableThread();
        runnableThread.setURL(str);
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
                System.out.println(url);

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
                for (int i = 0; i < jArray.length(); i++ ) {
                    // store the first object in the array
                    JSONObject obj = jArray.getJSONObject(i);
                    System.out.println(obj);

                    // the object contains another object called "show"
                    // store the show object
                    JSONObject show = obj.getJSONObject("show");

                    // get show name
                    String name = show.getString("name");
                    System.out.println(name);

                    // get show description
                    String description = show.getString("summary");
                    System.out.println(description);

                    // get average rating
                    JSONObject rating = show.getJSONObject("rating");
                    String avg_rating = rating.getString("average");
                    System.out.println(avg_rating);

                    // get image link
                    JSONObject image = show.getJSONObject("image");
                    String img_link = image.getString("medium");
                    System.out.println(img_link);

                    //get premiere date
                    String date = show.getString("premiered");
                    System.out.println(date);

                    //making sure shows are stored
                    //will need to store show objects instead
                    shows.add(name);

                    System.out.println(shows);}

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private String StreamToString(InputStream i) {
        Scanner s = new Scanner(i).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
