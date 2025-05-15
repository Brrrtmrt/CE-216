package org.example.artifactcatalog2;

public class Dimension {
    private long width;
    private long length;
    private long height;



    public Dimension(long length, long width, long height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }
    public Dimension() {}

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

    @Override
    public String toString() {
        return "Dimension{" +
                "width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}
