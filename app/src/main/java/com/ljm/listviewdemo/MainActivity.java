package com.ljm.listviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ljm.listviewdemo.scrollfragment.ScrollActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView)findViewById(R.id.mainlayoutlist);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch ((int)arg3) {
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ScrollActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("顶部Tab页底部ListView demo实例");
        data.add("TAB页 demo实例");
        data.add("TAB页装载ListView demo实例");
        return data;
    }
}
