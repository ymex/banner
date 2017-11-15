package com.example.banner.entity;

import android.view.View;

/**
 * Created by ymex on 2017/9/2.
 */

public class BanneModel {

    String url;
    String title ;
    View.OnClickListener onClickListener;

    public BanneModel() {

    }

    public BanneModel(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BanneModel setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
