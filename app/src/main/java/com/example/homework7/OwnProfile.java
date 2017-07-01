package com.example.homework7;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Nikita on 11/18/2016.
 */
public class OwnProfile implements Serializable {

    String uid,gender,displayName;
    String displayPic;

    @Override
    public String toString() {
        return "OwnProfile{" +
                "uid='" + uid + '\'' +
                ", gender='" + gender + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayPic='" + displayPic + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }
}
