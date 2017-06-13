package com.wx.eturntable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class ETurntableMenuView extends ViewGroup{

    private int mRadius;
    private int mWidth;
    private int mHeight;

    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;

    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    private static final int FLINGABLE_VALUE = 300;

    private static final int NOCLICK_VALUE = 3;

    private int mFlingableValue = FLINGABLE_VALUE;

    private float mPadding;

    private double mStartAngle = 0;

    private String[] mItemTexts;

    private int[] mItemImgs;

    private int mMenuItemCount;

    private float mTmpAngle;

    private float mLastX;
    private float mLastY;

    private int mMenuItemLayoutId = R.layout.eturntable_menu_item;

    //尽量保证最大值为1，在布局里将图片文字设置大点，放大会失真
    private float mMaxSize = 1.0f;
    private float mMinSize = 0.6f;

    //拉伸成椭圆
    private float mStretchX = 1.6f;
    private float mStretchY = 0.6f;

    public ETurntableMenuView(Context context) {
        this(context,null);
    }

    public ETurntableMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ETurntableMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(0, 0, 0, 0);
    }

    //最好写固定值，没做兼容
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = Math.min(mWidth,mHeight);

        mStretchY = mHeight*1.0f/mWidth;
        mStretchX = mWidth*1.0f/mHeight;

        final int count = getChildCount();
        int childSize = (int) (mHeight * RADIO_DEFAULT_CHILD_DIMENSION);
        int childMode = MeasureSpec.EXACTLY;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);

            child.measure(makeMeasureSpec, makeMeasureSpec);
        }
        //mPadding = RADIO_PADDING_LAYOUT * mRadius;            //取消Padding
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int childCount = getChildCount();
        int left, top;
        int cWidth = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        float angleDelay = 360*1.0f /getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            float mMoveAngle = (float) ((mStartAngle - 90)+360)%360;

            if (((int)mMoveAngle)/180%2 == 0) {
                child.setScaleX((1-mMoveAngle/180)*(mMaxSize-mMinSize)+mMinSize);
                child.setScaleY((1-mMoveAngle/180)*(mMaxSize-mMinSize)+mMinSize);
            } else {
                child.setScaleX(((mMoveAngle%180)/180)*(mMaxSize-mMinSize)+mMinSize);
                child.setScaleY(((mMoveAngle%180)/180)*(mMaxSize-mMinSize)+mMinSize);
            }

            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            float tmp = mRadius / 2f - cWidth / 2 - mPadding;                  //剩余长度  toRadians 将角度转换为弧度
            left = (mWidth / 2 + (int) (Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)) )*mStretchX- 1 / 2f * cWidth));
            top = (mHeight / 2 + (int) (Math.round(tmp * Math.sin(Math.toRadians(mStartAngle)) )*mStretchY- 1 / 2f * cWidth));

            child.layout(left, top, left + cWidth, top + cWidth);

            mStartAngle += angleDelay;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mTmpAngle = 0;
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);
                float end = getAngle(x, y);
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += (end - start);
                    mTmpAngle += (end - start);
                } else {
                    mStartAngle += (start - end);
                    mTmpAngle += (start - end);
                }
                requestLayout();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:

                float angleDelay = 360 /getChildCount();
                autoFling(Math.round(mStartAngle/angleDelay)*angleDelay);

                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return true;
                }

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private ValueAnimator mAnimator;
    private void autoFling(float endAngle) {
        if (endAngle==mStartAngle) return;
        if (mAnimator == null) {
            mAnimator = new ValueAnimator();
        } else {
            mAnimator.cancel();
        }
        mAnimator.setIntValues();
        mAnimator.setFloatValues(((float)mStartAngle),endAngle);
        mAnimator.setDuration((long) Math.abs(endAngle-mStartAngle)*5);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float mCurAngle = (float) valueAnimator.getAnimatedValue();
                mStartAngle = mCurAngle;
                requestLayout();
            }

        });
        mAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mHeight / 2d);
        double y = yTouch - (mHeight / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mHeight / 2);
        int tmpY = (int) (y - mHeight / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;
        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }
        mMenuItemCount = resIds == null ? texts.length : resIds.length;
        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }
        addMenuItems();
    }

    private void addMenuItems() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            ImageView iv = (ImageView) view.findViewById(R.id.eturntable_item_image);
            TextView tv = (TextView) view.findViewById(R.id.eturntable_item_text);

            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }
            if (tv != null) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(mItemTexts[i]);
            }
            addView(view);
        }
    }

    public void setFlingableValue(int mFlingableValue) {
        this.mFlingableValue = mFlingableValue;
    }

    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    //监听
    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

    }
    private OnMenuItemClickListener mOnMenuItemClickListener;
    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

}
