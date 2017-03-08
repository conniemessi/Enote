package com.dscientia.notebook;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.bean.Note;
import com.dscientia.bean.Notebook;
import com.dscientia.global.BaseFragActivity;
import com.dscientia.global.Constant;
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

public class NotebookDetailActivity extends BaseFragActivity implements View.OnClickListener {

    private ViewPager vp;
    private ImageView back;
    private SimpleDraweeView societyLogo;
    private TextView name;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private NotebookAllFragment notebookAllFragment;

    private FragmentPagerAdapter adapter;
    private List<TextView> tvIndicators = new ArrayList<TextView>();
    private List<View> vIndicators = new ArrayList<View>();

    private ProgressDialog pd;

    Notebook notebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook_detail);
        initData();
        initView();
        getAllActivitiesByClub();
    }

    private void initData() {
        //introFragment = new ClubIntroFragment();
        notebookAllFragment = new NotebookAllFragment();
        //recentActivityFragment = new ClubRecentFragment();
        //fragments.add(introFragment);
        fragments.add(notebookAllFragment);
        //fragments.add(recentActivityFragment);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        notebook = (Notebook) getIntent().getSerializableExtra("notebook");
    }

    private void initView() {
        societyLogo.setImageURI(Uri.parse(notebook.getImgUrl()));
        name = (TextView) findViewById(R.id.club_name);
        name.setText(notebook.getName());
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setOffscreenPageLimit(3);
        LinearLayout llTwo = (LinearLayout) findViewById(R.id.all_ll);
        TextView tvTwo = (TextView) findViewById(R.id.all);
        View vTwo = findViewById(R.id.all_line);
        tvIndicators.add(tvTwo);
        vIndicators.add(vTwo);
        llTwo.setOnClickListener(this);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setAdapter(adapter);
    }

    private void setIndicators(int pos) {
        switch (pos) {
            case 0:
                tvIndicators.get(0).setTextColor(Color.parseColor("#222222"));
                vIndicators.get(0).setVisibility(View.VISIBLE);
                tvIndicators.get(1).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(1).setVisibility(View.INVISIBLE);
                tvIndicators.get(2).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(2).setVisibility(View.INVISIBLE);
                break;
            case 1:
                tvIndicators.get(0).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(0).setVisibility(View.INVISIBLE);
                tvIndicators.get(1).setTextColor(Color.parseColor("#222222"));
                vIndicators.get(1).setVisibility(View.VISIBLE);
                tvIndicators.get(2).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(2).setVisibility(View.INVISIBLE);
                break;
            case 2:
                tvIndicators.get(0).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(0).setVisibility(View.INVISIBLE);
                tvIndicators.get(1).setTextColor(Color.parseColor("#14aa81"));
                vIndicators.get(1).setVisibility(View.INVISIBLE);
                tvIndicators.get(2).setTextColor(Color.parseColor("#222222"));
                vIndicators.get(2).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getAllActivitiesByClub() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cname", notebook.getName()));
        new HttpUtil().create(HttpUtil.POST, Constant.GET_NOTES, params, new HttpUtil.HttpCallBallListener() {
            @Override
            public void onStart() {
                pd = ProgressDialog.show(NotebookDetailActivity.this, "提示", "加载中，请稍后");
            }

            @Override
            public void onFinish() {
                pd.cancel();
            }

            @Override
            public void onError() {
                Toast.makeText(NotebookDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {


                            JSONObject jo = data.optJSONObject(i);
                            Note activity = new Note();
                        }
                    } else {
                        Toast.makeText(NotebookDetailActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            /*case R.id.intro_ll:
                vp.setCurrentItem(0, false);
                setIndicators(0);
                break;*/
            case R.id.all_ll:
                vp.setCurrentItem(1, false);
                setIndicators(1);
                break;
            /*case R.id.recent_ll:
                vp.setCurrentItem(2, false);
                setIndicators(2);
                break;*/
        }
    }
}

