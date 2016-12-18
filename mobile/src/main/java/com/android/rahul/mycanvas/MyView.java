package com.android.rahul.mycanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by rkrde on 17-12-2016.
 */

public class MyView extends View {
    String TAG = MyView.class.getSimpleName();

    public static final int DIR_LEFT = 0, DIR_UP = 1, DIR_RIGHT = 2, DIR_DOWN = 3;

    public static Snake snake;public static Food food;
    Paint snakePaint,foodPaint;
    Rect snakeReact,foodReact;
    MainActivity mainActivity;
    public static boolean isGameStarted = false,isNewGame = false;
    public static int col,rows,scale;
    public static Thread thread;
    void init() {
        snake = new Snake();
        food = new Food();

        snakePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foodPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        foodPaint.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));

        food.left = food.scale * new Random().nextInt(col+1);
        food.top = food.scale * new Random().nextInt(rows+1);

    }

    public int i=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isGameStarted)
            return;

        if(isNewGame)
        {
                isNewGame = false;
            startFresh(canvas);
            return;
        }

        switch (snake.dir) {
            case DIR_LEFT:
                moveLeft(canvas);
                break;
            case DIR_UP:
                moveUP(canvas);
                break;
            case DIR_RIGHT:
                moveRight(canvas);
                break;
            case DIR_DOWN:
                moveDown(canvas);
                break;
        }
    }

    void createSnakeTail()
    {
        //copy to previous nodes...
        for(int i=Snake.snakeTailList.size()-2;i>=0;--i)
        {

            int L = Snake.snakeTailList.get(i).left;
            int T = Snake.snakeTailList.get(i).top;

            Snake.snakeTailList.get(i+1).left = L;
            Snake.snakeTailList.get(i+1).top = T;

        }
    }

    void drawMySnake(Canvas canvas)  {

        for(int i=0;i<Snake.snakeTailList.size();++i)
        {
            int L = Snake.snakeTailList.get(i).left;
            int T = Snake.snakeTailList.get(i).top;
            snakeReact = new Rect(L,T,L+snake.scale,T+snake.scale);
            canvas.drawRect(snakeReact, snakePaint);
        }


        drawMyFood(canvas);
        boolean foodEaten = didYouEatFood(canvas);
        if(foodEaten)
        {
            //draw its tail....
            int L = Snake.snakeTailList.get(Snake.snakeTailList.size()-1).left;
            int T = Snake.snakeTailList.get(Snake.snakeTailList.size()-1).top;

            Snake.snakeTailList.add(new SnakeTail(L,T));
            changeFoodLocation();
            growSize(canvas);
        }


        boolean endReached = isItEnd(canvas);
        if (endReached) {
            EventBus.getDefault().post(new MessageEvent(true));
        }else
        {
            sleepForAWhile();
        }
    }

    void sleepForAWhile()
    {

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    invalidate();
//                }
//            },500);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleepForAWhile();
                    Thread.sleep(500);
                    postInvalidate();
//                        createSnakeTail();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if(!thread.isAlive())
            thread.start();
    }


    public void startFresh(Canvas canvas) {

        snake.dir = DIR_RIGHT;
        snake.total = 1;
        Snake.snakeTailList.clear();
        Snake.snakeTailList.add(new SnakeTail(0,0));
        drawMyFood(canvas);
        drawMySnake(canvas);
    }

    public void moveLeft(Canvas canvas) {

        createSnakeTail();
        Snake.snakeTailList.get(0).left -= scale;
        drawMySnake(canvas);
    }


    public void moveRight(Canvas canvas) {
        createSnakeTail();
        Snake.snakeTailList.get(0).left += scale;
        drawMySnake(canvas);
    }

    public void moveUP(Canvas canvas) {
        createSnakeTail();
        Snake.snakeTailList.get(0).top -= scale;
        drawMySnake(canvas);
    }

    public void moveDown(Canvas canvas) {
        createSnakeTail();
        Snake.snakeTailList.get(0).top += scale;
        drawMySnake(canvas);
    }

    //increase tail
    void growSize(Canvas canvas) {
        ++snake.total;
    }

    boolean didYouEatFood(Canvas canvas)
    {
        boolean answer = false;

        if(Snake.snakeTailList.get(0).left == food.left && Snake.snakeTailList.get(0).top==food.top)
            answer = true;
        return answer;
    }

    void changeFoodLocation()
    {
        MyView.food.left = MyView.food.scale * new Random().nextInt(MyView.col+1);
        MyView.food.top = MyView.food.scale * new Random().nextInt(MyView.rows+1);
    }

    boolean isItEnd(Canvas canvas) {
        Rect windowRect = canvas.getClipBounds();
        boolean answer = false;

        int upCrash =  Snake.snakeTailList.get(0).top+scale,
                leftCrash = Snake.snakeTailList.get(0).left+scale,
                rightCrash = windowRect.right-Snake.snakeTailList.get(0).left,
                bottomCrash = windowRect.bottom - Snake.snakeTailList.get(0).top;
        if (
                upCrash < scale ||
                        leftCrash < scale ||
                        rightCrash < scale ||
                        bottomCrash < scale
                )
        {
            answer =  true;
        }
        return answer;
    }


    public MyView(Context context, MainActivity activity) {
        super(context);
        this.mainActivity = activity;
        init();
    }

    void drawMyFood(Canvas canvas)
    {
        foodReact = new Rect(food.left, food.top, food.left+food.scale,food.top+food.scale);
        canvas.drawRect(foodReact, foodPaint);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
