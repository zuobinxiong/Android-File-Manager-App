package com.example.myapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTxtActivity extends Activity implements OnClickListener {
    // text content
    private EditText txtEditText;
    //text file name
    private TextView txtTextTitle;
    //save
    private Button txtSaveButton;
    //cacel
    private Button txtCancleButton;
    private String txtTitle;
    private String txtData;
    private String txtPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_txt);
        //initialization
        initContentView();
        //get path
        txtPath = getIntent().getStringExtra("path");
        //get name
        txtTitle = getIntent().getStringExtra("title");
        //get data
        txtData = getIntent().getStringExtra("data");
        try {
            txtData = new String(txtData.getBytes("ISO-8859-1"), "UTF-8");//
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        txtTextTitle.setText(txtTitle);
        txtEditText.setText(txtData);
    }


    private void initContentView() {
        txtEditText = (EditText) findViewById(R.id.EditTextDetail);
        txtTextTitle = (TextView) findViewById(R.id.TextViewTitle);
        txtSaveButton = (Button) findViewById(R.id.ButtonRefer);
        txtCancleButton = (Button) findViewById(R.id.ButtonBack);

        txtSaveButton.setOnClickListener(this);
        txtCancleButton.setOnClickListener(this);
    }


    public void onClick(View view) {
        if (view.getId() == txtSaveButton.getId()) {

            saveTxt();
        } else if (view.getId() == txtCancleButton.getId()) {
            EditTxtActivity.this.finish();
        }
    }

    private void saveTxt() {
        try {
            String newData = txtEditText.getText().toString();
            BufferedWriter mBW = new BufferedWriter(new FileWriter(new File(txtPath)));
            //write
            mBW.write(newData, 0, newData.length());
            mBW.newLine();
            mBW.close();
            Toast.makeText(EditTxtActivity.this, "File Saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(EditTxtActivity.this, "Error When Saving!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        this.finish();
    }
}
