package com.example.banner.entity;

import android.view.View;

/**
 * Created by ymex on 2017/11/15.
 * About:
 */

public class ItemActionModel {
    private String title;
    private View.OnClickListener onClickListener;

    public ItemActionModel(String title) {
        this(title, null);
    }


    public ItemActionModel(String title, View.OnClickListener onClickListener) {
        this.title = title;
        this.onClickListener = onClickListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public ItemActionModel setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }
}
