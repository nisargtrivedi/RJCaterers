package com.app.posapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.R;
import com.app.posapp.Utility.DataContext;
import com.app.posapp.listeners.onAddCart;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.util.ArrayList;

public class TransactionCartAdapter extends BaseAdapter {

    public ArrayList<tbl_sales_bill> items;
    Context context;
    LayoutInflater inflater;
    DataContext dataContext;
    onAddCart onItemCart;

    public TransactionCartAdapter(Context context, ArrayList<tbl_sales_bill> items) {
        this.context = context;
        this.items = items;
        dataContext=new DataContext(context);
        inflater = LayoutInflater.from(context);
    }

    public void onAddItem(onAddCart onItemDelete) {
        this.onItemCart = onItemDelete;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

       ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.cart_row, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvItem.setText(items.get(i).ItemName);
        holder.tvQty.setText(items.get(i).ItemQty+"");
        holder.tvUnit.setText(items.get(i).ItemUnit+"");
        if(items.get(i).ItemUnit.equalsIgnoreCase("gram") || items.get(i).ItemUnit.equalsIgnoreCase("kg")){
            holder.tvTotal.setText(String.format("%.2f",items.get(i).ItemPrice)+"");
        }else
            holder.tvTotal.setText(String.format("%.2f",(items.get(i).ItemQty*items.get(i).ItemPrice))+"");

//        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                removeItem(i);
//                return false;
//            }
//        });


        return view;
    }

    public class ViewHolder {
        TextView tvItem;
        TextView tvQty,tvUnit,tvTotal;
        LinearLayout ll;

        public ViewHolder(View v) {
            tvItem = v.findViewById(R.id.tvItem);
            tvQty=v.findViewById(R.id.tvQty);
            tvUnit = v.findViewById(R.id.tvUnit);
            tvTotal = v.findViewById(R.id.tvTotal);
            ll=v.findViewById(R.id.ll);
        }
    }
    public void addItem(tbl_cart obj){
        obj.setStatus(Entity.STATUS_NEW);
        try {
            dataContext.cartObjectSet.save(obj);
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }


}
