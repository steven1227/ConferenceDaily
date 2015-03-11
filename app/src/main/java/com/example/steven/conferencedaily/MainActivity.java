package com.example.steven.conferencedaily;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private ArrayAdapter<String> meetingAdaptor;
    private ListView meetinglist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] data = {
                "Mon ",
                "Tue",
                "Wed",
                "Thurs ",
                "Fri",
                "Sat ",
                "Sun "
        };


        String JSONString = null;
        JSONObject JSONObject = null;

        //open the inputStream to the file
        try {
            InputStream inputStream = getAssets().open("event1.json");
            Log.i("??","1");



        } catch (IOException e) {
            Log.i("??","2");
            e.printStackTrace();


        }


        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));


        this.meetingAdaptor =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        weekForecast
                );

        meetinglist = (ListView) findViewById(R.id.listView);
        meetinglist.setAdapter(meetingAdaptor);
        meetinglist.setOnItemClickListener(this);


    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutus) {
            Intent about=new Intent(this,AboutUs.class);
            startActivity(about);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String meeting=meetingAdaptor.getItem(position);
            Context context =this;
            CharSequence text = meeting;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent intent=new Intent(this ,Detail.class).putExtra(Intent.EXTRA_TEXT,meeting);
            startActivity(intent);
    }
}
