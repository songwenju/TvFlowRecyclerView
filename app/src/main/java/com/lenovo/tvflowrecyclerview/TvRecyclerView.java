package com.lenovo.tvflowrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author songwenju
 */

public class TvRecyclerView extends RecyclerView implements View.OnTouchListener {
    /**
     * RecyclerView距离父控件之间的距离
     */
    private static final int RECYCLER_VIEW_TO_PARENT = 155;
    /**
     * 上面预留阴影条的高度
     */
    private static final int UP_STRIP_HEIGHT = 70;
    /**
     * 下面预留阴影条默认的高度
     */
    private static final int BOTTOM_STRIP_HEIGHT = 20;
    private static final int MARGIN_FIFTEEN = 15;
    private static final int MARGIN_TWELVE = 12;
    private static final int MARGIN_THIRTY = 30;
    private int mMinSdkVersion = 19;
    private int mSelectedPosition = 0;
    private int mScreenHeight;
    private boolean mNeedAdjustScroll = false;
    private View mFocusView = null;
    private View mTopView;
    private View mBottomView;
    private boolean isUpPage = false;
    private boolean isNextPage = false;
    private boolean isFirst = true;
    private SparseIntArray map;
    private int dy = 0;
    private float downDy;
//    private OnTopLineListener mOnTopLineListener;

