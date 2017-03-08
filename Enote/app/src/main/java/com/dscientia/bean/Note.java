package com.dscientia.bean;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SAVE_SUCCESS = 4;

    private String mDate;
    private String mWeek;
    private String mContent;
    private String mNoteName;
    private String imgUrl;
    private String detailUrl;
    private String nid;
    private String nbid;

    public String toString() {
        return mNoteName + " Time:" + mDate + " Week:" + mWeek + " Content:" + mContent;
    }

    public Note() { }

    public Note(String date,String week ,String noteName, String content) {
        this.mDate = date;
        this.mWeek = week;
        this.mContent = content;
        this.mNoteName = noteName;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String Date) { this.mDate = Date; }

    public String getWeek(){
        return mWeek;
    }

    public void setWeek(String Week) { this.mWeek = Week; }

    public String getNoteName() {return mNoteName; }

    public void setNoteName(String noteName) {
        this.mNoteName = noteName;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String Content) { this.mContent = Content; }

    public void setId(String nid){this.nid=nid;}

    public String getId(){return nid;}

    public void setNbid(String nbid) {
        this.nbid = nbid;
    }

    public String getNbid() {
        return nbid;
    }

    /*public static void save(Context context, Note note) {
        SPUtil.put(context, "title", note.getNoteName());
        SPUtil.put(context, "time", note.getDate());
        SPUtil.put(context, "nid", note.getId());
        SPUtil.put(context, "weekday", note.getWeek());
        SPUtil.put(context, "body", note.getContent());
    }*/

}
