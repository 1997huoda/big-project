package com.example.huoda.left_ship;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateActivity extends AppCompatActivity {
    public View date_add,date_del;
    private AppBarLayout appBarLayout;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", /*Locale.getDefault()*/Locale.CHINESE);
    private CompactCalendarView compactCalendarView;
    private boolean isExpanded = false;
//    public String users,ip,db,user,pwd;
private SharedPreferences sharedPreferences;
public String  users,ip,db0,db,db2,user,pwd;
    public String date;
    public DatabaseConnection cnn = new DatabaseConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        date_add = findViewById(R.id.date_add);

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        date= (sf.format(c.getTime()));

//        Bundle bundle = this.getIntent().getExtras();
//        //接收name值
//        String users = bundle.getString("users");
//        String ip = bundle.getString("ip");
//        String db = bundle.getString("db");
//        String user = bundle.getString("user");
//        String pwd = bundle.getString("pwd");
        sharedPreferences = this.getSharedPreferences("userInfo", Context. MODE_PRIVATE);
        users=sharedPreferences.getString("user1", "user1");
        System.out.println(users);
        addTomId(ip, db, user, pwd,users,date);

        Handler handler = new Handler();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("公园植物管理系统");
        appBarLayout = findViewById(R.id.app_bar_layout);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                final SimpleDateFormat dateFormat_f = new SimpleDateFormat("yyyyMMdd", /*Locale.getDefault()*/Locale.ENGLISH);
                String day0=dateFormat_f.format(dateClicked);
                date=day0;

                final SimpleDateFormat dateFormat_f2 = new SimpleDateFormat("yyyy-MM-dd", /*Locale.getDefault()*/Locale.ENGLISH);
                String day2=dateFormat_f2.format(dateClicked);
                onSuccess(1,day2);
                addTomId(ip, db, user, pwd,users,date);
                final ImageView arrow = findViewById(R.id.date_picker_arrow);
                float rotation = isExpanded ? 0 : 180;
                ViewCompat.animate(arrow).rotation(rotation).start();
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        setCurrentDate(new Date());
        final ImageView arrow = findViewById(R.id.date_picker_arrow);

        RelativeLayout datePickerButton = findViewById(R.id.date_picker_button);

        datePickerButton.setOnClickListener(v -> {
            float rotation = isExpanded ? 0 : 180;
            ViewCompat.animate(arrow).rotation(rotation).start();
            isExpanded = !isExpanded;
            appBarLayout.setExpanded(isExpanded, true);
        });

        TextView tv_date_tom_add = findViewById(R.id.tv_date_tom_add);
        onSuccess(0,"");

        Button bt_add_pre = (Button) findViewById(R.id.bt_add_pre);
        bt_add_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_tom_add_pre = (EditText)findViewById(R.id.et_tom_add_pre);
                String pre = et_tom_add_pre.getText().toString().trim();
                System.out.println(pre);
                submitTomPre(ip, db, user, pwd,pre,users,date);
                hintKbTwo(v);
            }
        });

        FloatingActionButton fab_back = (FloatingActionButton) findViewById(R.id.fab_back2);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                finish();
            }
        });
        fab_back.setVisibility(View.VISIBLE);
    }
    private void hintKbTwo(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        if(imm.isActive()&&getCurrentFocus()!=null){
//            if (getCurrentFocus().getWindowToken()!=null) {
//
//               imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
    }

    private void submitTomPre(final String ip, final String db, final String user, final String pwd, final String pre,final String users,final String date){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    // String sql = "insert into student (S_name)values('"+pre+"')";
                    String sql ="insert into notes (date,user,notes) values("+date+",'"+users+"','"+pre+"')";
                    Statement st = (Statement) cn.createStatement();
                    int Res = st.executeUpdate(sql);
                    // Toast.makeText(DateActivity.this, "霍达提示您添加成功", Toast.LENGTH_SHORT).show();
                    System.out.println(Res > 0 ? "插入数据成功" : "插入数据失败");
                    if (Res>0) {
                        onSuccess2(20, 1);
                    } else {
                        onSuccess2(20, 0);
                    }

                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void addTomId(final String ip, final String db, final String user, final String pwd,final String users,final String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    // String sql = "select id from notes where user = '"+users+"' and date = "+date;
                    String sql = "select id from notes where user = '"+users+"'";

                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                   /* String sql = "select max(id) from notes ";
                    System.out.println(sql);
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    rs.next();
                    int mybook = rs.getInt("id");
                    onSuccess2(9, mybook);*/
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        String x=mybook+"";
                        if (x!=null)
                            onSuccess2(9, mybook);
                        else
                            onSuccess2(9, 0X00);
                    }
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    TextView tv_date_tom_add=findViewById(R.id.tv_date_tom_add);
                    Bundle bundle0 = msg.getData();
                    String data0 = bundle0.getString("json");
                    tv_date_tom_add.setText(data0);

                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    //  c.add(Calendar.DAY_OF_MONTH, 1);
                    tv_date_tom_add.setText(sf.format(c.getTime()));
                    break;
                case 1:
                    tv_date_tom_add=findViewById(R.id.tv_date_tom_add);
                    Bundle bundle1 = msg.getData();
                    String data1 = bundle1.getString("json");
                    tv_date_tom_add.setText(data1);
                    break;
                case 9:
                    TextView tv_date_add_id = (TextView)findViewById(R.id.tv_date_add_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle9 = msg.getData();
                    int data9 = bundle9.getInt("json");
                    data9++;
                    System.out.println(data9);
                    tv_date_add_id.setText(data9+"");
                    break;
                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(DateActivity.this, "霍达提示您添加成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DateActivity.this, "霍达提示您添加失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.title);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }
    public void onSuccess(int i, String json) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void onSuccess5(int i, String json1,String json2,String json3,String json4,String json5) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putString("json1", json1);
        bundle.putString("json2", json2);
        bundle.putString("json3", json3);
        bundle.putString("json4", json4);
        bundle.putString("json5", json5);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void onSuccess2(int i, int json) {
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }


    public void add_date(View view){
        date_add.setVisibility(View.VISIBLE);
        date_del.setVisibility(View.GONE);
    }
    public void del_date(View view){
        date_add.setVisibility(View.GONE);
        date_del.setVisibility(View.VISIBLE);
    }

}
