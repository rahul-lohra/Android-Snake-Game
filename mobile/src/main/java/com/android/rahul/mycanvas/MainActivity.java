package com.android.rahul.mycanvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @Bind(R.id.view)
    MyView mView;
    @Bind(R.id.btn_up)
    Button mBtnUp;
    @Bind(R.id.btn_left)
    Button mBtnLeft;
    @Bind(R.id.btn_right)
    Button mBtnRight;
    @Bind(R.id.btn_down)
    Button mBtnDown;

    String TAG = MainActivity.class.getSimpleName();
    int height,width;

    @OnClick({R.id.btn_up, R.id.btn_left, R.id.btn_right, R.id.btn_down,R.id.btn_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                moveUp();
                break;
            case R.id.btn_left:
                moveLeft();
                break;
            case R.id.btn_right:
                moveRight();
                break;
            case R.id.btn_down:
                moveDown();
                break;
            case R.id.btn_start:
                startGame();
                break;
        }
    }

    void startGame()
    {
        MyView.isGameStarted = true;
        MyView.isNewGame = true;
        getRowsAndCols();
        MyView.food.left = MyView.food.scale * new Random().nextInt(MyView.col+1);
        MyView.food.top = MyView.food.scale * new Random().nextInt(MyView.rows+1);
        mView.draw(new Canvas(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)));
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        getRowsAndCols();
//    }

    void getRowsAndCols()
    {
        String w = Integer.toString((int)mView.getWidth());
        String h = Integer.toString((int)mView.getHeight());


        char c[] = w.toCharArray();
        c[c.length-1] = '0';
        c[c.length-2] = '0';
        w = "";
        for(int i=0;i<c.length;++i)
        {
            w = w +c[i];
        }


        c = h.toCharArray();
        c[c.length-1] = '0';
        c[c.length-2] = '0';

        h = "";
        for(int i=0;i<c.length;++i)
        {
            h = h +c[i];
        }
        height = Integer.parseInt(h);
        width = Integer.parseInt(w);

        MyView.col = Integer.parseInt(w)/MyView.scale;
        MyView.rows = Integer.parseInt(h)/MyView.scale;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        getRowsAndCols();
//        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                getRowsAndCols();
//            }
//        });
//
//
//        mView.setLayoutParams(new LinearLayout.LayoutParams(width,height));

    }



    @Subscribe
    public void handleSomethingElse(MessageEvent event) {
        if(event.stop)

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,getString(R.string.game_over),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    void moveUp()
    {
        MyView.snake.dir = MyView.DIR_UP;
    }

    void moveDown()
    {
        MyView.snake.dir= MyView.DIR_DOWN;
    }

    void moveLeft()
    {
        MyView.snake.dir = MyView.DIR_LEFT;
    }

    void moveRight()
    {
        MyView.snake.dir = MyView.DIR_RIGHT;
    }

}
