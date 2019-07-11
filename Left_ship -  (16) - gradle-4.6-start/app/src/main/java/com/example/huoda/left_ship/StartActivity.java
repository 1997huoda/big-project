package com.example.huoda.left_ship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StartActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private String todayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：此处将setContentView()方法注释掉
         setContentView(R.layout.activity_start);

         //isTodayFirstLogin();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //gotoLogin();
                isTodayFirstLogin();
            }
        }, 300);

    }

    private void gotoLogin() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
        overridePendingTransition(0, 0);
    }
    private void gotoLogin2() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

   /* @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            super.onDestroy();
           // saveExitTime(todayTime);
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }*/

    private void isTodayFirstLogin() {
        //取
//        SharedPreferences preferences = getSharedPreferences("LastLoginTime", MODE_PRIVATE);
//        String lastTime = preferences.getString("LoginTime", "2017-04-08");
        //修改为第一次登陆，而不是每日第一次登陆；
        SharedPreferences preferences = getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "0");

        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        todayTime = df.format(new Date());// 获取当前的日期

        if (lastTime.equals("1")) { //如果两个时间段相等// if (lastTime.equals(todayTime)) { //如果两个时间段相等
           // Toast.makeText(this, "不是当日首次登陆", Toast.LENGTH_SHORT).show();
           // saveExitTime(todayTime);
            gotoLogin2();
            Log.e("Time", lastTime);
        } else {
//            Toast.makeText(this, "霍达提示您是今日首次登陆", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "霍达提示您是首次登陆", Toast.LENGTH_SHORT).show();
//            saveExitTime(todayTime);
            saveExitTime("1");
            gotoLogin();
            Log.e("date", lastTime);
            Log.e("todayDate", todayTime);
        }
    }
    private void saveExitTime(String extiLoginTime) {
        SharedPreferences.Editor editor = getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }*/
}
