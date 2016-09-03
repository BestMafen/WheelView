package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WheelView extends View {

    /**
     * 显示的数据
     */
    private ArrayList<String> mData;

    /**
     * 当前选择的位置
     */
    private int mPosition = -1;

    /**
     * 显示的个数,默认3
     */
    private int mCount = 5;

    /**
     * 字体大小(px)
     */
    private int mTextSize;

    /**
     * 字体的高度
     */
    private int mTextHeight;

    /**
     * y方向的的位移
     */
    private int mY;

    /**
     * item之间y方向的间隔
     */
    private int mSpace = 80;

    private int mDownY, mMoveY;

    private int mSpeed = 5;

    private int mWidth, mHeight;
    private Paint mPaint;
    private Paint.FontMetrics fm;

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context) {
        this(context, null);
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
        this.mY = 0;

        int size = mData.size();
        if (mData != null && size > 0) {
            mPosition = (int) Math.ceil(size / 2);
        }

        invalidate();
    }

    public void setPosition(int position) {
        this.mPosition = position;

        invalidate();
    }

    public void setPostion(String str) {
        int position = mData.indexOf(str);
        if (position != -1) {
            setPosition(position);
        } else {
            setPosition(0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mTextSize = (mHeight - (mCount + 1) * mSpace) / mCount;

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#123456"));
        fm = mPaint.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 3f * mSpace + 2f * mTextSize, mWidth, 3f * mSpace + 2f * mTextSize, mPaint);
        canvas.drawLine(0, 4f * mSpace + 3f * mTextSize, mWidth, 4f * mSpace + 3f * mTextSize, mPaint);
        if (mData != null && mData.size() > 0) {
            for (int i = mPosition - 3; i <= mPosition + 3; i++) {
                if (i < 0) {
                    canvas.drawText(mData.get(i + mData.size()), mWidth / 2,
                            (i - mPosition + 3) * (mSpace + mTextSize) - (fm.bottom - fm.leading) + mSpace / 2 + mY, mPaint);
                } else if (i > mData.size() - 1) {
                    canvas.drawText(mData.get(i - mData.size()), mWidth / 2,
                            (i - mPosition + 3) * (mSpace + mTextSize) - (fm.bottom - fm.leading) + mSpace / 2 + mY, mPaint);
                } else {
                    canvas.drawText(mData.get(i), mWidth / 2, (i - mPosition + 3) * (mSpace + mTextSize) - (fm.bottom - fm.leading) + mSpace / 2 + mY, mPaint);
                }
            }
        }
    }

    private Timer mTimer;
    private TimerTask mTask;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mDownY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveY = (int) event.getRawY();
                mY = mMoveY - mDownY;
                mPosition += (mDownY - mMoveY) / (mSpace + mTextSize);
                if (mPosition > mData.size() - 1) {
                    mPosition = 0;
                } else if (mPosition < 0) {
                    mPosition = mData.size() - 1;
                }

                if ((mDownY - mMoveY) > (mSpace + mTextSize)) {
                    mY = 0;
                    mDownY -= mSpace + mTextSize;
                } else if ((mMoveY - mDownY) > (mSpace + mTextSize)) {
                    mY = 0;
                    mDownY += mSpace + mTextSize;
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mTimer = new Timer();
                mTask = new TimerTask() {

                    @Override
                    public void run() {
                        if (mY == 0)
                            return;
                        if (mY > 0) {
                            if (mY > mSpeed) {
                                mY -= mSpeed;
                            } else {
                                mY -= mY;
                            }
                        } else {
                            if (mY < -mSpeed) {
                                mY += mSpeed;
                            } else {
                                mY += -mY;
                            }
                        }
                        postInvalidate();
                    }
                };
                mTimer.schedule(mTask, 0, 20);
                break;
        }
        return true;
    }
}