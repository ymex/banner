package com.example.banner;

import android.content.Intent;
import android.net.Uri;
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
                    case 5:
                        intent.setClassName(getPackageName(), DefaultBannerActivity.class.getName());
                        break;
                    case 2:
                        intent.setClassName(getPackageName(), CustomBannerActivity.class.getName());
                        break;
                    case 3:
                        intent.setClassName(getPackageName(), VerticalBannerActivity.class.getName());

                        break;
                    case 4:
                        intent.setClassName(getPackageName(), AnimationBannerActivity.class.getName());

                        break;
                    case 6:
                        intent.setClassName(getPackageName(), IndicatorBannerActivity.class.getName());

                        break;

                    default:
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://github.com/ymex/banner");
                        intent.setData(content_url);
                        break;
                }
                startActivity(intent);
            }
        });
    }


    private List<String> recycleAdapterData() {
        return new ArrayList<String>() {{
            add("banner");
            add("默认banner使用");
            add("定制banner");
            add("banner滚动方向/垂直/水平");// 3

            add("动画支持");

            add("默认指示器使用");
            add("定制指示器");

            add("bann方法及参数介绍");
        }};
    }

}
