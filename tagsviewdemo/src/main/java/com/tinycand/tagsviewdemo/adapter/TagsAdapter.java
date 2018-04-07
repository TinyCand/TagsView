package com.tinycand.tagsviewdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinycand.tagsviewdemo.R;
import com.tinycand.tagsviewdemo.bean.TagInfo;

import java.util.List;

public class TagsAdapter extends BaseAdapter {
    private static final String TAG = TagsAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<TagInfo> dataList;
    private OnItemClickListener onItemClickListener;
    private float density;
    private boolean isSelectable = true;

    public TagsAdapter(Context context, List<TagInfo> dataList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;

        density = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public TagInfo getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        final TagInfo item = getItem(pos);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tag, viewGroup, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(item.getDisplayName());
        holder.nameTextView.setTextColor(item.getColor());

        updataCheckStatusUI(item, holder);
        if (isSelectable) {
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setChecked(!item.isChecked());

                    updataCheckStatusUI(item, holder);
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(pos, item);
                    }

                }
            });
        } else {
            holder.mainLayout.setOnClickListener(null);
        }
        return convertView;
    }

    private void updataCheckStatusUI(final TagInfo item, final ViewHolder holder) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(3 * density);
        drawable.setStroke((int) (0.5 * density + 0.5), item.getColor());
        if (item.isChecked()) {
            holder.nameTextView.setTextColor(Color.WHITE);
            drawable.setColor(item.getColor());
        } else {
            holder.nameTextView.setTextColor(item.getColor());
            drawable.setColor(Color.WHITE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.mainLayout.setBackground(drawable);
        } else {
            holder.mainLayout.setBackgroundDrawable(drawable);
        }

    }

    public class ViewHolder {
        View mainLayout;
        TextView nameTextView;

        public ViewHolder(View v) {
            mainLayout = v.findViewById(R.id.layout_main);
            nameTextView = (TextView) v.findViewById(R.id.txt_color_name);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int pos, TagInfo item);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }
}
