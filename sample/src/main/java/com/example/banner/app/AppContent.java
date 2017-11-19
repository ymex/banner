package com.example.banner.app;

import cn.ymex.kits.ApplicationContent;
import cn.ymex.kits.Kits;

/**
 * Created by ymex on 2017/11/19.
 * About:
 */

public class AppContent extends ApplicationContent {
    @Override
    public void onCreate() {
        super.onCreate();
        Kits.create(this);
    }
}