    public TvRecyclerView(Context context) {
        this(context, null);
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 一些初始化
     */
    private void init() {
        setClipChildren(false);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);
        // TODO: 17-12-29 放到外面
        setOnTouchListener(this);
        // TODO: 17-12-29 放到外面，和adapter解耦
//        initItemHeight();
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        LogUtil.i(this, "TvRecyclerView.init,mScreenHeight：" + mScreenHeight);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //静止状态 计算下面阴影条的高度并设置
                    calcBottomViewHeight();
                    //最后一行调整上面阴影条的高度否则默认60dp
                    calcTopViewHeight();
                    //设置下面热区（阴影区）view的显示规则
                    if (mBottomView != null && !isLastItemCompletelyVisible()) {
                        mBottomView.setVisibility(VISIBLE);
                        if (!mBottomView.hasFocus()) {
                            mBottomView.setBackgroundResource(R.drawable.mask);
                        }
                    } else if (mBottomView != null) {
                        mBottomView.setVisibility(GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //在recyclerView的move事件情况下，拦截调，只让它响应五向键和左右箭头移动
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = super.dispatchKeyEvent(event);
        //findFocus() 取到View/ViewGroup内部的焦点View,此处不能用getFocusedChild()
        View focusView = this.findFocus();
        mFocusView = focusView;
        LogUtil.i(this, "TvRecyclerView.dispatchKeyEvent.focusView:" + focusView);
        if (focusView == null) {
            return result;
        } else {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                LogUtil.i(this, "TvRecyclerView.dispatchKeyEvent.ActionUp");
                //如果return true的话，将被拦截，不能按back键返回
                return false;
            }
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    LogUtil.i(this, "TvRecyclerView.dispatchKeyEvent.left");
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                    if (leftView != null) {
                        leftView.requestFocus();
                        return true;
                    } else {
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                    if (rightView != null) {
                        rightView.requestFocus();
                        return true;
                    } else {
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    LogUtil.d(this, "TvRecyclerView.dispatchKeyEvent.down");
                    View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);

                    LogUtil.d(this, "TvRecyclerView.dispatchKeyEvent.down.downView:" + downView);
                    if (downView != null) {
                        downView.requestFocus();
                        //测量计算是否需要滑动 局部可见需要向下滑动，完全可见直接返回不处理
                        Rect locRect = new Rect();
                        int height = downView.getHeight();
                        if (downView.getLocalVisibleRect(locRect)) {
                            if ((locRect.bottom - locRect.top) >= height) {
                                return true;
                            }
                        }
                        //规律：如果是第一页的时候以第一行View为基准位置计算滑动的距离，其他为第二行的View为基准去计算
                        //计算规则：View在屏幕中bottom的位置-RecycleView到屏幕顶的间距-上面阴影View的高度-View自身的PaddingBottom
                        View view;
                        LogUtil.d(this, "TvRecyclerView.dispatchKeyEvent.firstCompleteVisible:" + isFirstItemCompletelyVisible());
                        int firstPosition = getFirstVisiblePosition();
                        LogUtil.i(this,"TvRecyclerView.dispatchKeyEvent.firstPosition:"+firstPosition);
                        if (isFirstItemCompletelyVisible()) {
                            view = getChildAt(0);
                        } else {
                            view = getChildAt(1);

                        }
//                        view = getChildAt(firstPosition);
                        LogUtil.d(this, "TvRecyclerView.dispatchKeyEvent.view:" + view);
                        if (view != null) {
                            Rect rect = new Rect();
                            view.getGlobalVisibleRect(rect);
                            dy = rect.bottom - RECYCLER_VIEW_TO_PARENT - UP_STRIP_HEIGHT - view.getPaddingBottom();
//                            dy = rect.bottom - rect.top - UP_STRIP_HEIGHT - view.getPaddingBottom();
                            LogUtil.d(this, "TvRecyclerView.dispatchKeyEvent.scroll.down.dy:" + dy);
                            this.smoothScrollBy(0, dy);
                        }
                        return true;
                    } else {
                        //极端默认处理获取到焦点即可，样式达不到理想效果
//                        if (isLastItemVisible()) {
//                            return result;
//                        }
                        mNeedAdjustScroll = true;
                        this.smoothScrollBy(0, mScreenHeight);
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_UP:
                    View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                    if (upView != null) {
                        upView.requestFocus();
                        //测量计算是否需要滑动 局部可见需要向上滑动，完全可见直接返回不处理
                        Rect locRect = new Rect();
                        int height = upView.getHeight();
                        if (upView.getLocalVisibleRect(locRect)) {
                            if ((locRect.bottom - locRect.top) >= height) {
                                return true;
                            }
                        }
                        //判断向上的View是否包含标题并计算向上移动的距离
                        //判断可见屏幕内的第一条item是否包含标题？包含间距是30：否则间距是24
                        //向上移动的规则是：第一条item的高度-可见的高度+间距+默认阴影的高度
//                        if (isIncludeTitle(getAdapter().getItemViewType(getFirstVisiblePosition()))) {
//                            dy = getFirstHeight() - getFirstVisibleHeight() + MARGIN_TWELVE * 2 + UP_STRIP_HEIGHT;
//                        } else {
                        dy = getFirstHeight() - getFirstVisibleHeight() + MARGIN_THIRTY*2 + UP_STRIP_HEIGHT;
//                        }
                        this.smoothScrollBy(0, -dy);
                        return true;
                    } else {
                        //极端默认处理获取到焦点即可，样式达不到理想效果
                        if (isFirstItemVisible()) {
                            return !isFirstItemVisible();
                        }
                        mNeedAdjustScroll = true;
                        this.smoothScrollBy(0, -mScreenHeight);
                        return !isFirstItemVisible();
                    }

                default:
                    break;
            }
        }
        return result;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        //设置上面阴影View可见规则：如果第一条View的高度可见范围大于70（默认预留的高度），则隐藏，否则显示
//        if (isFirstItemVisible() && getFirstVisibleHeight() > UP_STRIP_HEIGHT) {
//            if (mTopView != null) {
//                mTopView.setVisibility(GONE);
//            }
////            if (mOnTopLineListener != null) {
////                mOnTopLineListener.onTopLineVisible(false);
////            }
//        } else {
//            if (mTopView != null) {
//                mTopView.setVisibility(VISIBLE);
//            }
////            if (mOnTopLineListener != null) {
////                mOnTopLineListener.onTopLineVisible(true);
////            }
//        }
//        mBottomView.setVisibility(VISIBLE);
//        mBottomView.setBackground(null);
//        LogUtil.d(this,"TvRecyclerView.onScrolled,mNeedAdjustScroll:"+mNeedAdjustScroll);
        //不响应hover
//        if (mNeedAdjustScroll) {
        LogUtil.i(this,"TvRecyclerView.onScrolled.dy:"+dy);
        final View focusView = this.getFocusedChild();
//            View focusView;
//            if (mFocusView != null) {
//                focusView = mFocusView;
//            } else {
//                focusView = getFocusedChild();
//            }
//        LogUtil.e(this, "TvRecyclerView.onScrolled.focusView:" + focusView);
        if (focusView != null) {
            if (dy < 0) {
                View upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP);
                if (upView != null) {
                    upView.requestFocus();
                    upView.requestFocusFromTouch();
//                        mNeedAdjustScroll = false;
                    stopScroll();
                }
            } else if (dy > 0) {
                View downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN);
                if (downView != null) {
                    downView.requestFocus();
                    downView.requestFocusFromTouch();
//                        mNeedAdjustScroll = false;
                    stopScroll();
                }
            }
        }
//        }

    }

