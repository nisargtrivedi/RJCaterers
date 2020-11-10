package com.app.posapp;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.adapter.CartAdapter;
import com.app.posapp.adapter.TransactionCartAdapter;
import com.app.posapp.listeners.onAddCart;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.transaction_detail)
public class TransactionItem extends BaseActivity {

    @ViewById
    ListView gvItems;

    TransactionCartAdapter adapter;

    ArrayList<tbl_cart> list=new ArrayList<>();

    double billNo=0;
    @AfterViews
    public void init(){
        try {
            billNo=getIntent().getDoubleExtra("billno",0);
            dataContext.salesBillObjectSet.fill("bill_no=?",new String[]{billNo+""},null);
            adapter=new TransactionCartAdapter(this,dataContext.salesBillObjectSet);
            gvItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
