package com.example.steven.conferencedaily;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> meetingAdaptor;
    private ListView meetinglist;
    private TextView meetDay;
    private final static String fileName = "events.json";
    private final String LOG_TAG=MainActivity.class.getSimpleName();
    private List<String> meetingArray = new ArrayList<String>();
    public static JSONArray  SortedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialcomponent();

        final String name= "name";
        final String start= "start";
        final String end = "end";

        SimpleDateFormat duration =new SimpleDateFormat("hh:mm a");
        SimpleDateFormat weekday =new SimpleDateFormat("EEEE");
        SimpleDateFormat monthday =new SimpleDateFormat( "MMMM dd");

        weekday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        monthday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        duration.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String jsonStr = getJson(this.fileName);

        try {
            JSONArray originArray = new JSONArray(jsonStr);

            SortedList= meetingSort(originArray);
            int len = SortedList.length();

            // inflate the meeting list to the list view
            for ( int i=0;i<=len;i++)
            {
                JSONObject MeetingObject = SortedList.getJSONObject(i);
                Date meetingtime1 = new Date(MeetingObject .getInt(start) * 1000L);
                Date meetingtime2 = new Date(MeetingObject .getInt(end) * 1000L);
                String begin = duration.format(meetingtime1);
                String stop = duration.format(meetingtime2);
                String day = weekday.format(meetingtime1);
                String description = MeetingObject .getString(name);
                meetingArray .add("\n"+description + "\n\n" +day + ", " + begin + " - " + stop+"\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialcomponent() {

        this.meetingAdaptor =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        meetingArray
                );

        meetDay=(TextView)findViewById(R.id.textView);
        meetinglist = (ListView) findViewById(R.id.listView);
        meetinglist.setAdapter(meetingAdaptor);
        meetinglist.setOnItemClickListener(this);
        meetingArray .clear();
    }


    /**
     * Self-defined sorting function.
     * Using the Collection built-in sorting function with the comparator to compare the starting time
     * In addition, filter the duplicated object first to save the work.
     */
    private JSONArray meetingSort( JSONArray originArray) {
        int flag=0;
        List<JSONObject> jsonValues = new ArrayList<>();

        for (int i = 0; i < originArray.length(); i++) {
            flag = 0;
            try {
                for (JSONObject a : jsonValues) {
                    if (originArray.getJSONObject(i).getString("name").equals(a.getString("name")))  // filter the duplicated JSON object
                    {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0)
                    jsonValues.add(originArray.getJSONObject(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsonValues,new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                int A=0;
                int B=0;
                try {
                   A=a.getInt("start");
                   B=b.getInt("start");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(A>B)
                      return 1;
                if(A<B)
                      return -1;
                return 0;
            }
        });

        JSONArray sortedJsonArray = new JSONArray(jsonValues);
        return sortedJsonArray;
    }

    /**
     * Get the Jason from the Jason file. Translate it to Jason string
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

        /**
         * A shout dialogue activity if you click the menu and click the about button
         * Enter the happening activity to see the happening list If cleck the WHAT HAPPENING button
         */
        if (id == R.id.aboutus) {
            Intent about=new Intent(this,AboutUs.class);
            startActivity(about);
        }
        else if(id == R.id.now)
        {
            Intent now=new Intent(this,Happening.class);
            startActivity(now);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *The extra function, to show the detail when you click on any meeting in the list.
     * Now, it transmit the information of name and key to the Detail Activity
     * For further use, can transmit more useful information of Jason object with parcel,
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           Log.e(LOG_TAG,""+position);
           String meetingname =new String();
           String meetingid   = new String();
           String meeting=meetingAdaptor.getItem(position);
        try {
            JSONObject sending = SortedList.getJSONObject(position);
            meetingname = sending.getString("name");
            meetingid= sending.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            Log.e(LOG_TAG,meetingname+"\n"+meetingid);
        Context context =this;
            CharSequence text = meeting;
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            Intent intent=new Intent(this,Detail.class).putExtra(Intent.EXTRA_TEXT, "Presentation: "+meetingname+"\n\n"+"Key: "+meetingid);
            startActivity(intent);
    }
}
