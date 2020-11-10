package com.app.posapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.adapter.Cash_Item_Adapter;
import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.model.Items;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_sales_bill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.cash_item)
public class ActivityCashItem extends BaseActivity {

    @ViewById
    GridView gvItems;
    @ViewById
    TextView tvCounter;

    TextView cart_badge;
    Cash_Item_Adapter adapter;

    ArrayList<Items> list=new ArrayList<>();

    int cartItem=0;
    @AfterViews
    public void init(){
        try {
            adapter=new Cash_Item_Adapter(this,list);
            gvItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.onAddItem(new onItemDelete() {
                @Override
                public void onDelete(Items items) {
                    startActivityForResult(new Intent(ActivityCashItem.this, ActivityDialog_.class)
                            .putExtra("id",items.getID()+"")
                    ,1000);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataContext.itemsObjectSet.fill();
            list.clear();
            list.addAll(dataContext.itemsObjectSet);
            adapter.notifyDataSetChanged();
            if(list.size()>0) {
                gvItems.setVisibility(View.VISIBLE);

            }
            if(cart_badge!=null)
                countCart();

            showTodaysCounter();
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }

    }

    public void countCart(){
        try {
            dataContext.cartObjectSet.fill();
//            for(tbl_cart cart:dataContext.cartObjectSet) {
//                System.out.println("ITEM NAME===>" + cart.ItemName);
//                System.out.println("ITEM UNIT===>" + cart.ItemQty);
//                System.out.println("ITEM PRICE===>" + cart.ItemPrice);
//            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        cart_badge.setText(dataContext.cartObjectSet.size()+"");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);
        countCart();

        MenuItem item = menu.findItem(R.id.action_done);
        item.setVisible(false);

        MenuItem item2 = menu.findItem(R.id.filerList);
        item2.setVisible(false);
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
            case R.id.action_cart:
                startActivity(new Intent(ActivityCashItem.this,ActivityCartItem_.class));
                break;
            // action with ID action_settings was selected
            case R.id.action_done:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1000){
            countCart();
        }else if(resultCode==RESULT_OK){
            countCart();

        }
    }

    private void showTodaysCounter(){
        try {
            dataContext.salesBillObjectSet.fill("sell_date=?",new String[]{sdf.format(new Date().getTime())},null);
            double total=0;
            for(tbl_sales_bill bill:dataContext.salesBillObjectSet) {
                if (bill.ItemUnit.equalsIgnoreCase("gram") || bill.ItemUnit.equalsIgnoreCase("kg"))
                    total = total + bill.ItemPrice;
                else
                    total = total + (bill.ItemPrice * bill.ItemQty);
            }

//                System.out.println("SALES NO-->"+bill.BillNo);
//                System.out.println("SALES DATE-->"+bill.sellDate);
//                System.out.println("SALES PRICE-->"+bill.ItemPrice);
//                System.out.println("SALES QTY-->"+bill.ItemQty);


            tvCounter.setText("Today Counter Rs. "+String.format("%.2f",total));
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }
}
