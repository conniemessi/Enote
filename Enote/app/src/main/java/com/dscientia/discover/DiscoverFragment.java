package com.dscientia.discover;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.WebActivity;
import com.dscientia.bean.Note;
import com.dscientia.first.FirstAdapter;
import com.dscientia.util.HttpUtil;
import com.zy.zlistview.view.ZListView;

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

public class DiscoverFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup typeGroup;
    private ZListView list;
    private List<Note> activities = new ArrayList<Note>();
    private FirstAdapter adapter;
    private ProgressDialog pd;
    private String type = "0";
    List<RadioButton> radioButtons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView(View v) {
        typeGroup = (RadioGroup) v.findViewById(R.id.type_group);
        typeGroup.setOnCheckedChangeListener(this);
        for (int i = 0; i < typeGroup.getChildCount(); i++) {
            radioButtons.add((RadioButton) typeGroup.getChildAt(i));
        }
        list = (ZListView) v.findViewById(R.id.listview);
        adapter = new FirstAdapter(activities, getActivity());
        list.setAdapter(adapter);
        list.setPullLoadEnable(true);
        list.setXListViewListener(new ZListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("type", type));
                //new HttpUtil().create(HttpUtil.POST, Constant.GET_SOME_ACTS_BY_TYPE, params, getNewDataCallBack);
            }

            @Override
            public void onLoadMore() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("skip", String.valueOf(activities.size())));
                //new HttpUtil().create(HttpUtil.POST, Constant.GET_MORE_ACTS_BY_TYPE, params, getMoreDataCallBack);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("activity", activities.get(position-1));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("type", type));
        //new HttpUtil().create(HttpUtil.POST, Constant.GET_SOME_ACTS_BY_TYPE, params, getNewDataCallBack);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setChechButton(group, checkedId);
        List<NameValuePair> params = new ArrayList<>();
        switch (checkedId) {
            case R.id.display:
                type = "0";
                params.add(new BasicNameValuePair("type", type));
                //new HttpUtil().create(HttpUtil.POST, Constant.GET_SOME_ACTS_BY_TYPE, params, getNewDataCallBack);
                break;
            case R.id.lecture:
                type = "1";
                params.add(new BasicNameValuePair("type", type));
                //new HttpUtil().create(HttpUtil.POST, Constant.GET_SOME_ACTS_BY_TYPE, params, getNewDataCallBack);
                break;
            case R.id.recruit:
                type = "2";
                params.add(new BasicNameValuePair("type", type));
                //new HttpUtil().create(HttpUtil.POST, Constant.GET_SOME_ACTS_BY_TYPE, params, getNewDataCallBack);
                break;
        }
    }

    private void setChechButton(RadioGroup group, int checkedId) {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).getId() == checkedId) {
                ((RadioButton)radioButtons.get(i)).setTextColor(Color.parseColor("#222222"));
            } else {
                ((RadioButton)radioButtons.get(i)).setTextColor(Color.parseColor("#24d399"));
            }
        }
    }

    HttpUtil.HttpCallBallListener getNewDataCallBack = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {
            pd = ProgressDialog.show(getActivity(), "提示", "加载中，请稍后");
        }

        @Override
        public void onFinish() {
            pd.cancel();
            list.stopRefresh();
        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 1) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    activities.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jo = data.optJSONObject(i);
                        Note note = new Note();
                        note.setId(jo.getString("id"));
                        note.setDetailUrl(jo.getString("detail_url"));
                        note.setImgUrl(jo.getString("img_url"));
                        activities.add(note);
                    }
                    adapter.setData(activities);
                } else {
                    Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    HttpUtil.HttpCallBallListener getMoreDataCallBack = new HttpUtil.HttpCallBallListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
            list.stopLoadMore();
        }

        @Override
        public void onError() {
            Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getInt("code") == 1) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jo = data.optJSONObject(i);
                        Note note = new Note();
                        note.setId(jo.getString("id"));
                        activities.add(note);
                    }
                    adapter.setData(activities);
                } else {
                    Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    };
}

