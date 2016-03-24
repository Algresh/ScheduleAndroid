package com.example.alex.scheduleandroid.dto;

public class MessageDTO  {

    int id;
    long dateSent;
    int grpId;
    String textMsg;
    int sent_ok;

    public MessageDTO(int id, long dateSent, int grpId, String textMsg, int sent_ok) {
        this.id = id;
        this.dateSent = dateSent;
        this.grpId = grpId;
        this.textMsg = textMsg;
        this.sent_ok = sent_ok;
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

    public int getSent_ok() {
        return sent_ok;
    }

    public void setSent_ok(int sent_ok) {
        this.sent_ok = sent_ok;
    }
}
