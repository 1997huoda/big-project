package com.example.huoda.left_ship;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.example.huoda.left_ship.image.SmartImageView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;

import java.util.Locale;
import java.util.TimeZone;

import imui.jiguang.cn.imuisample.messages.MessageListActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG ="主界面";
    public String path, response;
    public ImageView imageView;
    public EditText et_url;
    private SharedPreferences sharedPreferences,add_save;
    public ProgressBar load_1;

    private AppBarLayout appBarLayout;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", /*Locale.getDefault()*/Locale.CHINESE);
    private CompactCalendarView compactCalendarView;
    private boolean isExpanded = false;

    private TextView tv,tv_count,tv_date_yes,tv_date_to,tv_date_tom,tv_date_tom_add,tv_date_day;
    private ImageView iv_photo;
    public TextView tv_day_list,tv_day_pre;
    private ListView lv,lv_name;
    public View change_add,change_con,change_del,change_wel,change_edit,change_look,change_share,change_help,change_date_day;
    public View bo_yes,bo_to,bo_tom;
    public View content;
    public Button bt_add_save,bt_add_restore;
    private TextView mTextMessage;

    public DatabaseConnection cnn = new DatabaseConnection();

    public String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
//           Manifest.permission.READ_CONTACTS,  //我们不需要联系人
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" ,
            "android.permissionREQUEST_RECORD_VOICE_PERMISSION",
            "android.permissionREQUEST_CAMERA_PERMISSION",
            "android.permissionREQUEST_PHOTO_PERMISSION"};
    protected static String  users="user1",ip="pcohd.uicp.cn:24967",db0,db="librarydb",db2,user="test",pwd="";
    public int count;
    private BottomNavigationBar bottomNavigationBar;
    private ShapeBadgeItem badgeItem;
    public int state_1=1;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fdv();
        vis();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetGone();
            }
        }, 20);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vis();
            }
        }, 30);
        onSuccess(102,"");
        // Toast.makeText(this,"为了您的体验请开启相应权限", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityCompat.requestPermissions(MainActivity.this, permissions,
                        1);
            }
        }, 900);

        //verifyStoragePermissions(MainActivity.this);

        sharedPreferences = this.getSharedPreferences("userInfo",Context. MODE_PRIVATE);
        add_save = this.getSharedPreferences("saveadd",Context. MODE_PRIVATE);
        users=sharedPreferences.getString("user1", "user1");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("公园植物管理系统");

        FloatingActionButton fab_date = (FloatingActionButton) findViewById(R.id.fab_date);
        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(MainActivity.this,DateActivity.class);
                //用Bundle携带数据
