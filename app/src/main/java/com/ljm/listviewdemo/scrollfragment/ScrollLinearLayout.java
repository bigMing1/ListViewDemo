package com.ljm.listviewdemo.scrollfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;

import com.ljm.listviewdemo.R;

/**
 * Created by ljm on 2017/10/20.
 */

public class ScrollLinearLayout extends LinearLayout {
    private int mContentType = 1;
    private int mLayoutHeightMin = 0;
    private int mLayoutHeightMax = 0;
    private int mLayoutThresHold = 0;
    private int mCurrentScrollState = ScrollStateValue.SCROLL_MIN_STATE;
    private ViewGroup.LayoutParams mLayoutParams;
    private float mLastY = 0;
    private float mDeltY = 0;
    private float mLastX = 0;
    private float mDeltX = 0;
    private float mCurrentVel = 0;
    private OnChangeListener mListener;
    private float currentScrollOffset = 0;
    private int mContentViewState = ScrollStateValue.LISTVIEW_TOP_STATE;
    private ValueAnimator mTransitionAnim;
    private boolean mIsAnimPlay = false;
    private VelocityTracker mVelocityTracker;
    private PathInterpolator easeOutCubic;
    private PathInterpolator easeInCubic;

    public ScrollLinearLayout(Context context) {
        this(context, null);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init() {
        Resources res = getResources();
        mLayoutHeightMin = res
                .getDimensionPixelOffset(R.dimen.bottom_layout_min_height);
        mLayoutHeightMax = res
                .getDimensionPixelOffset(R.dimen.bottom_layout_max_height);
        mLayoutThresHold = res
                .getDimensionPixelOffset(R.dimen.bottom_layout_threshold_height);
        easeOutCubic = new PathInterpolator(0.215f, 0.61f, 0.355f, 1);
        easeInCubic = new PathInterpolator(0.55f, 0.055f, 0.675f, 0.19f);
    }

    public void setContentType(int contentType) {
        mContentType = contentType;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsAnimPlay
                || mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final VelocityTracker vt = mVelocityTracker;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                mDeltY = event.getY() - mLastY;
                handlerMove(mDeltY);
                vt.computeCurrentVelocity(1000);
                mCurrentVel = vt.getYVelocity();
                break;
            case MotionEvent.ACTION_UP:
                if (null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                startAnim(mCurrentVel);
                break;
            case MotionEvent.ACTION_CANCEL:
                if (null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsAnimPlay) {
            return true;
        }
        if (mContentType == ScrollStateValue.CONTENT_LISTVIEW) {
            return onListViewInterceptTouchEvent(ev);
        } else if (mContentType == ScrollStateValue.CONTENT_TABVIEW) {
            return onTabViewInterceptTouchEvent(ev);
        } else if (mContentType == ScrollStateValue.CONTENT_TABLISTVIEW) {
            return onTabListViewInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onListViewInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastY = ev.getY();
            mDeltY = 0;
        } else {
            mDeltY = ev.getY() - mLastY;
        }

        if (currentScrollOffset == ScrollStateValue.SCROLL_MIN_STATE) {
            return true;
        }

        if (currentScrollOffset == ScrollStateValue.SCROLL_MAX_STATE
                && mContentViewState == ScrollStateValue.LISTVIEW_TOP_STATE
                && mDeltY > 0) {
            return true;
        }
        if (mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTabViewInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastY = ev.getY();
            mLastX = ev.getX();
            mDeltX = 0;
            mDeltY = 0;
        } else {
            mDeltY = ev.getY() - mLastY;
            mDeltX = ev.getX() - mLastX;
        }
        if (Math.abs(mDeltY) > 10 && Math.abs(mDeltX) < 10) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTabListViewInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastY = ev.getY();
            mLastX = ev.getX();
            mDeltX = 0;
            mDeltY = 0;
        } else {
            mDeltY = ev.getY() - mLastY;
            mDeltX = ev.getX() - mLastX;
        }

        if (currentScrollOffset == ScrollStateValue.SCROLL_MAX_STATE
                && mContentViewState == ScrollStateValue.LISTVIEW_TOP_STATE
                && mDeltY > 0) {
            return true;
        }
        if (mContentViewState == ScrollStateValue.LISTVIEW_FLING_STATE) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void handlerMove(float delty) {
        mLayoutParams = getLayoutParams();
        mLayoutParams.height = (int) (mLayoutParams.height - delty);
        mLayoutParams.height = mLayoutParams.height > mLayoutHeightMax ? mLayoutHeightMax
                : mLayoutParams.height;
        mLayoutParams.height = mLayoutParams.height < mLayoutHeightMin ? mLayoutHeightMin
                : mLayoutParams.height;
        setLayoutParams(mLayoutParams);
        float state = (float) (mLayoutParams.height - mLayoutHeightMin) * 100
                / (mLayoutHeightMax - mLayoutHeightMin);

        if (mListener != null && currentScrollOffset != state) {
            mListener.onChange(state);
        }
        currentScrollOffset = state;
    }

    private void startAnim(float velocity) {
        mIsAnimPlay = true;
        int startValue = 0;
        int endValue = 0;
        if (Math.abs(velocity) < ScrollStateValue.SCROLL_VELOCITY_THRESHOLD) {
            if (mLayoutParams.height > mLayoutThresHold) {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMax;
            } else if (mLayoutParams.height <= mLayoutThresHold) {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMin;
            }
        } else {
            if (mCurrentScrollState == ScrollStateValue.SCROLL_MIN_STATE) {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMax;
            } else {
                startValue = mLayoutParams.height;
                endValue = mLayoutHeightMin;
            }
        }

        if (mTransitionAnim != null) {
            mTransitionAnim.cancel();
        }
        mTransitionAnim = ValueAnimator.ofInt(startValue, endValue);
        long duration = (long) ((float) Math.abs(startValue - endValue)
                / (float) Math.abs(mLayoutHeightMax - mLayoutThresHold) * ScrollStateValue.TRANSITION_DURATION_MAX);
        mTransitionAnim.setDuration(duration);
        mTransitionAnim.setInterpolator(easeOutCubic);
        mTransitionAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLayoutParams.height = (int) animation.getAnimatedValue();
                setLayoutParams(mLayoutParams);
                float state = (float) (mLayoutParams.height - mLayoutHeightMin)
                        * 100 / (mLayoutHeightMax - mLayoutHeightMin);

                if (mListener != null && currentScrollOffset != state) {
                    mListener.onChange(state);
                }
                currentScrollOffset = state;
                invalidate();
            }
        });
        mTransitionAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimPlay = false;
                mCurrentScrollState = (int) currentScrollOffset;
            }
        });
        mTransitionAnim.start();
    }

    public interface OnChangeListener {
        public void onChange(float state);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public void setContentViewState(int state) {
        mContentViewState = state;
    }
}