    /**
     * 计算下面阴影View（热区）的高度
     */
    private void calcBottomViewHeight() {
        View lastVisibleView = getChildAt(getChildCount() - 1);
        if (lastVisibleView != null) {
            Rect rect = new Rect();
            LogUtil.i(this, "TvRecyclerView.calcBottomViewHeight.paddingTop:" + lastVisibleView.getPaddingTop());

            if (lastVisibleView.getLocalVisibleRect(rect)) {
                int bottomHeight = rect.bottom - rect.top - lastVisibleView.getPaddingTop();
                setViewParams(mBottomView, bottomHeight);
            } else {
                setViewParams(mBottomView, BOTTOM_STRIP_HEIGHT);
            }
        } else {
            setViewParams(mBottomView, BOTTOM_STRIP_HEIGHT);
        }
    }

    /**
     * 计算上面阴影View（热区）的高度
     */
    private void calcTopViewHeight() {
        if (isLastItemCompletelyVisible()) {
            Rect rect = new Rect();
            View firstView = getChildAt(0);
            if (firstView.getLocalVisibleRect(rect)) {
                int height = rect.bottom - rect.top - firstView.getPaddingBottom();
                if (height > UP_STRIP_HEIGHT && !isFirstItemVisible()) {
                    setViewParams(mTopView, height);
                } else {
                    setViewParams(mTopView, UP_STRIP_HEIGHT);
                }
            }
        } else {
            setViewParams(mTopView, UP_STRIP_HEIGHT);
        }
    }

    /**
     * 处理拖动事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float distance;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downDy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                distance = Math.abs(event.getRawY() - downDy);
                if (distance > 0) {
                    int visibleHeight = getFirstVisibleHeight();
                    if (visibleHeight == UP_STRIP_HEIGHT) {
                        return false;
                    }
                    View view = getChildAt(0);
                    //判断第一个item是否可见，可见并且可见的高度大于View自身高度/2，则直接移动到最上面
                    //判断第一个可见的View是否完全可见，
                    //完全可见：如向下移动露出上面的view
                    //部分可见：判断可见的高度是否大于UP_STRIP_HEIGHT（阴影的高度）？大于向下移动：不大于向上移动
                    if (isFirstItemVisible() && visibleHeight > view.getHeight() / 2) {
                        dy = -mScreenHeight;
                    } else if (view.getHeight() == visibleHeight) {
                        Rect rect = new Rect();
                        int type = getAdapter().getItemViewType(getFirstVisiblePosition());
                        view.getGlobalVisibleRect(rect);
//                        if (isIncludeTitle(type)) {
//                            dy = MARGIN_TWELVE * 2 + UP_STRIP_HEIGHT;
//                        } else {
                        dy = MARGIN_THIRTY + UP_STRIP_HEIGHT;
//                        }
                        //此处有一个bug  rect.top为负值  移动距离计算会出错
                        int margin = rect.top - RECYCLER_VIEW_TO_PARENT;
                        dy = -(dy - margin);
                    } else {
                        if (visibleHeight > UP_STRIP_HEIGHT) {
                            dy = (visibleHeight - UP_STRIP_HEIGHT);
                        } else {
                            dy = -(UP_STRIP_HEIGHT - visibleHeight);
                        }
                    }
                    this.smoothScrollBy(0, dy);
                    return true;
                }
                break;

            default:
                break;
        }
        return false;
    }

//    /**
//     * 记录每一个模板的高度
//     */
//    private void initItemHeight() {
//        map = new SparseIntArray();
//        map.put(ITEM_TYPE_ONE, 312);
//        map.put(ITEM_TYPE_TWO, 312);
//        map.put(ITEM_TYPE_THREE, 312);
//        map.put(ITEM_TYPE_FOUR, 312);
//        map.put(ITEM_TYPE_FIVE, 312);
//        map.put(ITEM_TYPE_SIX, 312);
//        map.put(ITEM_TYPE_SEVEN, 200);
//        map.put(ITEM_TYPE_EIGHT, 130);
//        map.put(ITEM_TYPE_NINE, 654);
//        map.put(ITEM_TYPE_TEN, 654);
//        map.put(ITEM_TYPE_ELEVEN, 654);
//        map.put(ITEM_TYPE_TWELVE, 654);
//        map.put(ITEM_TYPE_THIRTEEN, 654);
//        map.put(ITEM_TYPE_FOURTEEN, 654);
//        map.put(ITEM_TYPE_FIFTEEN, 654);
//        map.put(ITEM_TYPE_SIXTEEN, 654);
//        map.put(ITEM_TYPE_SEVENTEEN, 654);
//        map.put(ITEM_TYPE_EIGHTEEN, 750);
//        map.put(ITEM_TYPE_NINETEEN, 750);
//        map.put(ITEM_TYPE_TWENTY, 445);
//    }
//
//    public int getItemHeight(int type) {
//        return map.get(type);
//    }

//    /**
//     * 模板类型是否包含标题
//     *
//     * @param type 模板的类型
//     * @return true包含标题 false不包含标题
//     */
//    public boolean isIncludeTitle(int type) {
//        return !(type == ITEM_TYPE_TWO || type == ITEM_TYPE_TWELVE);
//    }

