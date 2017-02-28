package co.jlabs.famb.fonts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class ButtonFont extends Button {


    public ButtonFont(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    public ButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    public ButtonFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/flaticon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}