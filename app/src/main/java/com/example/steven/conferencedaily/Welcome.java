package com.example.steven.conferencedaily;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by steven on 11-3-15.
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
                    sleep(3000);

                }catch (InterruptedException e)
                {
                    e.getStackTrace();

                }
                finally {

                    Intent start1=new Intent("com.example.steven.conferencedaily.MainActivity");
                    startActivity(start1);

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
