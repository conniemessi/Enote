package com.dscientia.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dscientia.R;
import com.dscientia.bean.User;
import com.dscientia.global.BaseActivity;
import com.dscientia.global.Conf;
import com.dscientia.global.Constant;
import com.dscientia.global.MyApplication;
import com.dscientia.util.HttpUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.dscientia.bean.Note.SAVE_SUCCESS;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class EditActivity extends BaseActivity {

    String single[] = {"日记","便签","学习笔记","读书笔记","旅行游记"};
    String week[] = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat",};
    StringBuilder sb;
    private String singleChoice;

    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button saveButton;
    EditText noteContent;
    EditText noteTitle;
    ImageView back;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
    }

    private void initView() {
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        saveButton = (Button) findViewById(R.id.note_save);
        noteContent = (EditText) findViewById(R.id.editText3);
        noteTitle = (EditText) findViewById(R.id.note_title);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = noteTitle.getText().toString();
                String newContent = noteContent.getText().toString();
                Date date = new Date();
                String newDate = DateFormat.getDateTimeInstance().format(date);
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String weekNum = String.valueOf(newCalendar.get(Calendar.DAY_OF_WEEK));
                String newWeek = week[Integer.parseInt(weekNum) - 1];
                //Note newNote = new Note(newDate, newWeek, newTitle, newContent);

                try {
                    FileOutputStream outStream = new FileOutputStream("/sdcard/"+newTitle+".txt");
                    String lineEnd = System.getProperty("line.separator");
                    OutputStreamWriter writer = new OutputStreamWriter(outStream, "gb2312");
                    writer.write(newTitle);
                    writer.write(lineEnd);
                    writer.write(newDate);
                    writer.write(lineEnd);
                    writer.write(newWeek);
                    writer.write(lineEnd);
                    writer.write(newContent);
                    writer.close();
                    outStream.close();

                    try {
                        File location = Environment.getRootDirectory();
                        FileOutputStream subOutputStream;
                        subOutputStream = openFileOutput(location+Constant.NOTE_LIST_FILE_NAME, Context.MODE_APPEND);
                        OutputStreamWriter subwriter = new OutputStreamWriter(subOutputStream, "gb2312");
                        subwriter.write(newTitle+".txt");
                        subwriter.write(lineEnd);
                        subwriter.close();
                        subOutputStream.close();
                    }catch(Exception e) {
                        throw e;
                    }
                    Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                }catch(Exception e){
                    Log.e("m", "file write error");
                    Toast.makeText(EditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                if (Conf.isLogin) {
                    User user = ((MyApplication) getApplication()).getUser();
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("title", newTitle));
                    params.add(new BasicNameValuePair("uid", user.getUid()));
                    params.add(new BasicNameValuePair("nbid", (user.getNotebookIds()).get(0)));
                    params.add(new BasicNameValuePair("time", newDate));
                    params.add(new BasicNameValuePair("weekday", newWeek));
                    params.add(new BasicNameValuePair("body", newContent));
                    new HttpUtil().create(HttpUtil.POST, Constant.ADD_NOTE_API, params, new HttpUtil.HttpCallBallListener() {
                        @Override
                        public void onStart() {
                            progressDialog = ProgressDialog.show(EditActivity.this, "提示", "同步中，请稍等");
                        }

                        @Override
                        public void onFinish() {
                            progressDialog.cancel();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(EditActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("code") == 1) {
                                    Toast.makeText(EditActivity.this, "同步成功", Toast.LENGTH_SHORT).show();
                                    JSONObject object = jsonObject.getJSONObject("data");
                                    setResult(SAVE_SUCCESS);
                                    EditActivity.this.finish();
                                } else {
                                    Toast.makeText(EditActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });

    }

    public void singleChoiceItems(View view) {//单选按钮监听
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Select type").setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleChoice = single[0];//默认选择第一项
                Toast.makeText(EditActivity.this, "选择了" + singleChoice, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        })
                //设置单选框监听
                .setSingleChoiceItems(single, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        singleChoice = single[which];//根据which决定选择了哪一个子项
                    }
                }).create();
        dialog.show();
    }
}