    /**
     * 向上翻页
     */
    public void upPage() {
        isUpPage = true;
        if (isFirstItemVisible() && getFirstVisibleHeight() > UP_STRIP_HEIGHT) {
            dy = mScreenHeight;
        } else {
            //规律：移动到屏幕可见第一条item上面的第二个item，并且留出默认的高度
            //计算规则：可见第一条item的高度+上一条item的高度-减去第一条item可见的高度+item之间的间距+默认上面阴影view的高度（默认预留显示的高度）
            int pos = getFirstVisiblePosition();
            int type1 = getAdapter().getItemViewType(pos);
            int type2 = getAdapter().getItemViewType(pos - 1);
//            int d1 = getItemHeight(type1);
//            int d2 = getItemHeight(type2);
//            dy = d1 + d2 - getFirstVisibleHeight() + UP_STRIP_HEIGHT;
//            if (isIncludeTitle(type1)) {
//                dy += MARGIN_TWELVE * 2;
//            } else {
//                dy += MARGIN_THIRTY;
//            }
//            if (isIncludeTitle(type2)) {
//                dy += MARGIN_TWELVE * 2;
//            } else {
//                dy += MARGIN_THIRTY;
//            }
        }
        this.smoothScrollBy(0, -dy);
    }

    /**
     * 向下翻页
     */
    public void nextPage() {
        isNextPage = true;
        //规律：屏幕可见范围倒数第二个view移动到屏幕顶端，并且预留默认的高度
        //计算规则：可见范围倒数第二个view在屏幕中的bottom位置-RecyclerView到屏幕顶端的高度-默认预留的高度
        View view = getChildAt(getChildCount() - 2);
        Rect rect = new Rect();
        if (view != null) {
            view.getGlobalVisibleRect(rect);
            dy = rect.bottom - RECYCLER_VIEW_TO_PARENT - UP_STRIP_HEIGHT;
        } else {
            //默认为一屏幕
            dy = mScreenHeight - RECYCLER_VIEW_TO_PARENT;
        }
        this.smoothScrollBy(0, dy);
    }

