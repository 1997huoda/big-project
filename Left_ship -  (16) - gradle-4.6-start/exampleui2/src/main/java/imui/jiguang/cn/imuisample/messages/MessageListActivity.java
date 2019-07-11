package imui.jiguang.cn.imuisample.messages;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;

import imui.jiguang.cn.imuisample.R;
import imui.jiguang.cn.imuisample.models.DefaultUser;
import imui.jiguang.cn.imuisample.models.MyMessage;
import imui.jiguang.cn.imuisample.views.ChatView;



public class MessageListActivity extends Activity implements ChatView.OnKeyboardChangedListener,
        ChatView.OnSizeChangedListener, View.OnTouchListener {
    private static final String TAG = "群聊";
    public static String  users,ip,db,user,pwd;
    public static int id;
    public static List<MyMessage> mData;
    public  List<MyMessage> list = new ArrayList<>();
    private boolean Ppp =true;

    private final int REQUEST_RECORD_VOICE_PERMISSION = 0x0001;
    private final int REQUEST_CAMERA_PERMISSION = 0x0002;
    private final int REQUEST_PHOTO_PERMISSION = 0x0003;

    private Context mContext;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
   // private List<MyMessage> mData,mData2;

    private InputMethodManager mImm;
    private Window mWindow;

public DatabaseConnection cnn = new DatabaseConnection();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.chat_activity);

        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setTitle("技术咨询");

        onSuccess(77,"","");

    onSuccess2(9,9);

        mChatView.setKeyboardChangedListener(this);
        mChatView.setOnSizeChangedListener(this);
        mChatView.setOnTouchListener(this);
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {

                if (input.length() == 0) {
                    return false;
                }

                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT);
//                message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                message.setUserInfo(new DefaultUser("1", users, users));
                String time =new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date());
                String what =input.toString();
                message.setTimeString(new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()) );

                insert_text(ip, db, user, pwd,users,time,what,users);

              //  mAdapter.addToStart(message, true);

                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);

                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO);
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    message.setMediaFilePath(item.getFilePath());

//                    message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                    message.setUserInfo(new DefaultUser("1", users, users));

                    final MyMessage fMsg = message;
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            @Override
            public void switchToMicrophoneMode() {
                if ((ActivityCompat.checkSelfPermission(MessageListActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                {
                    ActivityCompat.requestPermissions(MessageListActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_VOICE_PERMISSION);
                }
            }

            @Override
            public void switchToGalleryMode() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MessageListActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PHOTO_PERMISSION);
                }
            }

            @Override
            public void switchToCameraMode() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CAMERA_PERMISSION);
                }

                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/photo";
                mChatView.setCameraCaptureFile(fileDir, "temp_photo");
            }
        });

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // Show record voice interface
                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                mChatView.setRecordVoiceFile(fileDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
                message.setUserInfo(new DefaultUser("1", users, users));
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

                //将发送信息记录到数据库，再发一遍？

                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }
        });

        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
