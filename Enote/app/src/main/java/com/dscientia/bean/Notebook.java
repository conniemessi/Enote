package com.dscientia.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class Notebook implements Serializable {
    private String nbid;
    private String imgUrl;
    private String name;
    private ArrayList<String> noteids = new ArrayList<String>();
    private ArrayList<Note> notes = new ArrayList<Note>();

    public void setNbid(String nbid) {
        this.nbid = nbid;
    }

    public String getNbid() {
        return nbid;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNoteids() {
        return noteids;
    }

    public void setNoteids(ArrayList<String> noteids) {
        this.noteids = noteids;
    }

    public void addNoteid (String noteid) {
        noteids.add(noteid);
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

   /* public void addAll(List<String> note){
        for (int i = 0; i < note.size(); ++i) {
            addNote(note.get(i));
        }
    }*/

    public void deleteNote(String note) {
        for (int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).equals(note)) {
                notes.remove(i);
                break;
            }
        }
    }
}