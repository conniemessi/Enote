package com.dscientia.first;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dscientia.R;
import com.dscientia.bean.Note;

import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class FirstAdapter extends BaseAdapter {

    private List<Note> notes;
    private Context context;

    public FirstAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_first, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.note_title);
            viewHolder.noteDate = (TextView) view.findViewById(R.id.note_date);
            viewHolder.noteWeek = (TextView) view.findViewById(R.id.note_week);
            viewHolder.noteContent = (TextView) view.findViewById(R.id.note_content);
            //viewHolder.img = (SimpleDraweeView) view.findViewById(R.id.note_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(notes.get(i).getNoteName());
        viewHolder.noteDate.setText(notes.get(i).getDate());
        viewHolder.noteWeek.setText(notes.get(i).getWeek());
        viewHolder.noteContent.setText(notes.get(i).getContent());
        //Uri uri = Uri.parse(notes.get(i).getImgUrl());
        //viewHolder.img.setImageURI(uri);
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView noteDate;
        TextView noteWeek;
        TextView noteContent;
        //SimpleDraweeView img;
    }

    public void addData(List<Note> notes) {
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}