package edu.northeastern.team1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class WebServices extends AppCompatActivity {
    // store shows from query
    ArrayList<String> shows = new ArrayList<>();

    //textedit holds user's show
    private EditText mURLEditText;

    //api link
    private String API = "https://api.tvmaze.com/search/shows?q=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);

        // find id of the show
       // mURLEditText = (EditText)findViewById(R.id.URL_et);
    }

    public void callApi(View v){
        // store users show
       // String str = mURLEditText.getText().toString();
        String search = "https://api.tvmaze.com/search/shows?q=sunny%20in";

        runnableThread runnableThread = new runnableThread();
        runnableThread.setData(search);
        new Thread(runnableThread).start();
    }

    class runnableThread implements Runnable {
        // name, picture, premiere date year, description, rating
        private String data;
        String[] results = new String[2];

        public void setData(String _data) {
            this.data = _data;
        }

        @Override
        public void run() {

        }
    }





}