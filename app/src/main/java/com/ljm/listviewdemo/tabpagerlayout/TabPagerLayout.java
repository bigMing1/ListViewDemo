package com.ljm.listviewdemo.tabpagerlayout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabWidget;

import com.ljm.listviewdemo.R;

/**
 * Created by ljm on 2017/10/20.
 */

public class TabPagerLayout extends LinearLayout {
    private TabWidget mTabWidget;
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private MyFragment[] mFragments;
    private String[] mTitleName = { "first", "second", "third" };
    private Button[] mButtons = new Button[mTitleName.length];
    private int[] mButtonColors = {Color.BLUE, Color.RED, Color.YELLOW};
    private int[] mLayoutResId = { R.layout.fragment_viewpager_layout,
            R.layout.fragment_viewpager_layout,
            R.layout.fragment_viewpager_layout };

    private OnClickListener mTabClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mButtons[0]) {
                mViewPager.setCurrentItem(0);
            }
            if (v == mButtons[1]) {
                mViewPager.setCurrentItem(1);
            }
            if (v == mButtons[2]) {
                mViewPager.setCurrentItem(2);
            }
        }
    };
    public TabPagerLayout(Context context) {
        this(context, null);
    }

    public TabPagerLayout(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TabPagerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tab_pager_layout, this);
        setupFragment();
        setupTabWidget(context);
        setupViewPager(context);
    }

    private void setupFragment() {
        mFragments = new MyFragment[mTitleName.length];
        for (int i = 0; i < mTitleName.length; i++) {
            mFragments[i] = new MyFragment(i, mLayoutResId[i],
                    mListenerCallback);
        }
    }

    private void setupTabWidget(Context context) {
        mTabWidget = (TabWidget)findViewById(R.id.tabwidget);
        for (int i = 0; i < mButtons.length; i ++) {
            mButtons[i] = new Button(context);
            mButtons[i].setFocusable(true);
            mButtons[i].setText(mTitleName[i]);
            mButtons[i].setTextColor(mButtonColors[i]);
            mTabWidget.addView(mButtons[i]);
            mButtons[i].setOnClickListener(mTabClickListener);
        }
        mTabWidget.setCurrentTab(0);
        mTabWidget.setStripEnabled(false);
    }

    private void setupViewPager(Context context) {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new MyPagerAdapter(((FragmentActivity) context).getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(0);
    }

    private MyFragment.CreatedCallback mListenerCallback = new MyFragment.CreatedCallback() {

        @Override
        public void onCreatedView(int index) {
            //初始化加载fragment中的内容
        }
    };



    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public MyFragment getItem(int index) {
            return mFragments[index];
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            mTabWidget.setCurrentTab(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}
