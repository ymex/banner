package com.example.banner.entity;

/**
 * Created by ymex on 2017/9/2.
 */

public class BanneModel {
    String url;
    String title ;

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
}
