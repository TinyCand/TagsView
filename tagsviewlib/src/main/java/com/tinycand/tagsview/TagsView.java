package com.tinycand.tagsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.tinycand.tagsview.bean.TagLocationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * TagsView is a ViewGroup, which displays items in a two-dimensional, like GridView,
 * but the length of item is uncertain. <br/>
 * Only support one itemType.<br/>
 * TagsView是一个可以自动换行的ViewGroup, 用于展示不定长的item, 用法类似于GridView,
 * 可用来展示人物或物品的标签.<br/>
 * 只支持一种itemType<br/>
 * Created by TinyCand on 2018/4/5.
 * Email: TinyCand@gmail.com
 */

public class TagsView extends AdapterView<ListAdapter> {
    private static boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "TagsView";

    public static final int NO_STRETCH = 0;
    /**
     * Stretches the spacing between columns.
     */
    public static final int STRETCH_SPACING = 1;
//    public static final int STRETCH_COLUMN_WIDTH = 2;
//    public static final int STRETCH_SPACING_UNIFORM = 3;
    public static final int STRETCH_SPACING_AUTO = 5;

    /**
     * StretchMode like GridView<br/>
     * 拉伸模式，就像GridView
     */
    private int mStretchMode = NO_STRETCH;


    /**
     * 左间距
     */
    private int paddingLeft = 0;
    /**
     * 右间距
     */
    private int paddingRight = 0;
    /**
     *
     */
    private int paddingTop = 0;
    /**
     *
     */
    private int paddingBottom = 0;

    /**
     * The amount of horizontal space between items<br/>
     * 水平方向间距
     */
    private int horizontalSpace = 10;
    /**
     * The amount of vertical space between items<br/>
     * 行间距
     */
    private int verticalSpace = 5;

    /**
     * Holds a reference to the adapter bound to this view
     */
    private ListAdapter mAdapter;

    private List<ArrayList<TagLocationInfo>> mTagsLocLineList = new ArrayList<>();

    private ArrayList<TagLocationInfo> mTagsLocList = new ArrayList<>();


    public TagsView(Context context) {
        super(context);

    }

    public TagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TagsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.TagsView);
        int attrCount = attrArray.getIndexCount();
        if (DEBUG) Log.d(TAG, "attrCount : " + attrCount);
        for (int i = 0; i < attrCount; i++) {
            int attrId = attrArray.getIndex(i);
            if (attrId == R.styleable.TagsView_horizontalSpacing) {
                float dimen = attrArray.getDimension(attrId, 0);
                horizontalSpace = (int) dimen;

            } else if (attrId == R.styleable.TagsView_verticalSpacing) {
                float dimen = attrArray.getDimension(attrId, 0);
                verticalSpace = (int) dimen;

            } else if (attrId == R.styleable.TagsView_stretchMode) {
                mStretchMode = attrArray.getInt(attrId, NO_STRETCH);
            }

        }
        attrArray.recycle();

        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        String logFormation = String.format("--- onMeasure() widthMode %d, widthMeasure %d; heightMode %d, heightMeasure %d", widthMode, widthMeasure, heightMode, heightMeasure);
        if (DEBUG) Log.v(TAG, logFormation);
        mTagsLocList.clear();
        mTagsLocLineList.clear();

        int childCount = null == mAdapter ? 0 : mAdapter.getCount();
//        if (childCount <= 0) {
//            //XXX
//        }

        int childStartX = paddingLeft;// 横坐标开始
        int childStartY = paddingTop;// 纵坐标开始
        int childPredictEndX = childStartX;// 用来记录 估算childView的右边的x坐标

//        int maxAvailableWidth = Math.max(0, widthMeasure - paddingLeft - paddingRight);
        /* 临时记录每一行的高度 */
        int perLineHeight = 0;
//        int maxLineWidth = 0;
        int maxChildEndX = childStartX;
        /* 最后一行子view下边框坐标Y */
        int childrenEndY = childStartY;
        int rowCount = 1;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (null == childView) {
                if (DEBUG) Log.w(TAG, "----- onMeasure childView is NULL !");
                childView = mAdapter.getView(i, null, this);
                addChild(childView, i);
            }
