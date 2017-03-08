package com.dscientia.notebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.bean.Notebook;
import com.dscientia.bean.User;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;
import com.dscientia.util.HttpUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class NotebookFragment extends Fragment {

    public static final String TAG = "NotebookFragment";

    private SwipeRefreshLayout refreshLayout;
    private ListView list;
    private MyAdapter adapter;
    private List<Notebook> notebooks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notebook, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View v) {
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        list = (ListView) v.findViewById(R.id.listview);
        notebooks = new ArrayList<Notebook>();
        adapter = new MyAdapter(getActivity(), notebooks);
        list.setAdapter(adapter);
    }

    private void initData() {
        User user = ((MyApplication) getContext().getApplicationContext()).getUser();
       /* for (int i = 0; i < user.getNotebooks().size(); ++i) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("uid", user.getUid()));
            params.add(new BasicNameValuePair("nbid", user.getNotebookIds().get(i)));
            new HttpUtil().create(HttpUtil.POST, Constant.GET_ALL_NOTEBOOKS, params, new HttpUtil.HttpCallBallListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onError() {
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    adapter.setData(notebooks);
                    Log.i(TAG, "notebook size============>" + notebooks.size());
                }

                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getInt("code") == 1) {
                            notebooks.clear();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jo = data.optJSONObject(i);
                                Notebook notebook = new Notebook();
                                notebook.setName(jo.getString("name"));
                                notebook.setNbid(jo.getString("_id"));
                                notebook.setImgUrl(jo.getString("img_url"));
                                notebooks.add(notebook);
                            }
                            adapter.setData(notebooks);
                        } else {
                            Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                            adapter.setData(notebooks);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/

    }

    private void initListener() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NotebookDetailActivity.class);
                intent.putExtra("notebook", notebooks.get(position));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        Context context;
        List<Notebook> notebooks;

        MyAdapter(Context context, List<Notebook> notebooks) {
            this.context = context;
            this.notebooks = notebooks;
        }

        @Override
        public int getCount() {
            return notebooks.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_notebook, null);
                viewHolder = new ViewHolder();
                viewHolder.img = (SimpleDraweeView) convertView.findViewById(R.id.img);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Uri uri = Uri.parse(notebooks.get(position).getImgUrl());
            viewHolder.img.setImageURI(uri);
            viewHolder.name.setText(notebooks.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            SimpleDraweeView img;
            TextView name;
        }

        public void setData(List<Notebook> notebooks) {
            this.notebooks = notebooks;
            this.notifyDataSetChanged();
        }
    }
}


