package com.dscientia.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.dscientia.util.HttpUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dscientia.R;
import com.dscientia.bean.User;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noel_Cat on 2016/12/26.
 */


public class ModifyUserInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ModifyUserInfoActivity";

    public static final String MODIFY_SUCCESS = "MODIFY_SUCCESS";
    public static final int CHANGE_LOGOL = 21;
    public static final int CLIP_USER_LOGOL = 22;

    private ImageView back;
    private LinearLayout logolLayout;
    private SimpleDraweeView userLogol;
    private RadioGroup sexGroup;
    private Spinner major;
    private Spinner grade;
    private Button sure;

    private ProgressDialog pd;

    private User user;
    private String majorString;
    private String gradeString;
    private String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        initView();
        initData();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        logolLayout = (LinearLayout) findViewById(R.id.logol_layout);
        userLogol = (SimpleDraweeView) findViewById(R.id.user_logol);
        //sexGroup = (RadioGroup) findViewById(R.id.sex_group);
        //major = (Spinner) findViewById(R.id.major);
        //grade = (Spinner) findViewById(R.id.grade);
        sure = (Button) findViewById(R.id.sure);
        back.setOnClickListener(this);
        logolLayout.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    private void initData() {
        user = ((MyApplication) getApplication()).getUser();
        userLogol.setImageURI(Uri.parse(user.getAvatar()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.logol_layout:
                showChoosingPhotoDialog();
                break;
            case R.id.sure:
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("uid", user.getUid()));
                params.add(new BasicNameValuePair("sex", sex.equals("女") ? "0" : "1"));
                Log.i(TAG, "uid: " + user.getUid());
                new HttpUtil().create(HttpUtil.POST, Constant.UPDATE_USER_API, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        pd = ProgressDialog.show(ModifyUserInfoActivity.this, "提示", "信息更改");
                    }

                    @Override
                    public void onFinish() {
                        pd.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(ModifyUserInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Toast.makeText(ModifyUserInfoActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                                /*user.setGrade(gradeString);
                                user.setSex(sex);
                                user.setMajor(majorString);*/
                                User.save(ModifyUserInfoActivity.this, user);
                                Intent intent = new Intent();
                                intent.setAction(MODIFY_SUCCESS);
                                sendBroadcast(intent);
                                ModifyUserInfoActivity.this.finish();
                            } else {
                                Toast.makeText(ModifyUserInfoActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    private void showChoosingPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换头像");
        builder.setItems(new String[] { "相机", "图库" }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent cameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CHANGE_LOGOL);
                        break;
                    case 1:
                        Intent pictureIntent = new Intent();
                        pictureIntent.setType("image/*");
                        pictureIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(pictureIntent, CHANGE_LOGOL);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANGE_LOGOL:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        data.putExtra("Uri", uri.toString());
                        data.setClass(this, ClipUserLogolActivity.class);
                        startActivityForResult(data, CLIP_USER_LOGOL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CLIP_USER_LOGOL:
                if (resultCode == ClipUserLogolActivity.CLIP_USER_LOGOL_SUCCESS) {
                    Log.i(TAG, "avatar_url==============>" + user.getAvatar());
                    userLogol.setImageURI(Uri.parse(user.getAvatar()));
                    Intent intent = new Intent();
                    intent.setAction(MODIFY_SUCCESS);
                    sendBroadcast(intent);
                }
                break;
        }
    }
}
