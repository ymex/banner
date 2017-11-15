package cn.ymex.widget.banner.core;


import android.support.v4.view.ViewPager;

/**
 * 自定义导航器
 */
public interface IndicatorAble {
    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels);

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    void onBannerScrollStateChanged(int state);

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position  Position index of the new selected page.
     * @param size page size
     * @param object page data
     */
    void onBannerSelected(int position, int size, Object object);

    /**
     * 初始化
     * @param size size
     */
    void initIndicator(int size);
}