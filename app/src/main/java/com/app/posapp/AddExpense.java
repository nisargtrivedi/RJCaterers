package com.app.posapp;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.model.tbl_expense;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.add_expense)
public class AddExpense extends BaseActivity {

    @ViewById
    TextView eDate;
    @ViewById
    EditText edtPrice;
    @ViewById
    EditText edtDetail;
    @ViewById
    Button btnSave;



    @AfterViews
    public void init(){
        eDate.setText(sdf2.format(new Date().getTime()));
    }
    @Click
    public void btnSave(){
        if(TextUtils.isEmpty(edtDetail.getText().toString())){
            showAlert("Please enter expense detail");
        }else if(TextUtils.isEmpty(edtPrice.getText().toString())){
            showAlert("Please enter expense price");
        }else{
            tbl_expense expense=new tbl_expense();
            expense.ExpenseDate=eDate.getText().toString();
            expense.ExpenseName=edtDetail.getText().toString();
            expense.ExpenseAmount=Double.parseDouble(edtPrice.getText().toString());
            expense.setStatus(Entity.STATUS_NEW);
            try {
                dataContext.tblExpenseObjectSet.save(expense);
                showAlert("Save expense details successfully.");
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            edtPrice.setText("");
            edtDetail.setText("");
            eDate.setText(sdf.format(new Date().getTime()));
        }
    }
    @Click
    public void eDate(){
        openDatePicker(eDate);
    }
    private void openDatePicker(TextView tv) {
        int year = 0, month = 0, day = 0;
        Calendar now = Calendar.getInstance();
        year = now.get(java.util.Calendar.YEAR);
        month = now.get(java.util.Calendar.MONTH);
        day = now.get(java.util.Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText(String.format("%1$02d",dayOfMonth) + "-" + String.format("%1$02d",(month + 1)) + "-" +year);
                //tvSelectedData.setText(edtStartDate.getText().toString() + " -> " + edtEndDate.getText().toString());
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);

        menuItem.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.filerList);
        item2.setVisible(false);

        MenuItem item3 = menu.findItem(R.id.print);
        item3.setVisible(false);

        MenuItem item4 = menu.findItem(R.id.action_done);
        item4.setVisible(false);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }

        return true;
    }
}
