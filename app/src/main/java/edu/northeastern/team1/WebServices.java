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
    private Handler textHandler = new Handler();


    private static final String TAG = "WebServiceActivity";


    ArrayList<String> shows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

    }

    public void callApi(View v){
        // store users show
       // String str = mURLEditText.getText().toString();
        //String str = mURLEditText.getText().toString();
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
                URL url = new URL(search);
                System.out.println(url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("first");
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                System.out.println("Connected");

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "IOException");
                e.printStackTrace();
            }
        }


    }



}
