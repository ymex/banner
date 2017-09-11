package com.example.banner.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymex on 2017/9/10.
 */

public class DateBox {
    public static List<BanneModel> banneModels() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("http://7xrxcm.com2.z0.glb.qiniucdn.com/timg_1.jpg", "星空图-01"));
            add(new BanneModel("http://7xrxcm.com2.z0.glb.qiniucdn.com/timg_2.jpg", "星空图-02"));
            add(new BanneModel("http://7xrxcm.com2.z0.glb.qiniucdn.com/timg_3.jpg", "星空图-03"));
            add(new BanneModel("http://7xrxcm.com2.z0.glb.qiniucdn.com/timg_4.jpg", "星空图-04"));
            add(new BanneModel("http://7xrxcm.com2.z0.glb.qiniucdn.com/timg_5.jpg", "星空图-05"));
        }};
    }
}
