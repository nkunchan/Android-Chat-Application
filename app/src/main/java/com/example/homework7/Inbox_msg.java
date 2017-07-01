package com.example.homework7;

/**
 * Created by Nikita on 11/19/2016.
 */
public class Inbox_msg {
    String dp,uid,dis_name,time,message,type,content,status;

    @Override
    public String toString() {
        return "Inbox_msg{" +
                "dp='" + dp + '\'' +
                ", uid='" + uid + '\'' +
                ", dis_name='" + dis_name + '\'' +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDis_name() {
        return dis_name;
    }

    public void setDis_name(String dis_name) {
        this.dis_name = dis_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
