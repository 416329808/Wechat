package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout mTabweixin;
    private LinearLayout mTabfrd;
    private LinearLayout mTabaddress;
    private LinearLayout mTabsetting;

    private ImageButton mImgweixin;
    private ImageButton mImgfrd;
    private ImageButton mImgaddress;
    private ImageButton mImgsetting;

    private  weixinfragment tab01;
    private  Frdfragment tab02;
    private  Addressfragment tab03;
    private  Settingfragment tab04;

    //新加入的tab01中的按钮和文本框
    private Button button;
    private TextView textView;

    public static final String ACTION = "org.crazyit.action.ACTION";

    public static final String FLAGACTION = "org.crazyit.action.FLAGACTION";

    //接收广播的对象
    ActivityReceiver activityReceiver;
    String[] String = new String[] { "today", "is", "sunny","day","!"};

    @Override
    protected void onStart() {
        super.onStart();
        textView=this.tab01.getView().findViewById(R.id.textView2);
        button=this.tab01.getView().findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("org.crazyit.action.ACTION");
                sendBroadcast(intent);
            }
        });

    }

    private void initView() {
        mImgweixin = (ImageButton) findViewById(R.id.id_tab_weixin_img);
        mImgfrd = (ImageButton) findViewById(R.id.id_tab_friend_img);
        mImgaddress = (ImageButton) findViewById(R.id.id_tab_content_img);
        mImgsetting = (ImageButton) findViewById(R.id.id_tab_setting_img);
        mTabweixin = (LinearLayout) findViewById(R.id.id_tab_weixin);
        mTabfrd = (LinearLayout) findViewById(R.id.id_tab_friend);
        mTabaddress =  (LinearLayout) findViewById(R.id.id_tab_content);
        mTabsetting =  (LinearLayout) findViewById(R.id.id_tab_setting);
    }

    private void initEvents() {
        mTabweixin.setOnClickListener(this);
        mTabfrd.setOnClickListener(this);
        mTabaddress.setOnClickListener(this);
        mTabsetting.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        setSelect(0);
        activityReceiver = new ActivityReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(FLAGACTION);
        // 注册BroadcastReceiver
        registerReceiver(activityReceiver, filter);
        Intent intent = new Intent(this, MyService.class);
        // 启动后台Service
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_content) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        resetImg();
        switch (v.getId()) {
            case R.id.id_tab_weixin:
                setSelect(0);
                break;
            case R.id.id_tab_friend:
                setSelect(1);
                break;
            case R.id.id_tab_content:
                setSelect(2);
                break;
            case R.id.id_tab_setting:
                setSelect(3);
                break;
            default:
                break;
        }

    }


    private void resetImg() {
        mImgweixin.setImageResource(R.drawable.tab_weixin_normal);
        mImgfrd.setImageResource(R.drawable.tab_find_frd_normal);
        mImgaddress.setImageResource(R.drawable.tab_address_normal);
        mImgsetting.setImageResource(R.drawable.tab_settings_normal);
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (tab01 == null) {
                    tab01 = new weixinfragment();
                    transaction.add(R.id.id_content, tab01);//将微信聊天界面的Fragment添加到Activity中
                }else {
                    transaction.show(tab01);
                }
                mImgweixin.setImageResource(R.drawable.tab_weixin_pressed);
                break;
            case 1:
                if (tab02 == null) {
                    tab02 = new Frdfragment();
                    transaction.add(R.id.id_content, tab02);
                }else {
                    transaction.show(tab02);
                }
                mImgfrd.setImageResource(R.drawable.tab_find_frd_pressed);
                break;
            case 2:
                if (tab03 == null) {
                    tab03 = new Addressfragment();
                    transaction.add(R.id.id_content, tab03);
                }else {
                    transaction.show(tab03);
                }
                mImgaddress.setImageResource(R.drawable.tab_address_pressed);
                break;
            case 3:
                if (tab04 == null) {
                    tab04 = new Settingfragment();
                    transaction.add(R.id.id_content, tab04);
                }else {
                    transaction.show(tab04);
                }
                mImgsetting.setImageResource(R.drawable.tab_settings_pressed);
                break;

            default:
                break;
        }
        transaction.commit();
    }

    /*
     * 隐藏所有的Fragment
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if (tab01 != null) {
            transaction.hide(tab01);
        }
        if (tab02 != null) {
            transaction.hide(tab02);
        }
        if (tab03 != null) {
            transaction.hide(tab03);
        }
        if (tab04 != null) {
            transaction.hide(tab04);
        }

    }

    // 自定义的BroadcastReceiver，负责监听从Service传回来的广播
    public class ActivityReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 获取Intent中的flag
            int flag =intent.getIntExtra("flag", 0);
            switch (flag)
            {
                case 0:
                    textView.setText(""+String[0]);
                    break;
                case 1:
                    textView.setText(""+String[1]);
                    break;
                case 2:
                    textView.setText(""+String[2]);
                    break;
                case 3:
                    textView.setText(""+String[3]);
                    break;
                case 4:
                    textView.setText(""+String[4]);
                    break;

                default:
                    break;
            }
        }

    }

}