    /**
     * 上下阴影View平移动画
     *
     * @param mode 平移的类型 0:bottomView  up translate 1:bottomView  down translate
     *             2:upView  down translate   3:upView  up translate
     */
    public void translation(int mode) {
//        View view;
//        if (mode == 0 || mode == 1) {
//            view = getChildAt(getChildCount() - 1);
//        } else {
//            view = getChildAt(0);
//        }
//        if (view == null) {
//            return;
//        }
//        int pos = getLayoutManager().getPosition(view);
//        int type = getAdapter().getItemViewType(pos);
////        switch (mode) {
//            //bottomView  up translate
//            case 0:
//                if (isIncludeTitle(type)) {
//                    //图片View到模板上面的高度是50，设计要求到字体的中间所以31=12（间距）+19（字体高度的/2）
//                    dy = -VodUtil.dip2px(31);
//                } else {
//                    //间距为24  设计没有要求自己试的值为21
//                    dy = -VodUtil.dip2px(21);
//                }
//                break;
//            //bottomView  down translate
//            case 1:
//                dy = 0;
//                break;
//            //upView  down translate
//            case 2:
//                int position = getFirstVisiblePosition() + 1;
//                if (isIncludeTitle(getAdapter().getItemViewType(position))) {
//                    //间距为24  移动到间距的一半
//                    dy = MARGIN_TWELVE;
//                } else {
//                    //间距为30  移动到间距的一半
//                    dy = MARGIN_FIFTEEN;
//                }
//                break;
//            //upView  up translate
//            case 3:
//                dy = 0;
//                break;
//            default:
//                break;
//        }
//        int id = -1;
//        LogUtil.i(this,"TvRecyclerView.translation.type:" + type);
//        switch (type) {
//
//            case ITEM_TYPE_ONE:
//                AnimationUtil.getInstance().translationY(view.findViewById(R.id.type9_image_one), dy);
//                break;
//            case ITEM_TYPE_TWO:
//                id = R.id.layout_two;
//                break;
//            case ITEM_TYPE_THREE:
//                id = R.id.layout_three;
//                break;
//            case ITEM_TYPE_FOUR:
//                id = R.id.layout_four;
//                break;
//            case ITEM_TYPE_FIVE:
//                id = R.id.layout_five;
//                break;
//            case ITEM_TYPE_SIX:
//                id = R.id.layout_six;
//                break;
//            case ITEM_TYPE_SEVEN:
//                id = R.id.layout_seven;
//                break;
//            case ITEM_TYPE_EIGHT:
//                id = R.id.layout_eight;
//                break;
//            case ITEM_TYPE_NINE:
//                id = R.id.layout_nine;
//                break;
//            case ITEM_TYPE_TEN:
//                id = R.id.layout_ten;
//                break;
//            case ITEM_TYPE_ELEVEN:
//                id = R.id.layout_eleven;
//                break;
//            case ITEM_TYPE_TWELVE:
//                id = R.id.layout_twelve;
//                break;
//            default:
//                break;
//        }
//        if (type != ITEM_TYPE_ONE && id != -1) {
//            AnimationUtil.getInstance().translationY(view.findViewById(id), dy);
//        }
    }

