package com.dscientia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dscientia.bean.User;
import com.dscientia.discover.DiscoverFragment;
import com.dscientia.first.FirstFragment;
import com.dscientia.global.BaseFragActivity;
import com.dscientia.global.Conf;
import com.dscientia.global.MyApplication;
import com.dscientia.notebook.NotebookFragment;
import com.dscientia.user.EditActivity;
import com.dscientia.user.LoginActivity;
import com.dscientia.user.ModifyUserInfoActivity;
import com.dscientia.user.NotescanActivity;
import com.dscientia.user.UserInfoActivity;
import com.dscientia.util.SPUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragActivity {

    public static final String TAG = "MainActivity";

    public static final int LOGIN = 1;
    public static final int USER_INFO = 2;

    List<Fragment> fragments = new ArrayList<Fragment>();
    FirstFragment firstFragment;
    NotebookFragment notebookFragment;
    DiscoverFragment discoverFragment;

    List<ImageView> ivIndicators = new ArrayList<ImageView>();
    List<TextView> tvIndicators = new ArrayList<TextView>();

    SimpleDraweeView mainUserLogo;

    ImageView addButton;
    ImageView notescan;
    TextView titleText;

    SlidingMenu menu;
    SlidingMenu.CanvasTransformer mTransformer;
    TextView username;
    RelativeLayout userLayout;
    SimpleDraweeView userLogo;
    RelativeLayout colLayout;
    RelativeLayout focusLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        initSlidingMenu();
        initBroadcastReceiver();
    }

    private void initView() {
        mainUserLogo = (SimpleDraweeView) findViewById(R.id.user_img);
        addButton = (ImageView) findViewById(R.id.add_button);
        notescan=(ImageView) findViewById(R.id.notescan);
        titleText = (TextView) findViewById(R.id.title_text);
        mainUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        notescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotescanActivity.class));
            }
        });

        TextView tvFirst = (TextView) findViewById(R.id.first_tv);
        ImageView ivFirst = (ImageView) findViewById(R.id.first);
        TextView tvClub = (TextView) findViewById(R.id.club_tv);
        ImageView ivClub = (ImageView) findViewById(R.id.notebook);
        TextView tvDiscover = (TextView) findViewById(R.id.discover_tv);
        ImageView ivDiscover = (ImageView) findViewById(R.id.discover);
        ivIndicators.add(ivFirst);
        ivIndicators.add(ivClub);
        ivIndicators.add(ivDiscover);
        tvIndicators.add(tvFirst);
        tvIndicators.add(tvClub);
        tvIndicators.add(tvDiscover);

        LinearLayout firstLayout = (LinearLayout) findViewById(R.id.first_layout);
        LinearLayout societyLayout = (LinearLayout) findViewById(R.id.society_layout);
        LinearLayout discoverLayout = (LinearLayout) findViewById(R.id.discover_layout);
        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(0);
            }
        });
        societyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(1);
            }
        });
        discoverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(2);
            }
        });
    }

    private void initBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ModifyUserInfoActivity.MODIFY_SUCCESS);
        registerReceiver(mReceiver, filter);
    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        if (firstFragment == null) {
            firstFragment = new FirstFragment();
        }
        if(notebookFragment ==null){
            notebookFragment = new NotebookFragment();
        }
        if (discoverFragment == null) {
            discoverFragment = new DiscoverFragment();
        }
        fragments.add(firstFragment);
        fragments.add(notebookFragment);
        fragments.add(discoverFragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (!fragments.get(i).isAdded()) {
                ft.add(R.id.content, fragments.get(i));
            }
            if (i == 0) {
                ft.show(fragments.get(i));
            } else {
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();

    }

    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (i == idx) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        switch (idx) {
            case 0:
                ivIndicators.get(0).setImageResource(R.mipmap.note_press);
                tvIndicators.get(0).setTextColor(Color.parseColor("#5479b8"));
                ivIndicators.get(1).setImageResource(R.mipmap.notebook);
                tvIndicators.get(1).setTextColor(Color.parseColor("#a2a3a3"));
                ivIndicators.get(2).setImageResource(R.mipmap.find);
                tvIndicators.get(2).setTextColor(Color.parseColor("#a2a3a3"));
                titleText.setText("笔记");
                break;
            case 1:
                ivIndicators.get(0).setImageResource(R.mipmap.note);
                tvIndicators.get(0).setTextColor(Color.parseColor("#a2a3a3"));
                ivIndicators.get(1).setImageResource(R.mipmap.notebook_press);
                tvIndicators.get(1).setTextColor(Color.parseColor("#5479b8"));
                ivIndicators.get(2).setImageResource(R.mipmap.find);
                tvIndicators.get(2).setTextColor(Color.parseColor("#a2a3a3"));
                titleText.setText("笔记本");
                break;
            case 2:
                ivIndicators.get(0).setImageResource(R.mipmap.note);
                tvIndicators.get(0).setTextColor(Color.parseColor("#a2a3a3"));
                ivIndicators.get(1).setImageResource(R.mipmap.notebook);
                tvIndicators.get(1).setTextColor(Color.parseColor("#a2a3a3"));
                ivIndicators.get(2).setImageResource(R.mipmap.find_press);
                tvIndicators.get(2).setTextColor(Color.parseColor("#5479b8"));
                titleText.setText("推荐");
                break;
            default:
                break;
        }

    }

    private void initSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setFadeDegree(0.35f);
        menu.setMenu(R.layout.activity_user);
        mTransformer = new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen*0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
            }
        };
        menu.setBehindCanvasTransformer(mTransformer);
        userLayout = (RelativeLayout) menu.findViewById(R.id.user_logol_layout);
        username = (TextView) menu.findViewById(R.id.user_name);
        userLogo = (SimpleDraweeView) menu.findViewById(R.id.user_logol);
        colLayout = (RelativeLayout) menu.findViewById(R.id.col_layout);
        focusLayout = (RelativeLayout) menu.findViewById(R.id.focus_layout);
        Conf.isLogin = (boolean) SPUtil.get(this, "isLogin", false);
        initUserData();
        initSlidingMenuListener();
    }

    private void initUserData() {
        if (Conf.isLogin == true) {
            User user = User.load(this);
            ((MyApplication)getApplication()).setUser(user);
            username.setText(user.getName());
            Log.i(TAG, "Avatar==========>" + user.getAvatar());
            if (user.getAvatar().equals("")) {
                userLogo.setImageResource(R.mipmap.mine);
                mainUserLogo.setImageResource(R.mipmap.mine);
            } else {
                Uri uri = Uri.parse(user.getAvatar());
                userLogo.setImageURI(uri);
                mainUserLogo.setImageURI(uri);
            }
        } else {
            mainUserLogo.setImageResource(R.mipmap.mine);
            userLogo.setImageResource(R.mipmap.mine);
            username.setText("未登录");
        }
    }

    private void initSlidingMenuListener() {
        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Conf.isLogin == false) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LOGIN);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, UserInfoActivity.class), USER_INFO);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN && resultCode == LoginActivity.LOGIN_SUCCESS) {
            User user = ((MyApplication)getApplication()).getUser();
            username.setText(user.getName());
            if (user.getAvatar().equals("")) {
                userLogo.setImageResource(R.mipmap.mine);
                mainUserLogo.setImageResource(R.mipmap.mine);
            } else {
                Uri uri = Uri.parse(user.getAvatar());
                userLogo.setImageURI(uri);
                mainUserLogo.setImageURI(uri);
            }
        } else if (requestCode == USER_INFO && resultCode == UserInfoActivity.LOGOUT) {
            username.setText("未登录");
            userLogo.setImageResource(R.mipmap.mine);
            mainUserLogo.setImageResource(R.mipmap.mine);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ModifyUserInfoActivity.MODIFY_SUCCESS)) {
                User user = ((MyApplication)getApplication()).getUser();
                if (user.getAvatar().equals("")) {
                    userLogo.setImageResource(R.mipmap.mine);
                    mainUserLogo.setImageResource(R.mipmap.mine);
                } else {
                    Uri uri = Uri.parse(user.getAvatar());
                    userLogo.setImageURI(uri);
                    mainUserLogo.setImageURI(uri);
                }
            }
        }
    };
}

