package com.example.homework7;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Nikita on 11/18/2016.
 */
public class OtherUsers implements Serializable {

    String dp,uid;
    String displayName;

    public String getDp() {
        return dp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "OtherUsers{" +
                "dp='" + dp + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
