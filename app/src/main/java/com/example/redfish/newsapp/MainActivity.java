package com.example.redfish.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";
    private ProgressBar progress;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress =(ProgressBar)findViewById(R.id.progressBar);
        textView=(TextView)findViewById(R.id.display_JSON);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.search) {
            String s= "";
            NetworkTask task= new NetworkTask(s);
            task.execute();
        }
        return true;
    }

    class NetworkTask extends AsyncTask<URL, Void, String> {
        String query;

        NetworkTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {

            String result = null;
            URL url = NetworkUtils.makeURL();
            Log.d(TAG, "url: " + url.toString());
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if (s == null) {
                textView.setText("Sorry, no text was received");
            } else {
                textView.setText(s);
            }
        }
    }
}
