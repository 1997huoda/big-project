package com.example.huoda.left_ship;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class AddNoteActivity extends AppCompatActivity {

    int mYear, mMonth, mDay;

    TextView et_date;
    final int DATE_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        et_date=findViewById(R.id.et_date);


        FloatingActionButton fab_back2 = (FloatingActionButton) findViewById(R.id.fab_back2);
        fab_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onClick(View view) {
       // showDialog(DATE_DIALOG);
        DatePickerDialog
        pickerDialog = new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        pickerDialog.setTitle("日期");


       // pickerDialog.getDatePicker().setMaxDate(20180505);  //设置日期最大值


        pickerDialog.getDatePicker().setMinDate(20180404); //设置日期最小值
        pickerDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        String a,b,c;
        if(mDay<10)
        {
           c="0"+mDay;
        }
        else
        {
            c=mDay+"";
        }
        if((mMonth+1)<10)
        {
            b="0"+(mMonth+1);
        }
        else
        {
            b=(1+mMonth)+"";
        }
        a=mYear+"";
        et_date.setText(a+b+c);
        //et_date.setText(new StringBuffer().append(mYear).append(mMonth + 1).append(mDay).append(""));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
}
