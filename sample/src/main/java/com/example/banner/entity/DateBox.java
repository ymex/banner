package com.example.banner.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymex on 2017/9/10.
 */

public class DateBox {
    public static List<BanneModel> banneModels() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("http://doubii.naobutu.com/timg_1.jpg", "星空图-01"));
            add(new BanneModel("http://doubii.naobutu.com/timg_2.jpg", "星空图-02"));
            add(new BanneModel("http://doubii.naobutu.com/timg_3.jpg", "星空图-03"));
            add(new BanneModel("http://doubii.naobutu.com/timg_4.jpg", "星空图-04"));
            add(new BanneModel("http://doubii.naobutu.com/timg_5.jpg", "星空图-05"));
        }};
    }

    public static List<BanneModel> banne2Model() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("http://doubii.naobutu.com/timg_1.jpg", "星空图-01"));
            add(new BanneModel("http://doubii.naobutu.com/timg_2.jpg", "星空图-02"));
        }};
    }

    public static List<BanneModel> banne1Model() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("http://doubii.naobutu.com/timg_1.jpg", "星空图-01"));
        }};
    }
}
