package co.jlabs.famb.chatBox;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by JLabs on 12/23/16.
 */

public class NonSwipeableViewPager extends ViewPager {
    private View mCurrentView;
    public NonSwipeableViewPager(Context context) {
        super(context);
        //setMyScroller();
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setMyScroller();
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    //down one is added for smooth scrolling

//    private void setMyScroller() {
//        try {
//            Class<?> viewpager = ViewPager.class;
//            Field scroller = viewpager.getDeclaredField("mScroller");
//            scroller.setAccessible(true);
//            scroller.set(this, new MyScroller(getContext()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }
    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }


//    public class MyScroller extends Scroller {
//        public MyScroller(Context context) {
//            super(context, new DecelerateInterpolator());
//        }
//
//        @Override
//        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
//        }
//    }
}