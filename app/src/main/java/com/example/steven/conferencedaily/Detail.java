package com.example.steven.conferencedaily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Show the Detail of the meeting though the transmitted text
 * For further use, can elaborate more information about the meeting.
 */
public class Detail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
          String Detail = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(this.getClass().getSimpleName(),Detail);
            ((TextView)findViewById(R.id.Meetingdetail)).setText(Detail);
        }


        }

}
