package cn.ymex.widget.banner.core;


/**
 * 自定义导航器
 */
public interface IndicatorAble {
    void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onBannerScrollStateChanged(int state);

    void onBannerSelected(int position, int size, Object object);

    void initIndicator(int size);
}