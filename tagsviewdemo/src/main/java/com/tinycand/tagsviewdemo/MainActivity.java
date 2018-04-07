package com.tinycand.tagsviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.tinycand.tagsview.TagsView;
import com.tinycand.tagsview.TagsView;
import com.tinycand.tagsviewdemo.adapter.TagsAdapter;
import com.tinycand.tagsviewdemo.bean.TagInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] mTagsName;
    TagsView mTagsView;
    TagsView mMyTagsView;
    private TagsAdapter mTagsAdapter;
    private TagsAdapter mMyTagsAdapter;
    private List<TagInfo> mProfileTagList;
    private List<TagInfo> mMyTagList;
    private SparseArray<TagInfo> mMyTagSparseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        bindView();
    }

    private void initView() {
        TagsView.setDebug(true);
        mTagsView = (TagsView) findViewById(R.id.tagsView_all);
//        mTagsView.setStretchMode(TagsView.NO_STRETCH);
//        mTagsView.setStretchMode(TagsView.STRETCH_SPACING);
        mTagsView.setStretchMode(TagsView.STRETCH_SPACING_AUTO);

        mMyTagsView = (TagsView) findViewById(R.id.tagsView_mine);
        mMyTagsView.setStretchMode(TagsView.NO_STRETCH);
//        mMyTagsView.setStretchMode(TagsView.STRETCH_SPACING_AUTO);
//        mMyTagsView.setStretchMode(TagsView.STRETCH_SPACING);
    }

    private void initData() {
        mTagsName = getResources().getStringArray(R.array.tagsName);

        mProfileTagList = new ArrayList<>();
        int[] colors = getResources().getIntArray(R.array.tagColors);
        int len = colors.length;
        Random random = new Random();
        for (int i = 0; i < mTagsName.length; i++) {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setId(i);
            int pos = random.nextInt(len);
            tagInfo.setColor(colors[pos]);
            tagInfo.setDisplayName(mTagsName[i]);

            mProfileTagList.add(tagInfo);
        }


        mMyTagList = new ArrayList<>();
//        mMyTagList.addAll(mProfileTagList);
        mMyTagSparseArray = new SparseArray<>();
    }

    private void bindView() {
        mTagsAdapter = new TagsAdapter(this, mProfileTagList);
        mTagsAdapter.setOnItemClickListener(new TagsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, TagInfo item) {
                if (item.isChecked()) {
                    if (null == mMyTagSparseArray.get(pos)) {
                        mMyTagSparseArray.put(item.getId(), item);
                    }
                } else {
                    mMyTagSparseArray.remove(item.getId());
                }

                if (item.isChecked()) {
                    if (mMyTagList.contains(item)) {
                        /* do nothing */
                    } else {
                        mMyTagList.add(item);
                    }
                } else {
                    mMyTagList.remove(item);
                }

                mMyTagsAdapter.notifyDataSetChanged();
            }
        });
        mTagsView.setAdapter(mTagsAdapter);

        mMyTagsAdapter = new TagsAdapter(this, mMyTagList);
        mMyTagsAdapter.setSelectable(false);
        mMyTagsView.setAdapter(mMyTagsAdapter);
    }
}
