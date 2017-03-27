package com.jspark.android.kardoc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final static int LOADING_SPEED = 3000;

    Thread splash = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                splash.sleep(LOADING_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            h.sendEmptyMessage(LOADING_SPEED);
        }
    });

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(LOADING_SPEED==msg.what) {
                Intent i = new Intent(MainActivity.this, SignActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        splash.start();
    }

}
