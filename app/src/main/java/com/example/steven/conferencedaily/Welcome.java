package com.example.steven.conferencedaily;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * A welcome page of the application. Creating a new Thread and keep it about
 * 2 sec before enter the MainActivity.
 */
public class Welcome extends Activity {
    protected void onCreate(Bundle Steven) {
        super.onCreate(Steven);
        setContentView(R.layout.splash);

        Thread timer=new Thread()
        {
            @Override
            public void run()
            {
                try{
                    sleep(2000);

                }catch (InterruptedException e)
                {
                    e.getStackTrace();

                }
                finally {

                    Intent start1=new Intent("com.example.steven.conferencedaily.MainActivity");
                    startActivity(start1);    // Enter the MainActivity after the welcome Page.

                }


            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
