package com.example.xuruoxi.huarongdao;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity {
    static final String LOG_TAG = GameActivity.class.getSimpleName();
    private TextView t;
    private TextView stepNum;
    private Chronometer timer;
    private int gameId;
    private ConstraintLayout layout;

    private int[] rawfiles = {
            R.raw.mapfile0,
            R.raw.mapfile1,
            R.raw.mapfile2,
            R.raw.mapfile3,
            R.raw.mapfile4,
            R.raw.mapfile5,
            R.raw.mapfile6,
            R.raw.mapfile7,
            R.raw.mapfile8
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        layout = findViewById(R.id.TrueGameLayout);
        t = findViewById(R.id.ContestName);
        stepNum = findViewById(R.id.StepNum);
        // get gameId
        Intent intent = getIntent();
        gameId = intent.getIntExtra("gameId", -1);
        // start timer
        timer = (Chronometer) findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        // set map
        ArrayList<MyChess> chesses = new ArrayList<>();
        HRDFileReader hrdFileReader = new HRDFileReader(getResources().openRawResource(rawfiles[gameId]), stepNum);
        String name = hrdFileReader.getName();
        t.setText(name);
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i) {
            MyChess chess = hrdFileReader.getChess(this);
            layout.addView(chess);
            chesses.add(chess);
        }
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i) {
            chesses.get(i).setRelativeChesses(chesses);
            chesses.get(i).setBounds(0, 0, hrdFileReader.getMaxWidth(), hrdFileReader.getMaxHeight());
        }
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i){
            if (chesses.get(i).getText().equals(hrdFileReader.getWinnerName())){
                chesses.get(i).setWinner(hrdFileReader.getWinnerX(), hrdFileReader.getWinnerY());
            }
        }
    }

    public void lastGame(View view) {
        if (gameId > 0) {
            gameId--;
            this.newGame(view, gameId);
        }

    }

    public void nextGame(View view) {
        if (gameId < rawfiles.length-1) {
            gameId++;
            this.newGame(view, gameId);
        }
    }
    public void newGame(View view, int gameId) {
        layout.removeAllViews();
        // restart timer
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        // set map
        ArrayList<MyChess> chesses = new ArrayList<>();
        HRDFileReader hrdFileReader = new HRDFileReader(getResources().openRawResource(rawfiles[gameId]), stepNum);
        // set contest name
        String name = hrdFileReader.getName();
        t.setText(name);
        // set chess
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i) {
            MyChess chess = hrdFileReader.getChess(this);
            layout.addView(chess);
            chesses.add(chess);
        }
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i) {
            chesses.get(i).setRelativeChesses(chesses);
            chesses.get(i).setBounds(0, 0, hrdFileReader.getMaxWidth(), hrdFileReader.getMaxHeight());
        }
        for (int i = 0; i < hrdFileReader.getAllCount(); ++i){
            if (chesses.get(i).getText().equals(hrdFileReader.getWinnerName())){
                chesses.get(i).setWinner(hrdFileReader.getWinnerX(), hrdFileReader.getWinnerY());
            }
        }
    }
}

class HRDFileReader {
    private Scanner reader;
    private int m_allCount;
    private int max_width, max_height;
    private String winner_name;
    private String name;
    private int winner_x, winner_y;
    TextView stepNum;
    static final String LOG_TAG = GameActivity.class.getSimpleName();

    public HRDFileReader(InputStream istream, TextView t) {
        Log.d(LOG_TAG, "in create.");
        reader = new Scanner(istream);
        Log.d(LOG_TAG, "open the file.");
        name = reader.next();
        m_allCount = reader.nextInt();
        max_width = reader.nextInt();
        max_height = reader.nextInt();
        winner_name = reader.next();
        winner_x = reader.nextInt();
        winner_y = reader.nextInt();
        stepNum = t;
    }

    public String getName() {return name;}

    public int getAllCount() { return m_allCount; }

    public int getMaxWidth() { return max_width; }

    public int getMaxHeight() { return max_height; }

    public MyChess getChess(Context context) {
        MyChess chess = new MyChess(context, reader.next(), reader.nextInt(), reader.nextInt(), reader.nextInt(), reader.nextInt(), stepNum);
        return chess;
    }

    public String getWinnerName(){ return winner_name; }

    public int getWinnerX() { return winner_x; }

    public int getWinnerY() { return winner_y; }
}
