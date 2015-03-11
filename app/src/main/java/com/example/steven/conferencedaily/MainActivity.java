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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private ArrayAdapter<String> meetingAdaptor;
    private ListView meetinglist;
    private TextView meetDay;
    private final static String fileName = "events.json";
    private final String LOG_TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> weekForecast = new ArrayList<String>();


        this.meetingAdaptor =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        weekForecast
                );

        meetDay=(TextView)findViewById(R.id.textView);
        meetinglist = (ListView) findViewById(R.id.listView);
        meetinglist.setAdapter(meetingAdaptor);
        meetinglist.setOnItemClickListener(this);


        weekForecast.clear();
        weekForecast.add("steven8");


        final String name= "name";
        final String start= "start";
        final String end = "end";
        final String id= "id";

        SimpleDateFormat duration =new SimpleDateFormat("HH:mm");
        SimpleDateFormat weekday =new SimpleDateFormat("EEEE");
        SimpleDateFormat monthday =new SimpleDateFormat( "MMMM dd");

        weekday.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String jsonStr = getJson(this.fileName);
        try {
            JSONArray array = new JSONArray(jsonStr);
            int len = array.length();

            JSONObject dayForecast = array.getJSONObject(50);
            String begin= duration.format(new Date (dayForecast.getInt(start)*1000L));
            String stop = duration.format(new Date( dayForecast.getInt(end)*1000L));
            String description = dayForecast.getString(name);
            Log.v(LOG_TAG, "Forecast entry: " + weekday.format(new Date (dayForecast.getInt(start)*1000L)));

            meetDay.setText( weekday.format(new Date (dayForecast.getInt(start)*1000L))+ "\n" +monthday.format(new Date (dayForecast.getInt(start)*1000L)));
            weekForecast.add(description+"\n"+begin+" - "+stop+"\n"+"Location");



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     *
     */
    private String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    getAssets().open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
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
            Intent intent=new Intent(this ,Detail.class).putExtra(Intent.EXTRA_TEXT,meeting);// not only post the meeting
            startActivity(intent);
    }
}
