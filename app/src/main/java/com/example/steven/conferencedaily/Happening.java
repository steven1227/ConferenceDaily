package com.example.steven.conferencedaily;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * filter the meeting list to show the happening now
 */
public class Happening extends Activity {

    private ArrayAdapter<String> meetingAdaptor;
    private ListView meetinglist;
    private TextView current;
    private List<String> happenlist = new ArrayList<String>();
    private JSONArray SortedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happening);
        initialcomponent();

        SimpleDateFormat duration =new SimpleDateFormat("hh:mm a");    // The Date format translation
        SimpleDateFormat weekday =new SimpleDateFormat("EEEE");
        SimpleDateFormat monthday =new SimpleDateFormat( "MMMM dd");

        weekday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        monthday.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        duration.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        final String name= "name";
        final String start= "start";
        final String end = "end";

        JSONArray filterlist=timefiler(SortedList);
        int len = filterlist.length();
        try {
            // inflate the meeting list to the list view
            for ( int i=0;i<=len;i++)
            {
                JSONObject dayForecast = filterlist.getJSONObject(i);
                Date meetingtime1 = new Date(dayForecast.getInt(start) * 1000L);
                Date meetingtime2 = new Date(dayForecast.getInt(end) * 1000L);
                String begin = duration.format(meetingtime1);
                String stop = duration.format(meetingtime2);
                String day = weekday.format(meetingtime1);
                String description = dayForecast.getString(name);
                happenlist.add("\n" + description + "\n\n" + day + ", " + begin + " - " + stop + "\n");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * The function used to filger the time to choose the meeting happening now.
     * Use System.currentTimeMillis() to take the current time in UTC timezone
     * Compared it with the Start and End in the sorted meeting list.
     */

    private JSONArray timefiler(JSONArray sortedList) {

        long currentTime = System.currentTimeMillis() / 1000L;
       SimpleDateFormat temp2=new SimpleDateFormat("EEEE dd MMMM hh:mm a");

        temp2.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String hello2=temp2.format(new Date(currentTime* 1000L));

        current.setText("\nCurrent Time (UTC-5:00):\n"+hello2);    // fill the text to show the current time in the UTC-5:00 time zone
        List<JSONObject> jsonValues = new ArrayList<>();

        for (int i = 0; i < sortedList.length(); i++) {
            try {

                if(sortedList.getJSONObject(i).getInt("start")<=currentTime && sortedList.getJSONObject(i).getInt("end")>=currentTime)
                {

                    jsonValues.add(sortedList.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        JSONArray filterlist = new JSONArray(jsonValues);
        return filterlist;
    }

    private void initialcomponent() {
        SortedList=MainActivity.SortedList;
        current=(TextView)findViewById(R.id.current);
        this.meetingAdaptor =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        happenlist
                );

        meetinglist = (ListView) findViewById(R.id.listView);
        meetinglist.setAdapter(meetingAdaptor);
        happenlist.clear();

    }
}
