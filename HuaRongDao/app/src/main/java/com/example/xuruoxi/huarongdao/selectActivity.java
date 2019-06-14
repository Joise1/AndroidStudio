package com.example.xuruoxi.huarongdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class selectActivity extends AppCompatActivity {
    private static final String LOG_TAG = selectActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_select);
        Log.d(LOG_TAG, "This is after set content.");
        GridLayout gridLayout = findViewById(R.id.function_grid);

        for (int i = 0; i < rawfiles.length; ++i) {
            Button button = new Button(this);
            button.setText(Integer.toString(i + 1));
            button.setBackground(getResources().getDrawable(R.drawable.select_button));
            button.setTextAppearance(this, R.style.select_button);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(10, 5, 10, 5);
            button.setLayoutParams(lp);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(selectActivity.this, GameActivity.class);
                    intent.putExtra("gameId", finalI);
                    startActivity(intent);
                }
            });
            gridLayout.addView(button);
        }
    }
}