//                Bundle bundle=new Bundle();
//                //传递name参数为tinyphp
//                bundle.putString("users", users);//users是数据库里面列；内容默认是user1
//                bundle.putString("ip", ip);
//                bundle.putString("db", db);
//                bundle.putString("user", user);//user 是数据库登陆账号//我们用的是test
//                bundle.putString("pwd", pwd);
//                intent.putExtras(bundle);
System.out.println("1."+users);
                startActivity(intent);



            }
        });
        fab_date.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                onSuccess(101,day0);
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

            if (isExpanded) {
                //onSuccess(104,"");
                ifExpanded();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SetGone();
                    }
                }, 50);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ifExpanded();
                    }
                }, 100);
            } else{
                NotExpended();
                //  onSuccess(103,"");
            }
            appBarLayout.setExpanded(isExpanded, true);
        });
    }
    public void onClick_add_camera(View view) {
        ImageUtils.showImagePickDialog(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        path=null;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                if(resultCode == RESULT_CANCELED) {
                    return;
                }

                Uri imageUri = data.getData();
                //这里得到图片后做相应操作
                path = UriToPathUtil.getImageAbsolutePath(this,imageUri);
                // Bitmap bitmap2 = data.getParcelableExtra(path);
                Bitmap bitmap2 = BitmapFactory.decodeFile(path);
                Bitmap A=compressBySize(path);
                //  Bitmap A=compressImage(bitmap2);
                System.out.println(bitmap2);
                this.imageView.setImageBitmap(bitmap2);
                onSuccess2(88,8);
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                if(resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
                    Uri imageUriCamera = ImageUtils.imageUriFromCamera;
                    //这里得到图片后做相应操作
                    path = UriToPathUtil.getImageAbsolutePath(this,imageUriCamera);
                    Bitmap B=compressBySize(path);

//                    ContentValues values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, name);
//                    values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
//                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");


                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    this.imageView.setImageBitmap(B);
                    onSuccess2(88,9);
                }

                break;
            default:
                break;
        }
    }
    public static Bitmap compressBySize(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) 300);
        int heightRatio = (int) Math.ceil(imgHeight / (float) 300);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        //设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }
    public static void saveFile(Bitmap bm, String fileName) throws Exception {
//        File dirFile = new File(fileName);
//        //检测图片是否存在
//        if (dirFile.exists()) {
//            dirFile.delete();  //删除原图片
//        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }
    //    public static Bitmap compressImage(Bitmap image) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
    public void SetGone(){
        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.GONE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);

        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.GONE);
    }
    public void onClick_Add_Note() {
        Intent intent =new Intent(MainActivity.this,AddNoteActivity.class);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("users", users);
        bundle.putString("ip", ip);
        bundle.putString("db", db);
        bundle.putString("user", user);
        bundle.putString("pwd", pwd);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void fdv(){
        change_add = findViewById(R.id.change_add);
        change_con = findViewById(R.id.change_con);
        change_del = findViewById(R.id.change_del);
        change_wel = findViewById(R.id.change_wel);
        change_edit = findViewById(R.id.change_edit);
        change_look = findViewById(R.id.change_look);

        change_share = findViewById(R.id.change_share);
        change_help = findViewById(R.id.change_help);
        change_date_day = findViewById(R.id.change_date_day);
        bo_yes = findViewById(R.id.bo_yes);
        bo_to = findViewById(R.id.bo_to);
        bo_tom = findViewById(R.id.bo_tom);
        tv_date_tom_add = findViewById(R.id.tv_date_tom_add);
        tv_date_tom = findViewById(R.id.tv_date_tom);
        tv_date_to = findViewById(R.id.tv_date_to);
        tv_date_yes = findViewById(R.id.tv_date_yes);
        tv_date_day = findViewById(R.id.tv_date_day);

        tv_day_list =(TextView)findViewById(R.id.tv_day_list);
        tv_day_pre =(TextView)findViewById(R.id.tv_day_pre);
        lv = (ListView) findViewById(R.id.lv);

        iv_photo=findViewById(R.id.iv_photo);
        bt_add_save=findViewById(R.id.bt_add_save);
        bt_add_restore= findViewById(R.id.bt_add_restore);
        imageView=findViewById(R.id.imageView);
        et_url=findViewById(R.id.et_url);
        load_1=findViewById(R.id.load_1);
    }
    public void vis(){
        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.VISIBLE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);

        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.GONE);
        bo_yes.setVisibility(View.GONE);
        bo_to.setVisibility(View.VISIBLE);
        bo_tom.setVisibility(View.GONE);

        load_1.setVisibility(View.GONE);


    }
    public void ifExpanded(){
        SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c3 = Calendar.getInstance();
        tv_date_day.setText(sf3.format(c3.getTime()));
        SimpleDateFormat sf4 = new SimpleDateFormat("yyyyMMdd");
        Calendar c4 = Calendar.getInstance();
        String date_to = sf4.format(c4.getTime());
        if(change_add.getVisibility()==(View.VISIBLE))
        {state_1 = 1;}
        if(change_con.getVisibility()==(View.VISIBLE))
        {state_1 = 2;}
        if(change_wel.getVisibility()==(View.VISIBLE))
        {state_1 = 3;}
        if(change_del.getVisibility()==(View.VISIBLE))
        {state_1 = 4;}
        if(change_edit.getVisibility()==(View.VISIBLE))
        {state_1 = 5;}
        if(change_look.getVisibility()==(View.VISIBLE))
        {state_1 = 6;}
        if(change_share.getVisibility()==(View.VISIBLE))
        {state_1 = 8;}
        if(change_help.getVisibility()==(View.VISIBLE))
        {state_1 = 9;}

        change_add.setVisibility(View.GONE);
        change_con.setVisibility(View.GONE);
        change_wel.setVisibility(View.GONE);
        change_del.setVisibility(View.GONE);
        change_edit.setVisibility(View.GONE);
        change_look.setVisibility(View.GONE);

        change_share.setVisibility(View.GONE);
        change_help.setVisibility(View.GONE);
        change_date_day.setVisibility(View.VISIBLE);

        getdayId(date_to);
        getdayId2(date_to);
        getdayList(date_to);
        getdayPre(date_to);

    }
    public void NotExpended(){
        SimpleDateFormat sf4 = new SimpleDateFormat("yyyyMMdd");
        Calendar c4 = Calendar.getInstance();
        String date_to = sf4.format(c4.getTime());
        change_date_day.setVisibility(View.GONE);
        System.out.println(state_1);
        switch (state_1){
            case 1:
                change_add.setVisibility(View.VISIBLE);
                break;
            case 2:
                change_con.setVisibility(View.VISIBLE);
                break;
            case 3:
                change_wel.setVisibility(View.VISIBLE);
                break;
            case 4:
                change_del.setVisibility(View.VISIBLE);
                break;
            case 5:
                change_edit.setVisibility(View.VISIBLE);
                break;
            case 6:
                change_look.setVisibility(View.VISIBLE);
                break;

            case 8:
                change_share.setVisibility(View.VISIBLE);
                break;
            case 9:
                change_help.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this,"错误代码，无法识别", Toast.LENGTH_SHORT).show();
                break;
        }


    }
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
    public void onSuccess2(int i, int json) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    public void onSuccess3(int i, int json,String js) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        bundle.putString("js", js);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    private void getYesId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