    /**
     * 上下翻页获取焦点  暂时没有用到看设计关于焦点的要求
     */
    private void flipViewGetFocus() {
        View focusView = getFocusedChild();
        if (isUpPage) {
            View upView = FocusFinder.getInstance().findNextFocus(TvRecyclerView.this, focusView, View.FOCUS_UP);
            if (upView != null) {
                upView.requestFocus();
                upView.requestFocusFromTouch();
            }
            isUpPage = false;
            return;
        }
        if (isNextPage) {
            View downView = FocusFinder.getInstance().findNextFocus(TvRecyclerView.this, focusView, View.FOCUS_DOWN);
            if (downView != null) {
                downView.requestFocus();
                downView.requestFocusFromTouch();
            }
            isNextPage = false;
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        //进入上下阴影view的区域，拦截Hover事件，否则交给父类处理。此处理解决空鼠移动RecyclerView上下移动
        return isIntoShadowRect(event) || super.onInterceptHoverEvent(event);
    }

    /**
     * 是否进入上下阴影View的区域
     *
     * @param event MotionEvent
     * @return 进入上下阴影区域返回true 没有进入返回false
     */
    private boolean isIntoShadowRect(MotionEvent event) {
        float dy = event.getRawY();
        View firstView = getChildAt(0);
        View lastView = getChildAt(getChildCount() - 1);
        if (firstView != null) {
            Rect rect = new Rect();
            firstView.getGlobalVisibleRect(rect);
            if (!(isFirstItemVisible() && (rect.bottom - RECYCLER_VIEW_TO_PARENT) > UP_STRIP_HEIGHT)) {
                if (dy < rect.bottom) {
                    return true;
                }
            }
        }
        if (lastView != null) {
            Rect rect = new Rect();
            lastView.getGlobalVisibleRect(rect);
            if (!isLastItemCompletelyVisible()) {
                if (dy > rect.top) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //第一次View可见的时候，设置下面阴影View的高度
        if (hasWindowFocus && isFirst) {
            isFirst = true;
            if (mBottomView != null) {
                mBottomView.setVisibility(VISIBLE);
                mBottomView.setBackgroundResource(R.drawable.mask);
            }
            calcBottomViewHeight();
        }
    }


    @Override
    public boolean onHoverEvent(MotionEvent event) {
        mNeedAdjustScroll = false;
        return super.onHoverEvent(event);
    }

    /**
     * 获取第一条可见view的高度
     *
     * @return Height
     */
    private int getFirstVisibleHeight() {
        Rect rect = new Rect();
        View view = getChildAt(0);
        if (view != null) {
            view.getLocalVisibleRect(rect);
        }
        return rect.bottom - rect.top;
    }

    /**
     * 获取第一条view的高度
     *
     * @return Height
     */
    private int getFirstHeight() {
        View view = getChildAt(0);
        return view != null ? view.getHeight() : 0;
    }


    /**
     * 设置View的高度
     *
     * @param view   View
     * @param height 设置的高度
     */
    private void setViewParams(View view, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 获取第一条可见View的Position
     */
    public int getFirstVisiblePosition() {
        int firstVisiblePos = 0;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                firstVisiblePos = ((GridLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                firstVisiblePos = ((LinearLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] pos = new int[2];
                pos = ((StaggeredGridLayoutManager) layoutManager)
                        .findFirstVisibleItemPositions(pos);
                firstVisiblePos = pos[0];
            }
        }
        return firstVisiblePos;
    }

    /**
     * 第一个条目是否可见
     *
     * @return 可见返回true，不可见返回false
     */
    public boolean isFirstItemVisible() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int position = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            return position <= 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = new int[2];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).
                    findFirstVisibleItemPositions(firstVisibleItems);
            int position = firstVisibleItems[0];
            return position == 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            return position <= 0;
        }
        return false;
    }

    /**
     * 第一条Item是否完全可见
     *
     * @return 完全可见 true 不可见或者部分可见false
     */
    public boolean isFirstItemCompletelyVisible() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int position = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            return position <= 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = new int[2];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).
                    findFirstCompletelyVisibleItemPositions(firstVisibleItems);
            int position = firstVisibleItems[0];
            return position == 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            return position <= 0;
        }
        return false;
    }

    /**
     * 最后一条Item是否完全可见
     *
     * @return 完全可见 true 不可见或者部分可见false
     */
    public boolean isLastItemCompletelyVisible() {
        LayoutManager layoutManager = getLayoutManager();
        int count = getAdapter().getItemCount() - 1;
        if (layoutManager instanceof GridLayoutManager) {
            int position = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            return position >= count;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = new int[2];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).
                    findLastCompletelyVisibleItemPositions(firstVisibleItems);
            int position = firstVisibleItems[0];
            return position >= count;
        } else if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            return position >= count;
        }
        return false;
    }

    /**
     * 最后一条目是否可见
     *
     * @return 可见返回true，不可见返回false
     */
    public boolean isLastItemVisible() {
        LayoutManager layoutManager = getLayoutManager();
        int count = getAdapter().getItemCount() - 1;
        if (layoutManager instanceof GridLayoutManager) {
            int position = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            return position >= count;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] firstVisibleItems = new int[2];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).
                    findLastVisibleItemPositions(firstVisibleItems);
            int position = firstVisibleItems[0];
            return position >= count;
        } else if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            return position >= count;
        }
        return false;
    }

    @Override
    public boolean isInTouchMode() {
        boolean result = super.isInTouchMode();
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == mMinSdkVersion) {
            return !(hasFocus() && !result);
        } else {
            return result;
        }
    }

    @Override
    public void onDraw(Canvas c) {
        mSelectedPosition = getChildAdapterPosition(getFocusedChild());
        super.onDraw(c);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = mSelectedPosition;
        if (position < 0) {
            return i;
        } else {
            if (i == childCount - 1) {
                if (position > i) {
                    position = i;
                }
                return position;
            }
            if (i == position) {
                return childCount - 1;
            }
        }
        return i;
    }

//    public void setOnTopLineListener(OnTopLineListener mOnTopLineListener) {
//        this.mOnTopLineListener = mOnTopLineListener;
//    }

    public void setTopView(View topView) {
        mTopView = topView;
    }

    public void setBottomView(View bottomView) {
        mBottomView = bottomView;
    }


}
