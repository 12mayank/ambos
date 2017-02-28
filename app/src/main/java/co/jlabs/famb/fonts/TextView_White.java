package co.jlabs.famb.fonts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class TextView_White extends TextView {


    public TextView_White(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            //this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public TextView_White(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            //this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public TextView_White(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
           // this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}