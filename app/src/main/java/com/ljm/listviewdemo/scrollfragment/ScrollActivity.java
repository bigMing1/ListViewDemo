package com.ljm.listviewdemo.scrollfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ljm.listviewdemo.R;

/**
 * Created by ljm on 2017/10/20.
 */

public class ScrollActivity extends FragmentActivity {
    private ScrollLinearLayout mScrollLinearLayout;
    private float viewLayoutWidth;
    private float viewLayoutHeight;
    private ListView mListView;
    private LinearLayout mTopLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_listview);
        mTopLinearLayout = (LinearLayout)findViewById(R.id.background_linearlayout);
        //适配不同分辨率机器，调整view宽高
        ViewTreeObserver vto2 = mTopLinearLayout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTopLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                viewLayoutWidth = mTopLinearLayout.getWidth();
                viewLayoutHeight = mTopLinearLayout.getHeight();
            }
        });

        mScrollLinearLayout = (ScrollLinearLayout)findViewById(R.id.bottomlinearlayout);
        mScrollLinearLayout.setContentType(ScrollStateValue.CONTENT_LISTVIEW);
        mScrollLinearLayout.setOnChangeListener(new ScrollLinearLayout.OnChangeListener() {
            @Override
            public void onChange(float state) {
                ViewGroup.LayoutParams mLayoutParams = mTopLinearLayout.getLayoutParams();

                mLayoutParams.height = (int) (viewLayoutHeight - dpToPixel(getApplicationContext(), state * 2));
                mLayoutParams.width = (int) viewLayoutWidth;
                mTopLinearLayout.setLayoutParams(mLayoutParams);
            }
        });
        //运行时权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        Cursor cursor = getContentResolver().query(Contacts.People.CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);
        mListView = (ListView)findViewById(R.id.listview);
        mListView.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_1,
                cursor,
                new String[]{Contacts.People.NAME},
                new int[]{android.R.id.text1}));
        mListView.setOnScrollListener(new ScrollPostionListener(new ScrollPostionListener.ScrollPositionCallback() {

            @Override
            public void excute(int state) {
                switch (state) {
                    case ScrollStateValue.LISTVIEW_TOP_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_TOP_STATE);
                        break;
                    case ScrollStateValue.LISTVIEW_BOTTOM_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_BOTTOM_STATE);
                        break;
                    case ScrollStateValue.LISTVIEW_FLING_STATE:
                        mScrollLinearLayout.setContentViewState(ScrollStateValue.LISTVIEW_FLING_STATE);
                        break;
                    default:
                        break;
                }
            }
        }));
    }

    public int dpToPixel(Context context, float dpValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * m + 0.5);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"You denied the premission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
