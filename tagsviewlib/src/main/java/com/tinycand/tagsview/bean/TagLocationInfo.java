package com.tinycand.tagsview.bean;

/**
 * to record the location information of a tag.
 * Created by TinyCand on 2018/2/25
 * Email: TinyCand@gmail.com
 */

public class TagLocationInfo {
    public int startX;
    public int startY;
    private int endX;
    private int endY;

    public TagLocationInfo(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }
}
