package com.example.maruti5;

public class MessageModel {

    String Uid,message;
    long timestamp;




    public MessageModel(String uid, String message, long timestamp) {
        Uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uid, String message) {
        Uid = uid;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
