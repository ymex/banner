# banner

Android Viewpager rotation control, application guide page controls, support vertical, horizontal cycle scrolling, extended from view support animation, indicator extension and so on.


Android 轮播图控件、app引导页控件，支持垂直、水平循环滚动，扩展自viewpager 支持动画，指示器扩展等。

## 用途
banner基于viewpage 扩展，支持横向与纵向自动循环滚动。

## 简单使用
需要 v7 的包支持。
1、在布局文件中加入控件,IndicatorLayout 是指示器布局,你可以随意定义其位置。如果 不使用指示器移除它就可以了。
```
<cn.ymex.banner.Banner
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="240dip"
    android:background="@color/colorAccent"
    app:banner_auto_play="true"
    app:banner_interval="5000">

    <cn.ymex.banner.IndicatorLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:layout_marginBottom="8dip"
        android:gravity="center"
        app:indicator_height="8dip"
        app:indicator_width="8dip" />
</cn.ymex.banner.Banner>
```

2、使用bindview加载图片资源到banner中，banner默认实现了基于AppCompatImageView的布局。

```
banner.bindView(new Banner.BindViewCallBack<AppCompatImageView,BanneModel>() {
    @Override
    public void bindView(AppCompatImageView view, BanneModel data, int position) {
        //图片加载 
        Glide.with(view.getContext())
                .load(data.getUrl())
                .into(view);
    }
}).execute(data());
```

## 自定义
