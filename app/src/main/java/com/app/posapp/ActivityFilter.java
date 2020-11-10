package com.app.posapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.date_filter_dialog)
public class ActivityFilter extends BaseActivity {

    @ViewById
    TextView fromDate;

    @ViewById
    TextView toDate;

    @ViewById
    Button btnGo;

    @AfterViews
    public void init(){

        fromDate.setText(sdf.format(new Date().getTime()));
        toDate.setText(sdf.format(new Date().getTime()));
    }
    @Click
    public void fromDate(){
        openDatePicker(fromDate);
    }

    @Click
    public void toDate(){
        openDatePicker(toDate);
    }
    @Click
    public void btnGo(){
        Intent intent=new Intent();
        intent.putExtra("fromdate",fromDate.getText().toString());
        intent.putExtra("todate",toDate.getText().toString());
        setResult(1001,intent);
        finish();
    }

    private void openDatePicker(TextView tv) {
        int year = 0, month = 0, day = 0;
        Calendar now = Calendar.getInstance();
        year = now.get(java.util.Calendar.YEAR);
        month = now.get(java.util.Calendar.MONTH);
        day = now.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityFilter.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText(year + "-" + String.format("%1$02d",(month + 1)) + "-" + String.format("%1$02d",dayOfMonth));
                //tvSelectedData.setText(edtStartDate.getText().toString() + " -> " + edtEndDate.getText().toString());
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
