## ChatInput

[English Document](./README_EN.md)

这是一个聊天界面输入框组件，可以方便地结合 `MessageList` 使用，包含录音，选择图片，拍照等功能，提供了一些丰富的接口和回调供用户使用，
还可以选择自定义样式。

## 下载
提供了以下几种方式添加依赖，只需要选择其中一种即可。

- Gradle
```
compile 'cn.jiguang.imui:chatinput:0.1.0'
```

- Maven

```
<dependency>
  <groupId>cn.jiguang.imui</groupId>
  <artifactId>chatinput</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

- JitPack
> project 下的 build.gradle

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

```

> module 的 build.gradle

```
dependencies {
  compile 'com.github.jpush:imui:0.1.0'
}
```

## 用法
使用 ChatInput 只需要两个步骤。

#### 在 xml 布局文件中引用 ChatInputView：

```
    <cn.jiguang.imui.chatinput.ChatInputView
        android:id="@+id/chat_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:cameraBtnIcon="@drawable/camera"
        app:inputCursorDrawable="@drawable/jmui_text_cursor_bg"
        app:inputEditTextBg="@drawable/jmui_chat_input_bg"
        app:inputHint="@string/input_hint"
        app:photoBtnIcon="@drawable/photo"
        app:sendBtnIcon="@drawable/send"
        app:voiceBtnIcon="@drawable/mic" />

```

#### 初始化 ChatInputView

```
ChatInputView chatInputView = (ChatInputView) findViewById(R.id.chat_input);
chatInputView.setMenuContainerHeight(softInputHeight);
```
**初始化后一定要设置一下 MenuContainer 的高度，最好设置为软键盘的高度，否则会导致第一次打开菜单时高度不正常（此时打开软键盘会导致界面伸缩）。**

建议在跳转到聊天界面之前使用 onSizeChanged 方法监听软键盘的高度，然后在初始化的时候设置即可，
关于监听软键盘高度的方法可以参考 sample 下的 MessageListActivity 及 ChatView 中的 onSizeChanged 相关方法。

## 重要接口及事件
ChatInputView 提供了各种按钮及事件的监听回调，所以用户可以灵活地运用监听事件做一些操作，如发送消息等事件。

#### OnMenuClickListener
首先是输入框下面的菜单栏事件的监听，调用 chatInputView.setMenuClickListener 即可设置监听：
```
chatInput.setMenuClickListener(new OnMenuClickListener() {
    @Override
    public boolean onSendTextMessage(CharSequence input) {
         // 输入框输入文字后，点击发送按钮事件
    }

    @Override
    public void onSendFiles(List<String> list) {
        // 选中文件或者录制完视频后，点击发送按钮触发此事件
    }

    @Override
    public void switchToMicrophoneMode() {
        // 点击语音按钮触发事件，显示录音界面前触发此事件
    }

    @Override
    public void switchToGalleryMode() {
        // 点击图片按钮触发事件，显示图片选择界面前触发此事件
    }

    @Override
    public void switchToCameraMode() {
        // 点击拍照按钮触发事件，显示拍照界面前触发此事件
    }
});
```
关于上述事件的处理，可以参考 sample 中的 MessageListActivity 对于事件的处理。

#### RecordVoiceListener
这是录音的接口，使用方式：

```
mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
mRecordVoiceBtn.setRecordVoiceListener(new RecordVoiceListener() {
    @Override
    public void onStartRecord() {
        // Show record voice interface
        // 设置存放录音文件目录
        File rootDir = mContext.getFilesDir();
        String fileDir = rootDir.getAbsolutePath() + "/voice";
        mRecordVoiceBtn.setRecordVoiceFile(fileDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + "");
    }

    @Override
    public void onFinishRecord(File voiceFile, int duration) {
        MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
        message.setMediaFilePath(voiceFile.getPath());
        message.setDuration(duration);
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        mAdapter.addToStart(message, true);
    }

    @Override
    public void onCancelRecord() {

    }
});
```

#### OnCameraCallbackListener
这是相机相关的接口，使用方式：
```
mChatInput.setOnCameraCallbackListener(new OnCameraCallbackListener() {
    @Override
    public void onTakePictureCompleted(String photoPath) {
        MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);
        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
        message.setMediaFilePath(photoPath);
        message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
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
        // 请注意，点击发送视频的事件会回调给 onSendFiles，这个是在录制完视频后触发的                               
    }
                                       
    @Override
    public void onCancelVideoRecord() {
                                       
    }
});
```


#### 设置拍照后保存的文件
setCameraCaptureFile(String path, String fileName)

```
// 参数分别是路径及文件名，建议在上面的 onCameraClick 触发时调用
mChatInput.setCameraCaptureFile(path, fileName);

```
