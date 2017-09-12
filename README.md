[ ![Download](https://api.bintray.com/packages/ymex/maven/banner/images/download.svg) ](https://bintray.com/ymex/maven/banner/_latestVersion)

# banner

Android Viewpager rotation control, application guide page controls, support vertical, horizontal cycle scrolling, extended from view support animation, indicator extension and so on.


Android 轮播图控件、app引导页控件，支持垂直、水平循环滚动，扩展自viewpager 支持动画，指示器扩展等。<br>

1、app引导页控件<br>
2、轮播图控件<br>
![gif](https://github.com/ymex/banner/blob/master/art/GIF-d.gif)<br>
3、设置翻页动画<br>
![gif](https://github.com/ymex/banner/blob/master/art/GIF-a.gif)<br>
4、自定义指示器<br>
![gif](https://github.com/ymex/banner/blob/master/art/GIF-i.gif)<br>
5、垂直滚动<br>
![gif](https://github.com/ymex/banner/blob/master/art/GIF-v.gif)<br>

## 相关属性及方法

### banner部分方法
| 方法        | 解释   |
| --------   | :-----:  |
|createView()|创建banner 的布局|
|bindView()|处理banner控件元素|
|execute()|填充数据并展示|
|setOnClickBannerListener()|点击事件|
|setPageTransformer()|设置转换动画|
|setOrientation()|滚动方向|
|setIndicatorable()|设置指示器|


### banner属性


| 属性        | 解释   |
| --------   | :-----:  |
|banner_interval|滚动间隔 (默认5000ms)|
|banner_auto_play|是否自动播放 (默认true)|
|banner_loop|是否循环滚动 (默认true)|
|banner_orientation|horizontal(默认)，vertical|

### IndicatorLayout属性
| 属性        | 解释   |
| --------   | :-----:  |
|indicator_width|圆点的宽|
|indicator_height|圆点的高|
|indicator_margin|圆点的间距|
|indicator_selected|选中图片|
|indicator_unselected|未选中图片|




## 使用
banner基于viewpage 扩展，支持横向与纵向自动循环滚动。可用作 轮播图控件、app引导页控件。 
`需要 v7 的包支持`，并引入banner lib.

```
compile 'cn.ymex:banner:1.2.0'
```

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


