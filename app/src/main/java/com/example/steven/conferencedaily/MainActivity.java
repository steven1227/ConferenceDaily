package com.example.steven.conferencedaily;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    private List<String> weekForecast = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        final String name= "name";
        final String start= "start";
        final String end = "end";
        final String id= "id";

        SimpleDateFormat duration =new SimpleDateFormat("HH:mm");
        SimpleDateFormat weekday =new SimpleDateFormat("EEEE");
        SimpleDateFormat monthday =new SimpleDateFormat( "MMMM dd");

        weekday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        monthday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        duration.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String jsonStr = getJson(this.fileName);

        try {
            JSONArray originArray = new JSONArray(jsonStr);
            int len = originArray.length();


            JSONArray array= meetingSort(originArray);

            // inflate the meeting list to the list view
            for ( int i=0;i<=len;i++)
            {

                JSONObject dayForecast = array.getJSONObject(i);
                Date meetingtime1 = new Date(dayForecast.getInt(start) * 1000L);
                Date meetingtime2 = new Date(dayForecast.getInt(end) * 1000L);
                String begin = duration.format(meetingtime1);
                String stop = duration.format(meetingtime2);
                String day = weekday.format(meetingtime1);
                String description = dayForecast.getString(name);
                weekForecast.add("\n"+description + "\n\n" +"Time: "+ day + " " + begin + " - " + stop+"\n");
            }
          //  meetDay.setText( weekday.format(new Date (dayForecast.getInt(start)*1000L))+ "\n" +monthday.format(new Date (dayForecast.getInt(start)*1000L)));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONArray meetingSort( JSONArray originArray) {
        int flag=0;
        List<JSONObject> jsonValues = new ArrayList<>();

        for (int i = 0; i < originArray.length(); i++) {
            flag = 0;
            try {
                for (JSONObject a : jsonValues) {
                    if (originArray.getJSONObject(i).getString("name").equals(a.getString("name")))
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
      //  weekForecast.add(""+sortedJsonArray.length());
        return sortedJsonArray;
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
        else if(id == R.id.now)
        {
            Intent now=new Intent(this,Happening.class);
            startActivity(now);
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
