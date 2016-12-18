package com.android.rahul.mycanvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkrde on 17-12-2016.
 */

public class Snake {
    int total =1;
    int left=0;
    int top=0;
    int scale = 100;
    int dir = MyView.DIR_RIGHT;
    public static List<SnakeTail> snakeTailList  = new ArrayList<>();
    public Snake() {
        snakeTailList.add(new SnakeTail(left,top));
    }
}