//                message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                message.setUserInfo(new DefaultUser("1", users, users));
                //记录到数据库
                //或者发送到服务器——获得返回信息——url——记录url再发一遍，去掉这个图片发送；


                MessageListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_VOICE_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use record voice feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use take aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PHOTO_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use select aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<MyMessage> getMessages() {
        List<MyMessage> list = new ArrayList<>();
        Resources res = getResources();
        String[] messages = res.getStringArray(R.array.messages_array);
        for (int i = 9; i >= 0; i--) {
            MyMessage message;
            if (i % 2 == 1) {
                message = new MyMessage(messages[i], IMessage.MessageType.RECEIVE_TEXT);
                message.setUserInfo(new DefaultUser("0", "DeadPool", "deadpool"));
                message.setTimeString("11:2" + i);
            } else {
                message = new MyMessage(messages[i], IMessage.MessageType.SEND_TEXT);
                message.setUserInfo(new DefaultUser("1", "IronMan", "ironman"));
                message.setTimeString("11:2" + i);
            }
            // message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            // message.setTimeString("11:20");
            list.add(message);
        }

   // onSuccess(1,"","");

        return list;
    }


    private void initMsgAdapter() {

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                imageView.setImageResource(getResources().getIdentifier(url, "drawable", getPackageName()));
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO
                        || message.getType() == IMessage.MessageType.SEND_VIDEO) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(MessageListActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
//                    Toast.makeText(mContext, mContext.getString(R.string.message_click_hint),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
//                Toast.makeText(mContext, mContext.getString(R.string.message_long_click_hint),
//                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();

                  String name = userInfo.getDisplayName();

               Toast.makeText(mContext,name, Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, mContext.getString(R.string.avatar_click_hint),
//                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message.setUserInfo(new DefaultUser("0", "Deadpool", "deadpool"));
//        message.setTimeString();

        mAdapter.addToEnd(mData);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount < mData.size()) {
                    loadNextPage();
                }
            }
        });

        mChatView.setAdapter(mAdapter);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }

    @Override
    public void onKeyBoardStateChanged(int state) {
        switch (state) {
            case ChatInputView.KEYBOARD_STATE_INIT:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (mImm != null) {
                    mImm.isActive();
                }
                if (chatInputView.getMenuState() == View.INVISIBLE
                        || (!chatInputView.getSoftInputState()
                        && chatInputView.getMenuState() == View.GONE)) {

                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chatInputView.dismissMenuLayout();
                }
                break;
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatView.setMenuHeight(oldh - h);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();

                if (view.getId() == chatInputView.getInputView().getId()) {

                    if (chatInputView.getMenuState() == View.VISIBLE
                            && !chatInputView.getSoftInputState()) {
                        chatInputView.dismissMenuAndResetSoftMode();
                        return false;
                    } else {

                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                if (chatInputView.getSoftInputState()) {
                    View v = getCurrentFocus();

                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        chatInputView.setSoftInputState(false);
                    }
                }
                break;
        }
        return false;
    }
    public void onSuccess2(int i, int json) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }
    public void onSuccess(int i, String json,String js1) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        bundle.putString("js", js1);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }
    public void onSuccess3String(int i, String send,String what,String time) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putString("send", send);
        bundle.putString("what", what);
        bundle.putString("time", time);
        //bundle.putString("users", users);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }
    private void insert_text (final String ip, final String db, final String user, final String pwd, final String users,String time,String what,String send)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                    Class.forName("com.mysql.jdbc.Driver");
                  //  Connection cn = DriverManager.getConnection(url, user, pwd);
                   // Connection cn=getConnection2();
                    Connection cn =cnn.getConnection();

                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    Calendar c = Calendar.getInstance();
                    String date=sf.format(c.getTime());
                    String sql = " insert into chat(date,time,what,send,ob,"+users+") values('"+date+"','"+time+"','"+what+"','"+send+"'"+",1,'n')";
                    Statement st = (Statement) cn.createStatement();
                    int Res = st.executeUpdate(sql);
                    if (Res>0) {
                       // onSuccess3String(200,send,what,time);
                      //  onSuccess2(1,1);//立即调用——————有问题——会显示两边
                        cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                        //cn.close();
                        st.close();
                    } else {
                        Log.i(TAG, "插入：  " + "false_________________________");
//                        cn.close();
                        cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                        st.close();
                        insert_text(ip, db, user, pwd,users,time,what,users);
                        Log.i(TAG, "插入：  " + "return______________________");
                    }
                    cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
//                    cn.close();
                    st.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void read_judge_send(String send,String what,String time){
        if(send.equals(users))
        {
           Log.i(TAG, "自己发的：   " + send);
            MyMessage  message = new MyMessage(what, IMessage.MessageType.SEND_TEXT);
            message.setUserInfo(new DefaultUser("1", users, users));
            message.setTimeString(time) ;
            list.add(message);
        }else {
            Log.i(TAG, "别人发的：   " + send);
            MyMessage message2;
            message2 = new MyMessage(what, IMessage.MessageType.RECEIVE_TEXT);
            message2.setUserInfo(new DefaultUser("1", send, send));
            message2.setTimeString(time);
            list.add(message2);
        }
    }
    public void onSuccess3(int i, int json,String js) {
        // Log.i("Channel", "onSuccess");
        Message message = Message.obtain();
        message.what = i;
        Bundle bundle = new Bundle();
        bundle.putInt("json", json);
        bundle.putString("js", js);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }
public void delay_s(){
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            onSuccess(1,"","");
        }
    }, 1000);
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Ppp=false;
        Log.i(TAG, "执行关闭    " +"destroy" );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myHandler.removeCallbacksAndMessages(null);
       Log.i(TAG, "返回键    " + "按下");
    }



    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 8:
                    mData=list;
                    initMsgAdapter();
                    onSuccess2(1,1);
                    break;
                case 77://昨天 + 今天
                    Log.i(TAG, "77" );
//                    onSuccess2(7,2);//循环
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                                Class.forName("com.mysql.jdbc.Driver");
                              //  Connection cn = DriverManager.getConnection(url, user, pwd);
                                Connection cn =cnn.getConnection();

                                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.DAY_OF_MONTH, -1);//
                                SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMdd");
                                Calendar c2 = Calendar.getInstance();
                                //      sf.format(c.getTime())
                                //  String sql = "select * from chat where  date ="+sf.format(c.getTime())+" and "+users+" = 'y'"+" order by id desc ";
                                String sql = "select send,what,time,id from chat where  date in ("+sf.format(c.getTime())+","+sf2.format(c2.getTime())+") and "+users+" = 'y'"+" order by id desc ";
                                Log.i(TAG, "77:  " + sql);
                                Statement st = (Statement) cn.createStatement();
                                ResultSet rs = st.executeQuery(sql);

