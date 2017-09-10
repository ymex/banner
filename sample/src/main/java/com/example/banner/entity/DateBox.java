package com.example.banner.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymex on 2017/9/10.
 */

public class DateBox {
    public static List<BanneModel> banneModels() {
        return new ArrayList<BanneModel>() {{
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-07/29/0_admin_upload_1501307922564.png", "推动合规建设"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-08/30/0_admin_upload_1504077511668.png", "会员体系大升级"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-06/27/0_admin_upload_1498548797754.jpg", "红包大派对"));
            add(new BanneModel("https://www.lejinsuo.com/dyupfiles/images/2017-08/11/0_admin_upload_1502445455975.png", "兑换抽奖"));
        }};
    }
}
