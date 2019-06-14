package com.example.xuruoxi.huarongdao;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class Pair{
    int x;
    int y;
    Pair(int x, int y){this.x = x; this.y = y;}
}

public class MyChess extends android.support.v7.widget.AppCompatButton {
    private static final String LOG_TAG = MyChess.class.getSimpleName();
    TextView stepNum;

    private int width = getResources().getDisplayMetrics().widthPixels / 4;
    private int widthY = width*2/3;
    private int leftX, leftY;
    private int rightX, rightY;
    private ArrayList<MyChess> relativeChesses;

    private int currentX, currentY;
    private int minX, minY, maxX, maxY;
    private boolean isWin = false;
    private int win_x, win_y;

    public void setRelativeChesses(ArrayList<MyChess> chesses) {
        relativeChesses = chesses;
    }

    public void setBounds(int minX, int minY, int maxX, int maxY) {
        this.minX = minX * width;
        this.minY = minY * widthY;
        this.maxX = maxX * width;
        this.maxY = maxY * widthY;
    }

    public void setWinner(int X, int Y){
        this.isWin = true;
        this.win_x = X;
        this.win_y = Y;
    }

    public MyChess(final Context context, String name, int leftXNum, int leftYNum, int rightXNum, int rightYNum, final TextView stepNum) {
        super(context);
        // set TextView
        this.stepNum = stepNum;
        // set location and text of th button
        this.leftX = leftXNum * width;
        this.leftY = leftYNum * widthY;
        this.rightX = rightXNum * width;
        this.rightY = rightYNum * widthY;

        this.setWidth(rightX - leftX);
        this.setHeight(rightY - leftY);
        this.setX(leftX);
        this.setY(leftY);
        this.setText(name);
        // set background
        this.setBackground(getResources().getDrawable(R.drawable.chess_button));
        // set move event
        setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // when user first touch screen, get the location where user touched.
                    case MotionEvent.ACTION_DOWN:
                    {
                        currentX = (int) event.getX();
                        currentY = (int) event.getY();
                        break;
                    }
                    // when user move fingers, the button moves, too.
                    case MotionEvent.ACTION_MOVE: {
                        int dx = (int) event.getX() - currentX;
                        int dy = (int) event.getY() - currentY;
                        if (leftX + dx < minX) {
                            dx = minX - leftX;
                        } else if (rightX + dx > maxX) {
                            dx = maxX - rightX;
                        }
                        if (leftY + dy < minY) {
                            dy = minY - leftY;
                        } else if (rightY + dy > maxY) {
                            dy = maxY - rightY;
                        }
                        Pair move = new Pair(dx, dy);
                        for (int i = 0; i < relativeChesses.size(); ++i) {
                            if (relativeChesses.get(i) != MyChess.this) {
                                move = getTrueMove(MyChess.this, relativeChesses.get(i), move);
                            }
                        }
                        leftX += move.x;
                        leftY += move.y;
                        rightX += move.x;
                        rightY += move.y;
                        setX(leftX);
                        setY(leftY);
                        break;
                    }
                    // if user leave screen
                    case MotionEvent.ACTION_UP: {
                        // add step num
                        int count = Integer.parseInt(stepNum.getText().toString());
                        stepNum.setText(String.valueOf(count+1));
                        // other
                        leftX = Math.round((float) leftX / width) * width;
                        leftY = Math.round((float) leftY / widthY) * widthY;
                        rightX = Math.round((float) rightX / width) * width;
                        rightY = Math.round((float) rightY / widthY) * widthY;
                        setX(leftX);
                        setY(leftY);
                        if (isWin && win_x * width == leftX && win_y * widthY == leftY) {
                            Toast toast = Toast.makeText(context, getContext().getString(R.string.win_the_game), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            new Thread() {
                                public void run() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    ((Activity) context).finish();
                                }
                            }.start();
                            return true;
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    private Pair getTrueMove(MyChess chess1, MyChess chess2, Pair rawMove){
        // situation1: there is chess in vertical direction
        // situation1.1: after move, chess will overlap in lower direction.
        if (chess1.rightY <= chess2.leftY && chess1.rightY + rawMove.y > chess2.leftY){
            // may overlap
            if (chess1.leftX < chess2.rightX && chess1.rightX > chess2.leftX){
                rawMove.y = chess2.leftY - chess1.rightY;
            }
            else if (chess1.leftX == chess2.rightX || chess1.rightX == chess2.leftX){
                if (Math.abs(rawMove.x) > Math.abs(rawMove.y)){
                    rawMove.y = chess2.leftY - chess1.rightY;
                }
                else {
                    rawMove.x = 0;
                }
            }
        }
        // situation1.2: after move, chess will overlap in upper direction.
        else if (chess2.rightY <= chess1.leftY && chess1.leftY + rawMove.y < chess2.rightY){
            if (chess1.leftX < chess2.rightX && chess1.rightX > chess2.leftX){
                rawMove.y = chess2.rightY - chess1.leftY;
            }
            else if (chess1.leftX == chess2.rightX || chess1.rightX == chess2.leftX){
                if (Math.abs(rawMove.x) > Math.abs(rawMove.y)){
                    rawMove.y = chess2.rightY - chess1.leftY;
                }
                else {
                    rawMove.x = 0;
                }
            }
        }

        // situation2: there is chess in horizontal direction
        // situation2.1: after move, chess will overlap in left direction.
        if (chess1.rightX <= chess2.leftX && chess1.rightX + rawMove.x > chess2.leftX){
            if (chess1.rightY > chess2.leftY && chess1.leftY < chess2.rightY){
                rawMove.x = chess2.leftX - chess1.rightX;
            }
            else if (chess1.rightY == chess2.leftY || chess1.leftY == chess2.rightY){
                if (Math.abs(rawMove.x) > Math.abs(rawMove.y)){
                    rawMove.y = 0;
                }
                else {
                    rawMove.x = chess2.leftX - chess1.rightX;
                }
            }
        }
        // situation2.2: after move, chess will overlap in right direction.
        else if (chess2.rightX <= chess1.leftX && chess1.leftX + rawMove.x < chess2.rightX){
            if (chess1.rightY > chess2.leftY && chess1.leftY < chess2.rightY){
                rawMove.x = chess2.rightX - chess1.leftX;
            }
            else if (chess1.rightY == chess2.leftY || chess1.leftY == chess2.rightY){
                if (Math.abs(rawMove.x) > Math.abs(rawMove.y)){
                    rawMove.y = 0;
                }
                else {
                    rawMove.x = chess2.rightX - chess1.leftX;
                }
            }
        }
        return rawMove;
    }
}
