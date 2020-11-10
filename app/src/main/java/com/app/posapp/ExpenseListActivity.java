package com.app.posapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.adapter.ExpenseAdapter;
import com.app.posapp.model.tbl_expense;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.UUID;

@EActivity(R.layout.expense_list)
public class ExpenseListActivity extends BaseActivity {

    @ViewById
    ListView lvItems;

    @ViewById
    TextView lblMessage;
    @ViewById
            TextView tvTotal;

    double total=0;
    ExpenseAdapter adapter;



    @AfterViews
    public void init(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataContext.tblExpenseObjectSet.fill();
            adapter=new ExpenseAdapter(this,dataContext.tblExpenseObjectSet);
            lvItems.setAdapter(adapter);
            if(dataContext.tblExpenseObjectSet.size()>0){
                lblMessage.setVisibility(View.GONE);
                lvItems.setVisibility(View.VISIBLE);
                for(tbl_expense expense:dataContext.tblExpenseObjectSet){
                   total=total+expense.ExpenseAmount;
                }
                tvTotal.setText("Total expense is Rs. "+String.format("%.2f",total));
            }else{
                lblMessage.setVisibility(View.VISIBLE);
                lvItems.setVisibility(View.GONE);
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }

    @Click
    public void fbAdd(){
        startActivity(new Intent(this,AddExpense_.class));
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
