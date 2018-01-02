package com.lenovo.tvflowrecyclerview;

/**
 * @author songwenju on 18-1-2.
 */

public class Module {
    public Module() {
    }

    public Module(String styleType) {
        this.styleType = styleType;
    }

    String styleType;
    String poster;

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public Object getPoster() {
        return poster;
    }

    @Override
    public String toString() {
        return "Module{" +
                "styleType='" + styleType + '\'' +
                ", poster='" + poster + '\'' +
                '}';
    }
}
