package com.yanlei.mooclike;

import android.graphics.Bitmap;

/**
 * Created by Yanlei on 2017/3/20.
 */

public class Movie {
    private String name;
    private String teacher;
    private Bitmap icon;

    public Movie(Bitmap icon, String name, String teacher) {
        this.icon = icon;
        this.name = name;
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
