package com.dscientia.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.bean.Note;
import com.dscientia.bean.Notebook;
import com.dscientia.bean.User;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Conf;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;
import com.dscientia.util.HttpUtil;
import com.dscientia.util.SPUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class LoginActivity extends BaseActivity {

    public static final int LOGIN_SUCCESS = 2;

    ImageView back;

    TextView register;
    EditText username;
    EditText password;

    Button login;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);

        register = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.user_pwd);

        login = (Button) findViewById(R.id.login);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "信息填写不完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
                if (!pattern.matcher(username.getText().toString().trim()).matches()) {
                    Toast.makeText(LoginActivity.this, "用户名只能是英文字母或数字", Toast.LENGTH_SHORT);
                    return;
                }
                if (!pattern.matcher(password.getText().toString().trim()).matches()) {
                    Toast.makeText(LoginActivity.this, "密码只能是英文字母或数字", Toast.LENGTH_SHORT);
                    return;
                }

                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("pwd", pwd));

                new HttpUtil().create(HttpUtil.POST, Constant.LOGIN_API, params, new HttpUtil.HttpCallBallListener() {
                    @Override
                    public void onStart() {
                        progressDialog = ProgressDialog.show(LoginActivity.this, "提示", "登录中，请稍等");
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.cancel();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(LoginActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 1) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Conf.isLogin = true;
                                JSONObject object = jsonObject.getJSONObject("data");
                                final User user = new User();
                                user.setName(object.getString("name"));
                                user.setUid(object.getString("_id").substring(9, object.getString("_id").length() - 2));
                                user.setAvatar(object.getString("avatar_url"));

                                JSONArray array = object.getJSONArray("notebooks");

                                for (int i = 0; i < array.length(); i++) {
                                    user.addNotebookIds(array.getString(i));

                                    List<NameValuePair> subparams = new ArrayList<>();
                                    subparams.add(new BasicNameValuePair("uid", user.getUid()));
                                    subparams.add(new BasicNameValuePair("nbid", array.getString(i)));
                                    new HttpUtil().create(HttpUtil.POST, Constant.GET_ALL_NOTEBOOKS, subparams, new HttpUtil.HttpCallBallListener() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onFinish() {
                                        }

                                        @Override
                                        public void onError() {
                                        }

                                        @Override
                                        public void onSuccess(String result) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(result);
                                                if (jsonObject.getInt("code") == 1) {
                                                    JSONArray data = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < data.length(); i++) {
                                                        JSONObject jo = data.optJSONObject(i);
                                                        Notebook notebook = new Notebook();
                                                        notebook.setName(jo.getString("name"));
                                                        notebook.setNbid(jo.getString("_id"));
                                                        notebook.setImgUrl(jo.getString("cover_url"));
                                                        JSONArray noteobj = jo.getJSONArray("notes_id");
                                                        for (int j = 0; j < noteobj.length();++j) {
                                                            notebook.addNoteid(noteobj.getString(j));
                                                        }
                                                        noteobj = jo.getJSONArray("notes");
                                                        for (int j = 0 ; j < noteobj.length() ; ++j) {
                                                            JSONObject note1 = noteobj.getJSONObject(j);
                                                            Note note = new Note();
                                                            note.setId(note1.getString("_id"));
                                                            note.setDate(note1.getString("time"));
                                                            note.setWeek(note1.getString("weekday"));
                                                            note.setContent(note1.getString("body"));
                                                            note.setNbid(notebook.getNbid());
                                                            note.setNoteName(note1.getString("title"));
                                                            notebook.addNote(note);
                                                        }
                                                        user.addNoteBooks(notebook);
                                                    }
                                                } else {
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }

                                User.save(LoginActivity.this, user);
                                ((MyApplication)getApplication()).setUser(user);
                                SPUtil.put(LoginActivity.this, "isLogin", Conf.isLogin);
                                setResult(LOGIN_SUCCESS);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码出错", Toast.LENGTH_SHORT).show();
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
