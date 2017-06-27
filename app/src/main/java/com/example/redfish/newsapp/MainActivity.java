package com.example.redfish.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.redfish.newsapp.Models.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";
    private ProgressBar progress;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress =(ProgressBar)findViewById(R.id.progressBar);
        rv=(RecyclerView)findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));

        NetworkTask task=new NetworkTask("");
        task.execute();
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


    // Asynctask
    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> implements newsAdapter.ItemClickListener {
        String query;
        ArrayList<NewsItem> data;

        NetworkTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {

            ArrayList<NewsItem> result = null;
            URL url = NetworkUtils.makeURL();
            Log.d(TAG, "url: " + url.toString());
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result=NetworkUtils.parseJSON(json);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> data) {
            this.data=data;
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);
            if (data != null) {
                newsAdapter adapter = new newsAdapter(data, this);
                rv.setAdapter(adapter);

            }
        }
        @Override
        public void onItemClick(int clickedItemIndex) {
            String url = data.get(clickedItemIndex).getUrl();
            Log.d(TAG, String.format("Url %s", url));
            openWebPage(url);
        }

        public void openWebPage(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
