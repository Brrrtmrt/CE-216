package com.ce216;

public class Dimension {
    private long width;
    private long length;
    private long height;



    Dimension(long w, long l, long h) {
        width = w;
        length = l;
        height = h;
    }



    public long getWidth() {
        return width;
    }



    public void setWidth(long width) {
        this.width = width;
    }



    public long getLength() {
        return length;
    }



    public void setLength(long length) {
        this.length = length;
    }



    public long getHeight() {
        return height;
    }



    public void setHeight(long height) {
        this.height = height;
    }


}
