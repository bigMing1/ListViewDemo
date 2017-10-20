package com.ljm.listviewdemo.tabpagerlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljm.listviewdemo.R;

import java.util.Random;

/**
 * Created by ljm on 2017/10/20.
 */

public class MyFragment extends Fragment {
    private final CreatedCallback mListener;
    private int mLayoutResID;
    private int mFragmentIndex;
    private TextView mTextView;

    interface CreatedCallback{
        public void onCreatedView(int index);
    }

    public MyFragment(int index, int layoutResID, CreatedCallback callback) {
        mListener = callback;
        mLayoutResID = layoutResID;
        mFragmentIndex = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Random r = new Random(System.currentTimeMillis());

        View v = inflater.inflate(mLayoutResID, null);
        v.setBackgroundColor(r.nextInt() >> 8 | 0xFF << 24);

        mTextView = (TextView)v.findViewById(R.id.textview);

        mTextView.setTextColor(r.nextInt() >> 8 | 0xFF << 24);
        mTextView.setBackgroundColor(r.nextInt() >> 8 | 0xFF << 24);
        mTextView.setTextSize(30);
        mTextView.setText("Hello World!");
        mListener.onCreatedView(mFragmentIndex);
        return v;
    }

    public TextView getTextView() {
        return mTextView;
    }
}

