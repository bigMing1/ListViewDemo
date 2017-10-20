package com.ljm.listviewdemo.scrollfragment;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by ljm on 2017/10/20.
 */

public class ScrollPostionListener implements AbsListView.OnScrollListener{

    public interface ScrollPositionCallback {
        public void excute(int state);
    }
    private ScrollPositionCallback mCallback;
    public ScrollPostionListener (ScrollPositionCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getFirstVisiblePosition() == 0) {
                View v = view.getChildAt(0);
                int y = v.getTop();
                if (y == view.getTop()) {

                    mCallback.excute(ScrollStateValue.LISTVIEW_TOP_STATE);
                    return;
                }
            }
            else if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = view.getChildAt(view.getChildCount() - 1);

                int y = v.getBottom();
                if (y == view.getBottom()) {
                    mCallback.excute(ScrollStateValue.LISTVIEW_BOTTOM_STATE);
                    return;
                }
            }
            mCallback.excute(ScrollStateValue.LISTVIEW_FLING_STATE);
        }
    }
}
