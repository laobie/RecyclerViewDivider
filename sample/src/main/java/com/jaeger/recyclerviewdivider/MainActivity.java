package com.jaeger.recyclerviewdivider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setAdapter(new ItemAdapter(20));
        RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this)
            .setOrientation(RecyclerViewDivider.VERTICAL)
            .setStyle(RecyclerViewDivider.Style.BETWEEN)
            .setColorRes(R.color.divider_gray)
            .setSize(1.5f)
            .setStartSkipCount(2)
            .setMarginLeft(72)
            .setMarginRight(8)
            .build();
        mRvList.addItemDecoration(divider);
    }
}
