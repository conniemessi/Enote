package com.dscientia.user;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.bean.User;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;
import com.dscientia.util.BitmapUtil;
import com.dscientia.util.HttpUtil;
import com.dscientia.view.ClipImageLayout;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class ClipUserLogolActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ClipUserLogolActivity";

    public static int CLIP_USER_LOGOL_SUCCESS = 31;

    private ClipImageLayout clipImageLayout;
    private ImageView back;
    private ImageView clip;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_user_logol);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        clip = (ImageView) findViewById(R.id.img_clip);
        clipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
        int screenWidth;
        int screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        clipImageLayout.setHorizontalPadding(20);
        clipImageLayout.setScreenWidth(screenWidth);
        clipImageLayout.setScreenHeight(screenHeight);
        Uri uri = Uri.parse(getIntent().getStringExtra("Uri"));
        Log.i(TAG, "==============>" + uri.getPath());
        Bitmap bm = BitmapUtil.getSmallBitmap(BitmapUtil.getFilePath(this, uri));
        clipImageLayout.setBitmap(bm);
        back.setOnClickListener(this);
        clip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_clip:
                Bitmap bm = clipImageLayout.clip();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] data = baos.toByteArray();
                try {
                    String image = new String(Base64.encodeBase64(data), "UTF-8");
                    Log.i(TAG, "image==============>" + image);
                    User user = ((MyApplication)getApplication()).getUser();
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("avatar", image));
                    params.add(new BasicNameValuePair("uid", user.getUid()));
                    Log.i(TAG, "uid====>" + user.getUid());
                    new HttpUtil().create(HttpUtil.POST, Constant.UPDATE_USER_LOGO_API, params, new HttpUtil.HttpCallBallListener() {
                        @Override
                        public void onStart() {
                            pd = ProgressDialog.show(ClipUserLogolActivity.this, "提示", "上传头像");
                        }

                        @Override
                        public void onFinish() {
                            pd.cancel();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(ClipUserLogolActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("code") == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    User user = ((MyApplication) getApplication()).getUser();
                                    if (data.getString("avatar_url") != null) {
                                        Toast.makeText(ClipUserLogolActivity.this, "头像修改成功", Toast.LENGTH_SHORT).show();
                                        user.setAvatar(data.getString("avatar_url"));
                                        User.save(ClipUserLogolActivity.this, user);
                                        setResult(CLIP_USER_LOGOL_SUCCESS);
                                    } else {
                                        Toast.makeText(ClipUserLogolActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(ClipUserLogolActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}