//            if (View.GONE == childView.getVisibility()) {
//                //XXX we have not deal with it yet
//            }

            //XXX child matchParent
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            /* 获取子控件Child的宽高, XXX margin */
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            childPredictEndX = childStartX + childWidth;
            if (childPredictEndX > widthMeasure - paddingLeft) {
                if (childStartX > paddingLeft) {
                    /** 换行，没有再进行判断 childPredictEndX(结束位置)是否超过了父控件
                     */
//                    maxChildEndX = Math.max(maxChildEndX, childStartX - horizontalSpace);
                    childStartX = paddingLeft;
                    childStartY += perLineHeight + verticalSpace;
                    rowCount++;

                    perLineHeight = 0;//Math.max(0, childHeight);
                } else {
                    /* do nothing.
                     * 如果 childStartX <= paddingLeft，则说明一行根本显示不全该子view,
                     * startOffsetX < paddingLeft 是不存在的
                     */
                }
            } else {
            }
            perLineHeight = Math.max(perLineHeight, childHeight);
            childPredictEndX = childStartX + childWidth;
            maxChildEndX = Math.max(maxChildEndX, childPredictEndX);

            if (DEBUG)
                Log.d(TAG, "----- measure child :" + childStartX + ", " + childStartY + ", " + childPredictEndX + ", " + (childStartY + childHeight));

            TagLocationInfo tagLocationInfo = new TagLocationInfo(childStartX, childStartY);
            tagLocationInfo.setEndX(childPredictEndX);
            tagLocationInfo.setEndY(childStartY + childHeight);
            mTagsLocList.add(tagLocationInfo);

            ArrayList<TagLocationInfo> tagLocationInfoList;
            if (rowCount > mTagsLocLineList.size() || null == mTagsLocLineList.get(rowCount - 1)) {
                tagLocationInfoList = new ArrayList<>();
                mTagsLocLineList.add(tagLocationInfoList);
            } else {
                tagLocationInfoList = mTagsLocLineList.get(rowCount - 1);
            }
            tagLocationInfoList.add(tagLocationInfo);

            childStartX = childStartX + childWidth + horizontalSpace;
            if (i == childCount - 1) {
                childrenEndY = childStartY + perLineHeight;
            }

        }
        if (DEBUG) {
            for (int i = 0; i < mTagsLocList.size(); i++) {
                TagLocationInfo tagLocationInfo = mTagsLocList.get(i);
                Log.i(TAG, "----- onMeasure i :" + i + " ," + tagLocationInfo.startX);
            }
        }

        if (DEBUG) Log.i(TAG, "---- maxChildEndX : " + maxChildEndX);
        int finalTagsViewWidth = (widthMode == MeasureSpec.EXACTLY ? widthMeasure : maxChildEndX + /*getPaddingLeft() + */paddingRight);
        int finalTagsViewHeight = (heightMode == MeasureSpec.EXACTLY ? heightMeasure : childrenEndY + /*getPaddingTop() + */paddingBottom);
        if (STRETCH_SPACING == mStretchMode) {
            arrangeAllLine(mTagsLocLineList, finalTagsViewWidth, false);
        } else if (STRETCH_SPACING_AUTO == mStretchMode) {
            arrangeAllLine(mTagsLocLineList, finalTagsViewWidth, true);
        }
        setMeasuredDimension(finalTagsViewWidth, finalTagsViewHeight);
    }

    private void arrangeAllLine(List<ArrayList<TagLocationInfo>> tagsLocLineList, final int parentWidth, boolean isAutoDealLastLine) {
        if (null == tagsLocLineList) {
            return;
        }
        int lineCount = tagsLocLineList.size();
        for (int i = 0; i < lineCount; i++) {
            ArrayList<TagLocationInfo> tagLocationInfoList = tagsLocLineList.get(i);
            if (i == lineCount - 1) {
                arrangeLine(tagLocationInfoList, parentWidth, isAutoDealLastLine);
            } else {
                arrangeLine(tagLocationInfoList, parentWidth, false);
            }
        }
    }

    private void arrangeLine(final ArrayList<TagLocationInfo> tagLocationInfoList, final int parentWidth, final boolean isAutoDisposeLastLine) {
        if (null == tagLocationInfoList) {
            return;
        }
        int tagCount = tagLocationInfoList.size();
        if (tagCount > 1) {
            /* 只有大于一个元素才可能需要排列 */
            // remainderSpaceWidth is the remainder space.
            int remainderSpaceWidth = 0;
            int perOff = 0;
            for (int i = tagCount - 1; i >= 1; i--) {
                TagLocationInfo tagLocInfo = tagLocationInfoList.get(i);
                if (i == tagCount - 1) {
                    remainderSpaceWidth = parentWidth - paddingRight - tagLocInfo.getEndX();
                    if (isAutoDisposeLastLine && remainderSpaceWidth > parentWidth / 4) {
                        return;
                    }
                    perOff = remainderSpaceWidth / (tagCount - 1);
                }

                if (i == tagCount - 1) {
                                /* remove inaccuracy, cause we use "/" before.
                                 * 消除误差, 因为整除 */
                    tagLocInfo.setStartX(tagLocInfo.getStartX() + remainderSpaceWidth);
                } else {
                    tagLocInfo.startX = tagLocInfo.startX + perOff * i;
                }
            }
        } else {
            /* do nothing */
        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (DEBUG)
            Log.d(TAG, "--- onLayout changed :" + changed + " l :" + l + ",t :" + t + ",r :" + r + ",b :" + b);
        /** Modified by TinyCand 保证count的同步*/
//        int count = getChildCount();
        int count = null == mAdapter ? 0 : mAdapter.getCount();

        int width = getWidth();
        if (DEBUG) Log.i(TAG, "Width :" + width + " count :" + count);
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            childView = mAdapter.getView(i, childView, this);
            int w = childView.getMeasuredWidth();
            int h = childView.getMeasuredHeight();

            int x = mTagsLocList.get(i).startX;
            int y = mTagsLocList.get(i).startY;
            if (DEBUG) Log.i(TAG, "----- layout i : " + x + "," + y + " |w: " + w);

            // 布局子控件
            childView.layout(x, y, x + w, y + h);
        }

    }


    @Override
    public ListAdapter getAdapter() {
        if (DEBUG) Log.v(TAG, "getAdapter()");
        return mAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (DEBUG) Log.v(TAG, "--- setAdapter()");
        if (null != mAdapter && null != mAdapterDataSetObserver) {
            mAdapter.unregisterDataSetObserver(mAdapterDataSetObserver);
        }
        removeAllViewsInLayout();
        mAdapter = adapter;
        if (null != adapter) {
            mAdapter.registerDataSetObserver(mAdapterDataSetObserver);
            /* 把所有的child添加到布局中 */
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View child = mAdapter.getView(i, null, this);
                addChild(child, i);
            }
            requestLayout();
        }

    }

    /**
     * Adds a child to this viewGroup and measures it so it renders the correct size
     */
    private void addChild(final View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (null == params) {
            if (DEBUG) Log.w(TAG, "----- addChild params is NULL !");
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        addViewInLayout(child, viewPos, params, true);
    }

    /**
     * DataSetObserver used to capture adapter data change events
     */
    private DataSetObserver mAdapterDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (DEBUG) Log.v(TAG, "onChanged()");
            //Clear so we can notify again as we run out of data
            super.onChanged();
            removeAllViewsInLayout();

            requestLayout();//更新视图
        }

        @Override
        public void onInvalidated() {
            if (DEBUG) Log.v(TAG, "onInvalidated()");
            super.onInvalidated();
        }

    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mAdapter && null != mAdapterDataSetObserver) {
            mAdapter.unregisterDataSetObserver(mAdapterDataSetObserver);
            mAdapterDataSetObserver = null;
        }
    }

    public int getStretchMode() {
        return mStretchMode;
    }

    public void setStretchMode(int stretchMode) {
        this.mStretchMode = stretchMode;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void setDebug(boolean debug) {
        TagsView.DEBUG = debug;
    }

    /**
     * Not support
     */
    @Override
    @Deprecated
    public void setSelection(int position) {
        if (DEBUG) Log.v(TAG, "setSelection()");

    }

    /**
     * Not support
     */
    @Override
    @Deprecated
    public View getSelectedView() {
        if (DEBUG) Log.v(TAG, "getSelectedView()");
        return null;
    }
}
