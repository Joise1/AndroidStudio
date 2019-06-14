package com.example.xuruoxi.huarongdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "This is before create.");
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "This is before set content.");
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "This is after set content.");
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, selectActivity.class);
        startActivity(intent);
    }

    public void exitGame(View view) {
        finish();
    }
}
