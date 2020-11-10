package com.app.posapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.adapter.SalesRegisterAdapter;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.activity_sales_register)
public class ActivitySalesRegister extends BaseActivity {

    @ViewById
    ListView gvItems;
    @ViewById
    TextView tvTotal;
    @ViewById
    LinearLayout llMain;

    SalesRegisterAdapter adapter;

    double total=0;
    SQLiteDatabase db;
    ArrayList<tbl_sales_bill> list=new ArrayList<>();
    String sdate,todate;
    @AfterViews
    public void init() {
        try {
            sdate=sdf.format(new Date().getTime());
            todate=sdf.format(new Date().getTime());
            db=dataContext.getReadDataBase();
            list.clear();
            Cursor ds=db.rawQuery("select *,sum(item_price) as price from tbl_sales group by bill_no",null);
            //dataContext.salesBillObjectSet.fill("bill_no desc");
            while (ds.moveToNext()){
                tbl_sales_bill bill=new tbl_sales_bill();
                bill.ItemUnit=ds.getString(ds.getColumnIndex("item_unit"));
                bill.ItemPrice= ds.getDouble(ds.getColumnIndex("price"));
                bill.sellDate=ds.getString(ds.getColumnIndex("sell_date"));
                bill.BillNo=ds.getDouble(ds.getColumnIndex("bill_no"));
                bill.ItemQty=ds.getInt(ds.getColumnIndex("item_qty"));
                bill.ItemName=ds.getString(ds.getColumnIndex("item_name"));

                if(bill.ItemUnit.equalsIgnoreCase("gram") || bill.ItemUnit.equalsIgnoreCase("kg")){
                    total=total+bill.ItemPrice;
                }else
                    total=total+(bill.ItemQty*bill.ItemPrice);
                list.add(bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter=new SalesRegisterAdapter(ActivitySalesRegister.this,list);
        gvItems.setAdapter(adapter);
        tvTotal.setText("Total Sales Amount Rs."+String.format("%.2f",total));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        //cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        MenuItem item = menu.findItem(R.id.filerList);
        item.setVisible(true);

        MenuItem item4 = menu.findItem(R.id.print);
        item4.setVisible(true);

        MenuItem item2 = menu.findItem(R.id.action_done);
        item2.setVisible(false);

        MenuItem item3 = menu.findItem(R.id.action_cart);
        item3.setVisible(false);

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
            case R.id.filerList:
                startActivityForResult(new Intent(ActivitySalesRegister.this,ActivityFilter_.class),1001);
                break;
                case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.print:
                Cursor ds=db.rawQuery("select *,sum(item_price) as price from tbl_sales where sell_date >= '"+sdate+"' and sell_date <= '"+todate+"' group by bill_no",null);
                if(ds.getCount()>0)
                    createExcel(ds);
                else
                    Toast.makeText(getApplication(),
                            "No data found.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1001) {
            bindFilterdata(data.getStringExtra("fromdate"), data.getStringExtra("todate"));
        }
    }
    private void bindFilterdata(String fromdate,String tdate){
        sdate=fromdate;
        todate=tdate;
        total=0;
        list.clear();
        Cursor ds=db.rawQuery("select *,sum(item_price) as price from tbl_sales where sell_date >= '"+fromdate+"' and sell_date <= '"+todate+"' group by bill_no",null);
        while (ds.moveToNext()){
            tbl_sales_bill bill=new tbl_sales_bill();
            bill.ItemUnit=ds.getString(ds.getColumnIndex("item_unit"));
            bill.ItemPrice= ds.getDouble(ds.getColumnIndex("price"));
            bill.sellDate=ds.getString(ds.getColumnIndex("sell_date"));
            bill.BillNo=ds.getDouble(ds.getColumnIndex("bill_no"));
            bill.ItemQty=ds.getInt(ds.getColumnIndex("item_qty"));
            bill.ItemName=ds.getString(ds.getColumnIndex("item_name"));

            if(bill.ItemUnit.equalsIgnoreCase("gram") || bill.ItemUnit.equalsIgnoreCase("kg")){
                total=total+bill.ItemPrice;
            }else
                total=total+(bill.ItemQty*bill.ItemPrice);
            list.add(bill);
        }
        adapter.notifyDataSetChanged();
        tvTotal.setText("Total Sales Amount Rs."+String.format("%.2f",total));
    }



}
