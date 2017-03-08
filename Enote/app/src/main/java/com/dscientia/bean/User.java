package com.dscientia.bean;

import android.content.Context;

import com.dscientia.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class User {

    private String uid;
    private String name;
    private String avatar;
    private int nbsNum;
    private List<String> notebookIds;
    private List<Notebook> notebooks;

    public User() {
        notebooks = new ArrayList<Notebook>();
        notebookIds = new ArrayList<String>();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getNotebookIds() {
        return notebookIds;
    }

    public void setNotebookIds(List<String> notebookIds) {
        this.notebookIds = notebookIds;
    }

    public int getNbsNum() {
        return nbsNum;
    }

    public void setNbsNum(int nbsNum) {
        this.nbsNum = nbsNum;
    }

    public List<Notebook> getNotebooks() {
        return notebooks;
    }

    public void setNotebooks(List<Notebook> notebooks) {
        this.notebooks = notebooks;
    }

    public void addNoteBooks(Notebook noteBook) {
        notebooks.add(noteBook);
        nbsNum++;
    }

    public void addNotebookIds(String notebookId) {
        notebookIds.add(notebookId);
    }

    public static void save(Context context, User user) {
        SPUtil.put(context, "name", user.getName());
        SPUtil.put(context, "avatar_url", user.getAvatar());
        SPUtil.put(context, "uid", user.getUid());
    }

    public static User load(Context context) {
        User user = new User();
        user.setName((String) SPUtil.get(context, "name", ""));
        user.setAvatar((String) SPUtil.get(context, "avatar_url", ""));
        user.setUid((String) SPUtil.get(context, "uid", ""));

        return user;
    }

    public static void clear(Context context) {
        SPUtil.remove(context, "name");
        SPUtil.remove(context, "avatar_url");
        SPUtil.remove(context, "uid");
    }

}