//                                rs.next();
//                                String send2 =rs.getString("send");
//                                String what2 =rs.getString("what");
//                                String time2 =rs.getString("time");
//                                id=rs.getInt("id");
//                                read_judge_send(send2,what2,time2,users,list);

                                while(rs.next())
                                {

                                    String send =rs.getString("send");

                                    String what =rs.getString("what");
                                  // Log.i(TAG, send2 + what);
                                    String time =rs.getString("time");
                                   //  int id4=rs.getInt("id");
                                    read_judge_send(send,what,time);
                                }
                                cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                              //  cn.close();
                                st.close();
                                rs.close();
                                // onSuccess3(78,1,users);//今天
                                onSuccess2(7,2);//循环
                            } catch (ClassNotFoundException e) {
//                                onSuccess2(7,2);//循环
                                e.printStackTrace();
                            } catch (SQLException e) {
//                                onSuccess2(7,2);//循环
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
//                case 4:
//                    Bundle bundle4 = msg.getData();
//                    int id4 = bundle4.getInt("json");
//                    id=id4;
//                    break;
                case 200:   // 传入参数，添加一条消息记录
                    Bundle bundle200 = msg.getData();
                    String send200 = bundle200.getString("send");
                    String what200 =bundle200.getString("what");
                    String time200 =bundle200.getString("time");
                    MyMessage message200 = new MyMessage(what200, IMessage.MessageType.SEND_TEXT);
                    message200.setUserInfo(new DefaultUser("1", send200, send200));
                    message200.setTimeString(time200);
                    mAdapter.addToStart(message200, true);
                    break;
                case 1:  //循环
                   Log.i(TAG, "当前的消息id" + id);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                                Class.forName("com.mysql.jdbc.Driver");

//                                Connection cn = DriverManager.getConnection(url, user, pwd);
                                Connection cn =cnn.getConnection();
                               // Connection cn = getConnection();
                                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                                Calendar c = Calendar.getInstance();
                                //   c.add(Calendar.DAY_OF_MONTH, -1);//
                                //      sf.format(c.getTime())
                                String sql = "select send,what,time,id from chat where  "+users+" = 'n'";
                               // String sql = "select send,what,time,id from chat where  id > "+id;
                                Log.i(TAG, "循环语句：  " + sql);
                                Statement st = (Statement) cn.createStatement();
                                ResultSet rs = st.executeQuery(sql);

                                while(rs.next())
                                {
                                    String send =rs.getString("send");
                                    String what =rs.getString("what");
                                    String time =rs.getString("time");
                                    int id4 =rs.getInt("id");
                                    //   read_judge_send2(send,what,time,id,users);
                                    id=id4;
                                    onSuccess3String(3,send,what,time);
                                    onSuccess2(0,id4);//change_n
                                    //调用  结果如上
                                }
                                //  onSuccess3(76,1,users);
//                                cn.close();
                                cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                                st.close();
                                rs.close();
                                onSuccess2(2,2);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 0:     //change_n
                    Bundle bundle0 = msg.getData();
                    int id5 = bundle0.getInt("json");
                   Log.i(TAG, "chang_n:  id_" + id5);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String url = "jdbc:mysql://" + ip + "/" + db+"?useUnicode=true&characterEncoding=UTF-8";
                                Class.forName("com.mysql.jdbc.Driver");
//                                Connection cn = DriverManager.getConnection(url, user, pwd);
                                Connection cn =cnn.getConnection();

                                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                                Calendar c = Calendar.getInstance();
                                //   c.add(Calendar.DAY_OF_MONTH, -1);//
                                //      sf.format(c.getTime())
                                String sql = "update chat set "+users+" = 'y' where id = "+id5;
                                Statement st = (Statement) cn.createStatement();
                                int Res = st.executeUpdate(sql);
//                                if (Res>0) {
//                                    onSuccess2(20, 1);
//                                } else {
//                                    onSuccess2(20, 0);
//                                }
//                                cn.close();
                                cnn.releaseConnection((com.mysql.jdbc.Connection) cn);
                                st.close();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 3://       read_judge_send
                    Bundle bundle3 = msg.getData();
                    String send = bundle3.getString("send");
                    String what =bundle3.getString("what");
                    String time =bundle3.getString("time");
                    // System.out.println(users);
                    if(send.equals(users))
                    {
                        Log.i(TAG, "自己：    " +what );
                        MyMessage  message = new MyMessage(what, IMessage.MessageType.SEND_TEXT);
                        message.setUserInfo(new DefaultUser("1", users, users));
                        message.setTimeString(time) ;
                        mAdapter.addToStart(message, true);
                    }else {
                       Log.i(TAG, "别人：    " +what );
                        MyMessage message2;
                        message2 = new MyMessage(what, IMessage.MessageType.RECEIVE_TEXT);
                        message2.setUserInfo(new DefaultUser("1", send, send));
                        message2.setTimeString(time);
                        mAdapter.addToStart(message2, true);
                    }
                    break;
                case 2: //延时
                    if(Ppp)
                    {
                        delay_s();
                    }
                    break;

                case 7: mData=list;
                    initMsgAdapter();
                    onSuccess2(1,1);
                    break;

                case 20:
                    Bundle bundle20 = msg.getData();
                    int data20 = bundle20.getInt("json");
                    if (data20==1)
                        Toast.makeText(MessageListActivity.this, "霍达提示您添加成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MessageListActivity.this, "霍达提示您添加失败", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

}


