package com.tecesind.oigo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rosember on 11/13/2015.
 */
public class MyViewPager extends ViewPager {


        public MyViewPager(Context context) {
            super(context);
        }

        public MyViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return false;
        }

}
