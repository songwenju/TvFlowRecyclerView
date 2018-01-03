package com.lenovo.tvflowrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * @author songwenju
 */
@SuppressLint("NewApi")
public class FocusRoundImageView extends RoundedImageView implements View.OnHoverListener {
    private float borderWidth = 0.0f;
    private float defaultBorderWidth = 0.0f;

    public FocusRoundImageView(Context context) {
        this(context, null);
    }

    public FocusRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    protected void init(Context context) {
        borderWidth = getResources().getDimensionPixelSize(R.dimen.border);
        setOnHoverListener(this);
        setBorderColor(ContextCompat.getColor(context, R.color.borderBlueColor));
        setCornerRadius(7);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        LogUtil.e(this,"onFocusChanged,gainFocus:"+gainFocus);

        if (gainFocus) {
            setBorderWidth(borderWidth);
            AnimationUtil.getInstance().zoomIn(this,getScaleSize());
        } else {
            setBorderWidth(defaultBorderWidth);
            AnimationUtil.getInstance().zoomOut(this);
        }
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                LogUtil.e(this,"FocusRoundImageView.onHover.ACTION_HOVER_ENTER");
                requestFocusFromTouch();
                requestFocus();
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                break;
            default:
                break;
        }
        return false;
    }
}