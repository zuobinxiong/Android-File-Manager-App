package com.example.myapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

public class WebActivity extends Activity {
    private WebView webView;
    private RelativeLayout loadingLayout, webLayout;
    private ZoomControls zoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webView = (WebView) findViewById(R.id.webkit);
        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        webLayout = (RelativeLayout) findViewById(R.id.weblayout);
        zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
        WebSettings webSettings = webView.getSettings();
        //set js script
        webSettings.setJavaScriptEnabled(true);
        //excute Asynctask
        new MyAsyncTask().execute("");
    }

    private void reading() {
        String filePath = getIntent().getStringExtra("filePath");
        if (filePath != null) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadData(readWebDataToStringFromPath(filePath, new FileReadOverBack() {
                        @Override
                        public void fileReadOver() {
                        }
                    }), "text/html", HTTP.UTF_8);
                }
            });
        } else {
            new AlertDialog.Builder(WebActivity.this).setTitle("Error").setMessage("Error in getting file path!").setPositiveButton("Back", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WebActivity.this.finish();
                }
            });
        }
    }

    private String readWebDataToStringFromPath(String path, final FileReadOverBack fileReadOverBack) {
        File file = new File(path);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int readCount = 0;
            while ((readCount = inputStream.read(bytes)) > 0) {
                stringBuffer.append(new String(bytes, 0, readCount));
            }
            fileReadOverBack.fileReadOver();
        } catch (FileNotFoundException e) {
            return "File not exist!";
        } catch (IOException e) {
            return "Error reading file!";
        }
        return stringBuffer.toString();
    }

    interface FileReadOverBack {
        void fileReadOver();
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingLayout.setVisibility(View.VISIBLE);
            webLayout.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            reading();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingLayout.setVisibility(View.GONE);
            webLayout.setVisibility(View.VISIBLE);
            zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.zoomIn();
                }
            });
            zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.zoomOut();
                }
            });
        }
    }
}
