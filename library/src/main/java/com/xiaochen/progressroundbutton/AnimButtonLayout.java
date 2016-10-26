package com.xiaochen.progressroundbutton;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;


/**
 * Created by tanfujun on 10/26/16.
 */

public class AnimButtonLayout extends LinearLayout {

    private AnimDownloadProgressButton mDownloadProgressButton;
    private Drawable mShadowDrawable;//阴影
    private final int DEFAULT_COLOR = Color.GRAY;
    private TimeInterpolator mInterpolator;
    private ValueAnimator mLayoutDownAnimator;
    private ValueAnimator mLayoutUpAnimator;
    private float mDensity;
    private float mCenterX;
    private float mCenterY;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private float mCanvasScale = 1f;
    private final String PROPERTY_CANVAS_SCALE = "canvasScale";
    private final long ANIM_DOWN_DURATION = 128;
    private final long ANIM_UP_DURATION = 352;
    private float mTargetScale = 1.0f;
    private float mMinScale = 0.95f;

    public AnimButtonLayout(Context context) {
        super(context);
        mDownloadProgressButton = new AnimDownloadProgressButton(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDownloadProgressButton.setLayoutParams(lp);
        this.addView(mDownloadProgressButton);
        init(context, null);
    }

    public AnimButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mDownloadProgressButton = new AnimDownloadProgressButton(context, attrs);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDownloadProgressButton.setLayoutParams(lp);
        this.addView(mDownloadProgressButton);
    }


    private void init(Context context, AttributeSet attributeSet) {
        if (Build.VERSION.SDK_INT >= 21) {
            mInterpolator = new PathInterpolator(0.33f, 0f, 0.33f, 1);
        } else {
            mInterpolator = new AccelerateDecelerateInterpolator();
        }
        mShadowDrawable = getResources().getDrawable(R.drawable.gradient_layout_shadow);
        mDensity = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mCanvasScale, mCanvasScale, mCenterX, mCenterY);
        Log.w("tan", mCanvasScale + "");
        drawShadow(canvas);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    /**
     * 画阴影
     *
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        if (mShadowDrawable == null) {
            return;
        }
        //绘制阴影,阴影也会根据触摸事件进行旋转
        canvas.save();
        float scale = 1 - (1 - mCanvasScale) * 6;//scale最小是为0.7f
        canvas.scale(scale, scale, mCenterX, mCenterY);
        canvas.translate(0, (mCanvasScale - 1) * mLayoutHeight * 6 + mLayoutHeight * 0.4f + mDensity);
        mShadowDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (!isEnabled()) {
            return false;
        }
        if (!isClickable()) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(ev);
                Log.w("tan", "action down");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.w("tan", "onsize change");
        mLayoutWidth = w;
        mLayoutHeight = h;
        mCenterX = mLayoutWidth / 2;
        mCenterY = mLayoutHeight / 2;
        if (mShadowDrawable == null) {
            return;
        }
        mShadowDrawable.setColorFilter(DEFAULT_COLOR, PorterDuff.Mode.SRC_IN);
        mShadowDrawable.setBounds(0, 0, mLayoutWidth, mLayoutHeight);

        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
    }

    /**
     * 处理ActionDown事件
     *
     * @param ev
     */
    private void handleActionDown(MotionEvent ev) {
        setupLayoutDownAnimator();
        mLayoutDownAnimator.start();
    }

    /**
     * 处理handleActionUp事件
     *
     * @param ev
     */
    private void handleActionUp(MotionEvent ev) {
        setupLayoutUpAnimator();
        mLayoutUpAnimator.start();
    }

    /**
     * 点下去的动画
     */
    private void setupLayoutDownAnimator() {

        mLayoutDownAnimator = ValueAnimator.ofFloat(1f, 0.95f);
        mLayoutDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLayoutDownAnimator.setInterpolator(mInterpolator);
        mLayoutDownAnimator.setDuration(ANIM_DOWN_DURATION);
    }

    /**
     * 抬起手来的动画
     */
    private void setupLayoutUpAnimator() {

        mLayoutUpAnimator = ValueAnimator.ofFloat(0.95f, 1f);
        mLayoutUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasScale = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLayoutUpAnimator.setInterpolator(mInterpolator);
        mLayoutUpAnimator.setDuration(ANIM_UP_DURATION);

    }


    @Override
    public void invalidate() {
        super.invalidate();
        mDownloadProgressButton.invalidate();
    }

    public int getState() {
        return mDownloadProgressButton.getState();
    }

    public void setState(int state) {
        mDownloadProgressButton.setState(state);

    }

    /**
     * 设置按钮文字
     */
    public void setCurrentText(CharSequence charSequence) {
        mDownloadProgressButton.setCurrentText(charSequence);
    }


    /**
     * 设置带下载进度的文字
     */
    public void setProgressText(String text, float progress) {
        mDownloadProgressButton.setProgressText(text, progress);
    }

    public float getProgress() {
        return mDownloadProgressButton.getProgress();
    }

    public void setProgress(float progress) {
        mDownloadProgressButton.setProgress(progress);

    }

    /**
     * Sometimes you should use the method to avoid memory leak
     */
    public void removeAllAnim() {
        mDownloadProgressButton.removeAllAnim();
    }

    public float getButtonRadius() {
        return mDownloadProgressButton.getButtonRadius();
    }

    public void setButtonRadius(float buttonRadius) {
        mDownloadProgressButton.setButtonRadius(buttonRadius);
    }

    public int getTextColor() {
        return mDownloadProgressButton.getTextColor();
    }

    public void setTextColor(int textColor) {
        mDownloadProgressButton.setTextColor(textColor);
    }

    public int getTextCoverColor() {
        return mDownloadProgressButton.getTextCoverColor();
    }

    public void setTextCoverColor(int textCoverColor) {
        mDownloadProgressButton.setTextCoverColor(textCoverColor);
    }

    public int getMinProgress() {
        return mDownloadProgressButton.getMinProgress();
    }

    public void setMinProgress(int minProgress) {

        mDownloadProgressButton.setMinProgress(minProgress);
    }

    public int getMaxProgress() {
        return mDownloadProgressButton.getMaxProgress();
    }

    public void setMaxProgress(int maxProgress) {
        mDownloadProgressButton.setMaxProgress(maxProgress);
    }

    public void enabelDefaultPress(boolean enable) {
        mDownloadProgressButton.enabelDefaultPress(enable);
    }

    public void enabelDefaultGradient(boolean enable) {
        mDownloadProgressButton.enabelDefaultGradient(enable);
    }

    public void setTextSize(float size) {
        mDownloadProgressButton.setTextSize(size);
    }

    public float getTextSize() {
        return mDownloadProgressButton.getTextSize();
    }

    public AnimDownloadProgressButton setCustomerController(ButtonController customerController) {
        return mDownloadProgressButton.setCustomerController(customerController);
    }
}
