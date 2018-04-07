package com.tinycand.tagsviewdemo.bean;

/**
 * Created by TinyCand on 2018/3/28.
 * Email: tinycand@gmail.com
 */

public class TagInfo {

    private int id;
    private String displayName;
    private int color;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