//                   Connection cn =cnn.getConnection();
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select id from tips where users = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(0, mybook);
                    }
//                    cn.close();
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getYesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where users = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(1, mybook);
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
    private void getYesPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(2, mybook);
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
    private void getTodayId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from tips where users = '"+users+"' and date = "+sf.format(c2.getTime());
                    System.out.println(sql);
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(3, mybook);
                    }
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getTodayList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    // c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where users = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(4, mybook);
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
    private void getTodayPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    //c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(5, mybook);
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
    private void getTomId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select id from tips where users = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(6, mybook);
                    }
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getTomList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select tips from tips where users = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(7, mybook);
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
    private void getTomPre() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(8, mybook);
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
    private void getYesId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c.getTime());

                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(10, mybook);
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
    private void getTodayId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c2.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(11, mybook);
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
    private void getTomId2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    String sql = "select id from notes where user = '"+users+"' and date = "+sf.format(c.getTime());
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(12, mybook);
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
    private void getdayId(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    // SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    // Calendar c2 = Calendar.getInstance();
                    String sql = "select id from tips where users = '"+users+"' and date = "+date ;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        String x=mybook+"";
                        if (x!=null)
                            onSuccess2(13, mybook);
                        else
                            onSuccess2(13, 0X00);
                    }
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st.close();
                    rs.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(20, 0);
                }
            }
        }).start();
    }
    private void getdayId2(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
                    String sql = "select id from notes where user = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next())
                    {
                        int mybook = rs.getInt("id");
                        onSuccess2(16, mybook);
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
    private void getdayList(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    // c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select tips from tips where users = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("tips");
                        onSuccess(14, mybook);
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
    private void getdayPre(String date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db;
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    //c.add(Calendar.DAY_OF_MONTH, -1);
                    String sql = "select notes from notes where user = '"+users+"' and date = "+date;
                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String mybook = rs.getString("notes");
                        onSuccess(15, mybook);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, Connect.class));
            return true;
        }
        if (id == R.id.action_help) {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            return true;
        }
        if (id == R.id.action_add) {
            onClick_Add_Note();
            return true;
        }
        if (id == R.id.action_IMUI) {
            qqUI();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Handler handler = new Handler();
        if (id == R.id.nav_camera) {
            //  change_add
            //onSuccess(110,"");
            onSuccess(110,"");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(110,"");
                }
            }, 60);


        } else if (id == R.id.nav_gallery) {
            /*tv.setText("hi");*/

        } else if (id == R.id.nav_slideshow) {
            //  change_del
            onSuccess(105,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(61,"");
                }
            }, 10);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(105,"");
                }
            }, 60);

        } else if (id == R.id.nav_manage) {
            //  change_look
            onSuccess(112,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(112,"");
                }
            }, 60);

        } else if (id == R.id.nav_share) {
            //  startActivity(new Intent(MainActivity.this, Login.class));//红色部分为要打开的新窗口的类名
            //  change_share
            onSuccess(113,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(113,"");
                }
            }, 60);


            final ConstraintLayout cut2=(ConstraintLayout)findViewById(R.id.cut2);
            final ImageView im_logo2=(ImageView)findViewById(R.id.im_logo2);
            //   final TextView tv = (TextView) findViewById(R.id.tv_logo);
            //  tv.setBackgroundColor(Color.GREEN);
            // tv.setDrawingCacheEnabled(true);

            final Runnable runnable = new Runnable() {
                SmartImageView smart_v=findViewById(R.id.smart_v);
                @Override
                public void run() {
//                    viewSaveToImage(im_logo2);
                    viewSaveToImage(smart_v);
//                    smart_v
                }
            };
            Button button = (Button) findViewById(R.id.bt_share);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new Handler().post(runnable);
                }
            });
            Button button2 = (Button) findViewById(R.id.bt_share2);
            button2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "总想祭天");//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_TEXT, "我觉得公园植物管理系统非常好用");//添加分享内容
                    share_intent = Intent.createChooser(share_intent, "分享公园植物管理系统");
                    MainActivity.this.startActivity(share_intent);



                }
            });

        } else if (id == R.id.nav_send) {
            //  change_wel
            onSuccess(114,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(114,"");
                }
            }, 60);


        } else if (id == R.id.home) {
            //  change_wel
            onSuccess(115,"");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetGone();
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess(115,"");
                }
            }, 60);


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setScrimColor(Color.TRANSPARENT);
        return true;
    }
    public void connect(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
    private class MyAdapter extends BaseAdapter {

        /**
         *获取列表里面一共有多少条记录
         * @return 返回记录总数
         */
        @Override
        public int getCount() {

            return 1000000;
        }

        /**
         * 返回一个view对象，这个view对象显示在指定的位置
         * @param position
         *          item的位置
         * @param convertView
         *          回收的view
         * @param parent
         *          父容器
         * @return  返回的view对象
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = new TextView(MainActivity.this);
                //  System.out.println("创建新的view:" + position);
            } else {
                tv = (TextView) convertView;
                //  System.out.println("使用回收的view:" + position);
            }
            tv.setText("我是文本：" + position);
            tv.setTextColor(Color.RED);
            tv.setTextSize(20);
            return tv;
        }

        /**
         * 获取一条item
         * @param position
         *          item的位置
         * @return  item
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * 获取一条item的id
         * @param position
         *          item的位置
         * @return  item的id
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // mTextMessage.setText(R.string.title_home);
                    // Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    onSuccess(60,"");
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    onSuccess(61,"");
                    return true;
                case R.id.navigation_notifications:
                    //  mTextMessage.setText(R.string.title_notifications);
                    onSuccess(62,"");
                    return true;
            }
            return false;
        }
    };

    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "test", "你对图片的描述");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void SaveToImage(View view){
        SmartImageView smart_v=findViewById(R.id.smart_v2);
        viewSaveToImage(smart_v);
    }
    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
                ""));

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(sdRoot, "test2.PNG");
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
        Toast.makeText(this, " 霍达提示您保存成功!", Toast.LENGTH_SHORT).show();

    }
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.BLUE);

        // 水印的字体大小
        p.setTextSize(30);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在中间位置开始添加水印
        canvas.drawText(str, w-8*w / 9, h / 9, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }
    public void add_restore(View view){
        String et1=add_save.getString("et1", "");
        String et2=add_save.getString("et2", "");
        String et3=add_save.getString("et3", "");
        String et4=add_save.getString("et4", "");
        String et5=add_save.getString("et5", "");
        String eturl=add_save.getString("eturl", "");

        EditText et_1 = (EditText)findViewById(R.id.et_1);
        et_1.setText(et1);
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        et_2.setText(et2);
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        et_3.setText(et3);
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        et_4.setText(et4);
        EditText et_5 = (EditText)findViewById(R.id.et_5);
        et_5.setText(et5);
        EditText et_url = (EditText)findViewById(R.id.et_url);
        et_url.setText(eturl);
    }
    public void add_save(View view){
        EditText et_1 = (EditText)findViewById(R.id.et_1);
        String et1 = et_1.getText().toString().trim();
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        String et2 = et_2.getText().toString().trim();
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        String et3 = et_3.getText().toString().trim();
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        String et4 = et_4.getText().toString().trim();
        EditText et_5 = (EditText)findViewById(R.id.et_5);
        String et5 = et_5.getText().toString().trim();
        EditText et_url = (EditText)findViewById(R.id.et_url);
        String eturl = et_url.getText().toString().trim();
        SharedPreferences.Editor editor = add_save.edit();
        editor.putString("et1", et1);
        editor.putString("et2", et2);
        editor.putString("et3", et3);
        editor.putString("et4", et4);
        editor.putString("et5", et5);
        editor.putString("et5", et5);
        editor.putString("eturl", eturl);
        editor.commit();
        if ((add_save.getString("et5", "").equals(et5))&&
                (add_save.getString("et4", "").equals(et4))&&
                (add_save.getString("et3", "").equals(et3))&&
                (add_save.getString("et2", "").equals(et2))&&
                (add_save.getString("et1", "").equals(et1)) &&
                (add_save.getString("eturl", "").equals(eturl))
                )
        {
//            onSuccess2(89,0);
            Toast.makeText(this,"霍达提示您保存成功", Toast.LENGTH_SHORT).show();
        }

    }
    public void add_clear(View view){
        String et1="";
        String et2="";
        String et3="";
        String et4="";
        String et5="";
        String eturl="";
        EditText et_1 = (EditText)findViewById(R.id.et_1);
        et_1.setText(et1);
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        et_2.setText(et2);
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        et_3.setText(et3);
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        et_4.setText(et4);
        EditText et_5 = (EditText)findViewById(R.id.et_5);
        et_5.setText(et5);
        EditText et_url = (EditText)findViewById(R.id.et_url);
        et_url.setText(eturl);
    }
    public void add_clear(){
        String et1="";
        String et2="";
        String et3="";
        String et4="";
        String et5="";
        String eturl="";
        EditText et_1 = (EditText)findViewById(R.id.et_1);
        et_1.setText(et1);
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        et_2.setText(et2);
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        et_3.setText(et3);
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        et_4.setText(et4);
        EditText et_5 = (EditText)findViewById(R.id.et_5);
        et_5.setText(et5);
        EditText et_url = (EditText)findViewById(R.id.et_url);
        et_url.setText(eturl);
    }
    public void addInfo(View view){
        EditText et_1 = (EditText)findViewById(R.id.et_1);
        String et1 = et_1.getText().toString().trim();
        EditText et_2 = (EditText)findViewById(R.id.et_2);
        String et2 = et_2.getText().toString().trim();
        EditText et_3 = (EditText)findViewById(R.id.et_3);
        String et3 = et_3.getText().toString().trim();
        EditText et_4 = (EditText)findViewById(R.id.et_4);
        String et4 = et_4.getText().toString().trim();
        EditText et_5 = (EditText)findViewById(R.id.et_5);
        String et5 = et_5.getText().toString().trim();
        EditText et_url = (EditText)findViewById(R.id.et_url);
        String eturl = et_url.getText().toString().trim();
        SharedPreferences.Editor editor = add_save.edit();
        editor.putString("et1", et1);
        editor.putString("et2", et2);
        editor.putString("et3", et3);
        editor.putString("et4", et4);
        editor.putString("et5", et5);
//        editor.putString("eturl", eturl);
        editor.commit();

        add(et1,et2,et3,et4,et5,eturl);
        hintKbTwo(view);
        update();

    }
    private void add(final String et1,final String et2,final String et3,final String et4,final String et5,final String eturl) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     String www =et_url.getText().toString().trim();
                    String w1=add_save.getString("eturl", "");
                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c2 = Calendar.getInstance();
//                    sf.format(c2.getTime());
                    String sql1 = "insert into plants (date,name,trunk,leaves,insect,others,url,users) values ('"+ sf.format(c2.getTime())+"','"+et1+"','"+et2+"','"+et3+"','"+et4+"','"+et5+"','"+w1+"','"+users+"')";
                    System.out.println(sql1);
                    Statement st1 = (Statement) cn.createStatement();
                    int Res1 = st1.executeUpdate(sql1);

                    onSuccess2(21,1);
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                    st1.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    onSuccess2(21,0);
                } catch (SQLException e) {
                    e.printStackTrace();
                    onSuccess2(21,0);
                }
            }
        }).start();
    }
    private void hintKbTwo(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//// 获取软键盘的显示状态
//        boolean isOpen=imm.isActive();
//
//// 如果软键盘已经显示，则隐藏，反之则显示
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//// 隐藏软键盘
//        imm.hideSoftInputFromWindow(view, InputMethodManager.HIDE_NOT_ALWAYS);
//
//// 强制显示软键盘
//        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//
//// 强制隐藏软键盘
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        if(imm.isActive()&&getCurrentFocus()!=null){
//            if (getCurrentFocus().getWindowToken()!=null) {
//
//               imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
    }
    private void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection cn =cnn.getConnection();
                    // String sql = "insert into student (S_name)values('"+pre+"')";
//                    String sql ="insert into notes (date,user,notes) values("+date+",'"+users+"','"+pre+"')";
                    String sql ="update plantc set wf = 'n'" ;
                    Statement st = (Statement) cn.createStatement();
                    int Res = st.executeUpdate(sql);
                    System.out.println(Res > 0 ? "更新wf成功" : "更新wf失败");


                    String sql2 ="update plantc set cf = 'n'" ;
                    Statement st2 = (Statement) cn.createStatement();
                    int Res2 = st2.executeUpdate(sql2);
                    System.out.println(Res2 > 0 ? "更新cf成功" : "更新cf失败");


                    String sql3 ="update plantc set ff = 'n'" ;
                    Statement st3 = (Statement) cn.createStatement();
                    int Res3 = st3.executeUpdate(sql3);
                    System.out.println(Res3 > 0 ? "更新ff成功" : "更新ff失败");


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
    private void qqUI()
    {
        //startActivity(new Intent(MainActivity.this, MessageListActivity.class));
        onSuccess(70,"");
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 90:
                    load_1=findViewById(R.id.load_1);
                    load_1.setVisibility(View.GONE);
                    break;
                case 88:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://pcohd.oicp.io:33667/WebServer/servlet/UploadShipServlet";
                            System.out.println(path);
                            response =UploadUtil.uploadFile(new File(path),url);
                            if(response!=null) {
                                // et_url.setText(response);
                                onSuccess2(90,9);
                                SharedPreferences.Editor editor = add_save.edit();
                                editor.putString("eturl", response);
                                editor.apply();
//                                EditText et_url = (EditText)findViewById(R.id.et_url);
//                                et_url.setText(response);
                                System.out.println(response);
                            }

                        }
                    }).start();
                    load_1=findViewById(R.id.load_1);
                    load_1.setVisibility(View.VISIBLE);
                    break;

                case 70:
                    // Intent intent =new Intent(MainActivity.this,MessageListActivity.class);

                    Intent intent = new Intent(MainActivity.this, MessageListActivity.class);
//用Bundle携带数据
                    // MessageListActivity.mData = mData;
                    MessageListActivity.ip = ip;
                    MessageListActivity.db = db;
                    MessageListActivity.user = user;
                    MessageListActivity.users = users;
                    MessageListActivity.pwd = pwd;

                    Bundle bundle=new Bundle();
//传递name参数为tinyphp
                    bundle.putString("users", users);//users是数据库里面列；内容默认是user1
                    bundle.putString("ip", ip);
                    bundle.putString("db", db);
                    bundle.putString("user", user);//user 是数据库登陆账号//我们用的是test
                    bundle.putString("users", users);//users 是用户名
                    bundle.putString("pwd", pwd);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    // startActivity(new Intent(MainActivity.this, MessageListActivity.class));
                    break;

                case 60://设置昨天的
                    bo_yes.setVisibility(View.VISIBLE);
                    bo_to.setVisibility(View.GONE);
                    bo_tom.setVisibility(View.GONE);
                    getYesList();
                    getYesPre();
                    getYesId();
                    getYesId2();
                    break;
                case 61://设置今天的
                    bo_yes.setVisibility(View.GONE);
                    bo_to.setVisibility(View.VISIBLE);
                    bo_tom.setVisibility(View.GONE);
                    getTodayList();
                    getTodayPre();
                    getTodayId();
                    getTodayId2();
                    break;
                case 62://设置明天的
                    bo_yes.setVisibility(View.GONE);
                    bo_to.setVisibility(View.GONE);
                    bo_tom.setVisibility(View.VISIBLE);
                    getTomList();
                    getTomPre();
                    getTomId();
                    getTomId2();
                    break;
                case 233:
//                    Toast.makeText(this,"nihao", Toast.LENGTH_SHORT).show();
                    break;
                case 110:
                    change_add.setVisibility(View.VISIBLE);
                    add_clear();
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);

                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 111:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.VISIBLE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);

                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 112:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.VISIBLE);

                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "jdbc:mysql://" + ip + "/" + db;
                                //  System.out.println(url);
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection cn =cnn.getConnection();
                                String sql = "select * from student";
                                Statement st = (Statement) cn.createStatement();
                                ResultSet rs = st.executeQuery(sql);
                                System.out.println("成功读取数据库！！");
                                while (rs.next()) {
                                    String mybook = rs.getString("S_Name");
                                    //  System.out.println(mybook);
                                    int id = rs.getInt("id");
                                    String s = String.valueOf(id);
                                    tv_count.setText(s);
                                    System.out.println(id);
                                }
                                cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                                st.close();
                                rs.close();
                                System.out.println("数据库关闭");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    lv.setAdapter(new MyAdapter());
                    lv_name.setAdapter(new MyAdapter());
                    break;
                case 113:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.GONE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);

                    change_share.setVisibility(View.VISIBLE);
                    onSuccess(100,"");
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 114:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.VISIBLE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);

                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 115:
                    change_add.setVisibility(View.GONE);
                    change_con.setVisibility(View.GONE);
                    change_wel.setVisibility(View.VISIBLE);
                    change_del.setVisibility(View.GONE);
                    change_edit.setVisibility(View.GONE);
                    change_look.setVisibility(View.GONE);

                    change_share.setVisibility(View.GONE);
                    change_help.setVisibility(View.GONE);
                    change_date_day.setVisibility(View.GONE);
                    break;
                case 116:
                    break;
                case 117:
                    break;
                case 118:
                    break;
                case 105:
                    vis();
                    break;
                case 104:
                    ifExpanded();
                    break;
                case 103:
                    NotExpended();
                    break;
                case 102:
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    tv_date_to.setText(sf.format(c.getTime()));
                    //  System.out.println(“当前日期：               ”+sf.format(c.getTime()));
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    //   System.out.println(“增加一天后日期 ：  ”+sf.format(c.getTime()));
                    tv_date_tom.setText(sf.format(c.getTime()));
                    //  tv_date_tom_add.setText(sf.format(c.getTime()));
                    c.add(Calendar.DAY_OF_MONTH, -2);
                    tv_date_yes.setText(sf.format(c.getTime()));
                    break;
                case 101:
                    Bundle bundle101 = msg.getData();
                    String day0 = bundle101.getString("json");
                    tv_date_day.setText(day0);
                    //refresh
                   TextView tv_day_id2 =(TextView)findViewById(R.id.tv_day_id2);
                    TextView tv_day_id =(TextView)findViewById(R.id.tv_day_id);
                    tv_day_id.setText("id");
                    tv_day_id2.setText("id");
                    tv_day_list.setText("系统提示");
                    tv_day_pre.setText("待办事项");
                    //get
                    getdayId(day0);
                    getdayId2(day0);
                    getdayList(day0);
                    getdayPre(day0);
                    break ;
                case 100:
                    SmartImageView siv = (SmartImageView) findViewById(R.id.smart_v);
                    siv.setImageUrl("http://pcohd.oicp.io:33667/WebServer/image/share.png", R.drawable.ic_menu_camera);
                    SmartImageView siv2 = (SmartImageView) findViewById(R.id.smart_v2);
                    siv2.setImageUrl("http://pcohd.oicp.io:33667/WebServer/image/share.png", R.drawable.ic_menu_camera);
                    break;
                case 99:
                    // vis();
                    break;
                case 0:
                    TextView tv_yes_id = (TextView)findViewById(R.id.tv_yes_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle0 = msg.getData();
                    int data0 = bundle0.getInt("json");
                    tv_yes_id.setText(data0+"");
                    break;
                case 1:
                    TextView tv_yes_list = (TextView)findViewById(R.id.tv_yes_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle1 = msg.getData();
                    String data1 = bundle1.getString("json");
                    tv_yes_list.setText(data1);
                    break;
                case 2:
                    TextView tv_yes_pre = (TextView)findViewById(R.id.tv_yes_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle2 = msg.getData();
                    String data2 = bundle2.getString("json");
                    tv_yes_pre.setText(data2);
                    break;
                case 3:
                    TextView tv_day2_id = (TextView)findViewById(R.id.tv_today_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle3 = msg.getData();
                    int data3 = bundle3.getInt("json");
                    System.out.println(data3);
                    tv_day2_id.setText(data3+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;

//                TextView tv_day_id = (TextView)findViewById(R.id.tv_day_id);
//                //完成主界面更新,拿到数据
//                Bundle bundle3 = msg.getData();
//                int data3 = bundle3.getInt("json");
//                tv_day_id.setText(data3+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
//                break;


                case 4:
                    TextView tv_today_list = (TextView)findViewById(R.id.tv_today_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle4 = msg.getData();
                    String data4 = bundle4.getString("json");
                    tv_today_list.setText(data4);
                    break;
                case 5:
                    TextView tv_today_pre = (TextView)findViewById(R.id.tv_today_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle5 = msg.getData();
                    String data5 = bundle5.getString("json");
                    tv_today_pre.setText(data5);
                    break;
                case 6:
                    TextView tv_tom_id = (TextView)findViewById(R.id.tv_tom_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle6 = msg.getData();
                    int data6 = bundle6.getInt("json");
                    tv_tom_id.setText(data6+"");
                    break;
                case 7:
                    TextView tv_tom_list = (TextView)findViewById(R.id.tv_tom_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle7 = msg.getData();
                    String data7 = bundle7.getString("json");
                    tv_tom_list.setText(data7);
                    break;
                case 8:
                    TextView tv_tom_pre = (TextView)findViewById(R.id.tv_tom_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle8 = msg.getData();
                    String data8 = bundle8.getString("json");
                    tv_tom_pre.setText(data8);
                    break;
                case 9:
                    TextView tv_date_add_id = (TextView)findViewById(R.id.tv_date_add_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle9 = msg.getData();
                    int data9 = bundle9.getInt("json");
                    System.out.println(data9);
                    tv_date_add_id.setText(data9+"");
                    break;
                case 10:
                    TextView tv_yes_id2 = (TextView)findViewById(R.id.tv_yes_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle10 = msg.getData();
                    int data10 = bundle10.getInt("json");
                    tv_yes_id2.setText(data10+"");
                    break;
                case 11:
                    TextView tv_today_id2 = (TextView)findViewById(R.id.tv_today_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle11 = msg.getData();
                    int data11 = bundle11.getInt("json");
                    tv_today_id2.setText(data11+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 12:
                    TextView tv_tom_id2 = (TextView)findViewById(R.id.tv_tom_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle12 = msg.getData();
                    int data12 = bundle12.getInt("json");
                    tv_tom_id2.setText(data12+"");
                    break;
                case 13:
                    TextView tv_day1_id = (TextView)findViewById(R.id.tv_day_id);
                    //完成主界面更新,拿到数据
                    Bundle bundle13 = msg.getData();
                    int data13 = bundle13.getInt("json");
                    tv_day1_id.setText(data13+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 14:
                    TextView tv_day_list = (TextView)findViewById(R.id.tv_day_list);
                    //完成主界面更新,拿到数据
                    Bundle bundle14 = msg.getData();
                    String data14 = bundle14.getString("json");
                    tv_day_list.setText(data14);
                    break;
                case 15:
                    TextView tv_day_pre = (TextView)findViewById(R.id.tv_day_pre);
                    //完成主界面更新,拿到数据
                    Bundle bundle15 = msg.getData();
                    String data15 = bundle15.getString("json");
                    tv_day_pre.setText(data15);
                    break;
                case 16:
                    TextView tv_day_id9 = (TextView)findViewById(R.id.tv_day_id2);
                    //完成主界面更新,拿到数据
                    Bundle bundle16 = msg.getData();
                    int data16 = bundle16.getInt("json");
                    tv_day_id9.setText(data16+"");//setText参数如果是int类型，一定要在最后加上 +""，否则报错
                    break;
                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(MainActivity.this, "霍达提示您连接成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "霍达提示您连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 22:
                    Bundle bundle22 = msg.getData();
                    int data22 = bundle22.getInt("json");
                    if (data22==1)
                        Toast.makeText(MainActivity.this, "霍达提示您添加用户成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "霍达提示您添加用户失败", Toast.LENGTH_SHORT).show();
                    break;
                case 21:
                    Bundle bundle21 = msg.getData();
                    int data21 = bundle21.getInt("json");
                    if (data21==1)
                        Toast.makeText(MainActivity.this, "霍达提示您上报成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "霍达提示您上报失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
