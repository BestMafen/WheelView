package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WheelView wv;
    private ArrayList<String> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wv = (WheelView) findViewById(R.id.wv);
        for (int i = 1; i < 10; i++) {
            mList.add("" + i*1111111);
        }
        wv.setData(mList);
    }
}
