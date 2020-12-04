package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class AppInfo {

    public Drawable mappimage = null;
    public String mappname = "";
    public String mpackagename = "";

    public AppInfo(Drawable appimage, String appname, String packagename) {
        this.mappimage = appimage;
        this.mappname = appname;
        this.mpackagename = packagename;
    }

    public String getMappname() {
        return this.mappname;
    }

    public String getMpackagename() {
        return this.mpackagename;
    }

}