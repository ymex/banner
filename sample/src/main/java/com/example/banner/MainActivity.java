package com.example.banner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.banner.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContent;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setAdapter(adapter = new RecyclerViewAdapter());
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        adapter.setDatas(recycleAdapterData());
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 1:
                        intent.setClassName(getPackageName(),DefaultBannerUserActivity.class.getName());
                        startActivity(intent);
                        break;
                    case 3://banner垂直方向滚动
                        adapter.setBannerDirection(true);
                        break;
                    case 4://banner水平方向滚动
                        adapter.setBannerDirection(false);
                        break;
                }
            }
        });
    }


    private List<String> recycleAdapterData() {
        return new ArrayList<String>() {{
            add("banner");
            add("默认banner使用");
            add("动画支持");
            add("banner垂直方向滚动");// 3
            add("banner水平方向滚动");// 4
            add("banner高级定制");
            add("默认指示器使用");
            add("自定义指示器");
            add("bann方法及参数介绍");
        }};
    }

}
