package com.dscientia.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Constant;
import com.dscientia.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class RegisterActivity extends BaseActivity {

    ImageView back;
    EditText username;
    EditText pwd;
    EditText pwdAgain;
    Button register;

    String name = "";
    String password = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        username = (EditText) findViewById(R.id.user_name);
        pwd = (EditText) findViewById(R.id.user_pwd);
        pwdAgain = (EditText) findViewById(R.id.user_pwd_again);
        register = (Button) findViewById(R.id.register);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("") || pwdAgain.getText().toString().equals("") || pwd.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.getText().toString().trim().equals(pwdAgain.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "前后密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
                if (!pattern.matcher(username.getText().toString().trim()).matches()) {
                    Toast.makeText(RegisterActivity.this, "用户名只能是英文字母或数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pattern.matcher(pwd.getText().toString().trim()).matches()) {
                    Toast.makeText(RegisterActivity.this, "密码只能是英文字母或数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                name = username.getText().toString().trim();
                password = pwd.getText().toString().trim();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("pwd", password));

                new HttpUtil().create(HttpUtil.POST, Constant.REGISTER_API, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        pd = ProgressDialog.show(RegisterActivity.this, "提示", "注册中，请稍等");
                    }

                    @Override
                    public void onFinish() {
                        pd.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}

