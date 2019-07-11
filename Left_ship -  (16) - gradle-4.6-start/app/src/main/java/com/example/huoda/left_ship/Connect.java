package com.example.huoda.left_ship;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect extends AppCompatActivity {

    private Button bt_connect;
    public EditText et_ip,et_db,et_user,et_pwd,et_ip2;
    public int ip_net=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bt_connect=(Button) findViewById(R.id.bt_connect);
        et_ip=(EditText) findViewById(R.id.et_ip);
        et_ip2=(EditText) findViewById(R.id.et_ip2);
        et_db=(EditText) findViewById(R.id.et_db);
        et_user=(EditText) findViewById(R.id.et_user);
        et_pwd=(EditText) findViewById(R.id.et_pwd);


    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(Connect.this, "霍达提示您连接成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Connect.this, "霍达提示您连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    public void onSuccess2(int i, int json) {
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void note(View view ){
        Snackbar.make(view, "霍达提示您默认选择的是外网连接", Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    public void bt_set(View view){
        if(ip_net==0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(Connect.this, MainActivity.class);

                        String c_ip = et_ip.getText().toString().trim();
                        String c_db = et_db.getText().toString().trim();
                        String c_user = et_user.getText().toString().trim();
                        String c_pwd = et_pwd.getText().toString().trim();

                        MainActivity.ip = c_ip;
                        MainActivity.db = c_db;
                        MainActivity.user = c_user;
                        MainActivity.pwd = c_pwd;


                        String url = "jdbc:mysql://" + c_ip + "/" + c_db;
                        System.out.println(url);
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection cn = DriverManager.getConnection(url, c_user, c_pwd);
                        Statement st = (Statement) cn.createStatement();
                        // Toast.makeText(Connect.this, "霍达提示您成功连接数据库!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 1);
                        cn.close();
                        st.close();
                        startActivity(intent);

                    } catch (ClassNotFoundException e) {
                        // Toast.makeText(Connect.this, "霍达提示您连接数据库失败!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 0);
                        e.printStackTrace();
                    } catch (SQLException e) {
                        // Toast.makeText(Connect.this, "霍达提示您连接数据库失败!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 0);
                        e.printStackTrace();
                    }
                }
            }).start();
            // Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        }
        else if(ip_net==1)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(Connect.this, MainActivity.class);

                        String c_ip = et_ip2.getText().toString().trim();
                        String c_db = et_db.getText().toString().trim();
                        String c_user = et_user.getText().toString().trim();
                        String c_pwd = et_pwd.getText().toString().trim();

                        MainActivity.ip = c_ip;
                        MainActivity.db = c_db;
                        MainActivity.user = c_user;
                        MainActivity.pwd = c_pwd;


                        String url = "jdbc:mysql://" + c_ip + "/" + c_db;
                        System.out.println(url);
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection cn = DriverManager.getConnection(url, c_user, c_pwd);
                        Statement st = (Statement) cn.createStatement();
                        // Toast.makeText(Connect.this, "霍达提示您成功连接数据库!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 1);
                        cn.close();
                        st.close();
                        startActivity(intent);

                    } catch (ClassNotFoundException e) {
                        // Toast.makeText(Connect.this, "霍达提示您连接数据库失败!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 0);
                        e.printStackTrace();
                    } catch (SQLException e) {
                        // Toast.makeText(Connect.this, "霍达提示您连接数据库失败!", Toast.LENGTH_SHORT).show();
                        onSuccess2(20, 0);
                        e.printStackTrace();
                    }
                }
            }).start();
            // Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void Net1(View view){
        ip_net=0;
        Snackbar.make(view, "您选择的是局域网连接", Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
        //Toast.makeText(this,"您选择的是内网连接", Toast.LENGTH_SHORT).show();
        // 10.63.101.201
    }
    public void Net2(View view){
        ip_net=1;
        Snackbar.make(view, "您选择的是外网连接", Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
       // Toast.makeText(this,"您选择的是外网连接", Toast.LENGTH_SHORT).show();
      //  Editable text = et_ip.getText("pcohd.uicp.cn:24967");
       // pcohd.uicp.cn:24967
    }

}
