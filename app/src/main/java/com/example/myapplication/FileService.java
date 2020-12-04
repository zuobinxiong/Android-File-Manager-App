package com.example.myapplication;

import java.io.File;
import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class FileService extends Service {
    private Looper mLooper;
    private FileHandler mFileHandler;
    private ArrayList<String> mFileName = null;
    private ArrayList<String> mFilePaths = null;
    public static final String FILE_SEARCH_COMPLETED = "com.supermario.file.FILE_SEARCH_COMPLETED";
    public static final String FILE_NOTIFICATION = "com.supermario.file.FILE_NOTIFICATION";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("FileService", "file service is onCreate");
        //new process thread
        HandlerThread mHT = new HandlerThread("FileService", HandlerThread.NORM_PRIORITY);
        mHT.start();
        mLooper = mHT.getLooper();
        mFileHandler = new FileHandler(mLooper);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("FileService", "file service is onStart");
        mFileName = new ArrayList<String>();
        mFilePaths = new ArrayList<String>();
        mFileHandler.sendEmptyMessage(0);
        //notifying searching
        fileSearchNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //cancel notification
        mNF.cancel(R.string.app_name);
    }

    class FileHandler extends Handler {
        public FileHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("FileService", "file service is handleMessage");
            //serch in area
            initFileArray(new File(SearchBroadCast.mServiceSearchPath));
            //search canceled
            if (!MainActivity.isComeBackFromNotification == true) {
                Intent intent = new Intent(FILE_SEARCH_COMPLETED);
                intent.putStringArrayListExtra("mFileNameList", mFileName);
                intent.putStringArrayListExtra("mFilePathsList", mFilePaths);
                //notify when search is done
                sendBroadcast(intent);
            }
        }
    }

    private int m = -1;

    /**
     * callback function
     */
    private void initFileArray(File file) {
        Log.d("FileService", "currentArray is " + file.getPath());

        if (file.canRead()) {
            File[] mFileArray = file.listFiles();
            for (File currentArray : mFileArray) {
                if (currentArray.getName().indexOf(SearchBroadCast.mServiceKeyword) != -1) {
                    if (m == -1) {
                        m++;
                        // back to folder before searching
                        mFileName.add("BacktoSearchBefore");
                        mFilePaths.add(MainActivity.mCurrentFilePath);
                    }
                    mFileName.add(currentArray.getName());
                    mFilePaths.add(currentArray.getPath());
                }
                //if folder, call back method
                if (currentArray.exists() && currentArray.isDirectory()) {
                    //cancel search
                    if (MainActivity.isComeBackFromNotification == true) {
                        return;
                    }
                    initFileArray(currentArray);
                }
            }
        }
    }

    NotificationManager mNF;


    private void fileSearchNotification() {
        Notification mNotification = new Notification(R.drawable.logo, "Searching...", System.currentTimeMillis());
        Intent intent = new Intent(FILE_NOTIFICATION);
        intent.putExtra("notification", "当通知还存在，说明搜索未完成，可以在这里触发一个事件，当点击通知回到Activity之后，可以弹出一个框，提示是否取消搜索!");
        PendingIntent mPI = PendingIntent.getBroadcast(this, 0, intent, 0);
//        mNotification.setLatestEventInfo(this, "在" + SearchBroadCast.mServiceSearchPath + "下搜索", "搜索关键字为" + SearchBroadCast.mServiceKeyword + "【点击可取消搜索】", mPI);
        if (mNF == null) {
            mNF = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        mNF.notify(R.string.app_name, mNotification);
    }
}