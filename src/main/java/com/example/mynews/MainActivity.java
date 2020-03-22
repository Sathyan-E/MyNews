package com.example.mynews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static String myApi="5d1151ed-36f2-4e96-8e96-fe8218862c82";
    private static String LINK="https://content.guardianapis.com/search";
    private NewsAdapter myNewsAdapter;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //emptyTextView to show a text when the ListView is empty
        emptyText = (TextView)findViewById(R.id.emptySetText);

        //ListView binding
        ListView listView = (ListView) findViewById(R.id.list);

        //NewsAdapter Instannce
        myNewsAdapter =new NewsAdapter(MainActivity.this, new ArrayList<News>());

        //LoaderManager initalization
        LoaderManager loaderManager  = getLoaderManager();
        loaderManager.initLoader(1,null,MainActivity.this);

       // myNewsAdapter=new NewsAdapter(this,myNewsList);
        listView.setAdapter(myNewsAdapter);
        listView.setEmptyView(emptyText);
        //setting onItemClickListner to the ListVIew
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //Getting current NewsObject
                News currentNews = myNewsAdapter.getItem(position);
                //getting the uri of current NewsObject
                Uri currentUri = Uri.parse(currentNews.getUrl());
                //implicit Intent initialization
                Intent goWebsite = new Intent(Intent.ACTION_VIEW,currentUri);
                startActivity(goWebsite);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        //sharedPreferences instance initialiazation
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String query =sharedPreferences.getString(getString(R.string.settings_category_key),getString(R.string.settings_category_default));
        //Using Uri.Builder to make the query
        Uri baseUri = Uri.parse(LINK);
        Uri.Builder uriBuilder =baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.main_query_key),query);
        uriBuilder.appendQueryParameter(getString(R.string.date_query_key),getString(R.string.date_query_value));
        uriBuilder.appendQueryParameter(getString(R.string.api_key),myApi);
       //Log Message
        Log.i("StringBuilder","value is"+uriBuilder.toString());
        return new NewsLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        //To check internet connectivity
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected())
        {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);


            emptyText.setText(R.string.empty_warning_message);

            myNewsAdapter.clear();
            if (data!=null && !data.isEmpty())
            {
                myNewsAdapter.addAll(data);
            }
        }
        else {
            emptyText.setText(R.string.internet_warning_message);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    myNewsAdapter.clear();
    }
    //settings menu inflation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    //Intent action if setting menu is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //getting the id of click item
        int id = item.getItemId();
        if (id==R.id.action_settings)
        {
            //initialization of explicit Intent to go settings activity
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
