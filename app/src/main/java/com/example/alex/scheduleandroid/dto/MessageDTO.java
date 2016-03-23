package com.example.alex.scheduleandroid.dto;

public class MessageDTO  {

    int id;
    long dateSent;
    int grpId;
    String textMsg;

    public MessageDTO(int id, long dateSent, int grpId, String textMsg) {
        this.id = id;
        this.dateSent = dateSent;
        this.grpId = grpId;
        this.textMsg = textMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateSent() {
        return dateSent;
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }

    public int getGrpId() {
        return grpId;
    }

    public void setGrpId(int grpId) {
        this.grpId = grpId;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }
}
