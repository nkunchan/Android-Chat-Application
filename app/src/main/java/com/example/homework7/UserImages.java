package com.example.homework7;

import java.io.Serializable;

/**
 * Created by priya on 11/18/2016.
 */

public class UserImages implements Serializable {

    String image,dateofupload;

    public String getDateofupload() {
        return dateofupload;
    }

    public void setDateofupload(String dateofupload) {
        this.dateofupload = dateofupload;
    }

    @Override
    public String toString() {
        return "UserImages{" +
                "image='" + image + '\'' +
                ", dateofupload='" + dateofupload + '\'' +
                '}';
    }

    public UserImages(){}

    public UserImages(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
