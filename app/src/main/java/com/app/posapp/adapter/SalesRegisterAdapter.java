package com.app.posapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.R;
import com.app.posapp.TransactionItem_;
import com.app.posapp.Utility.DataContext;
import com.app.posapp.listeners.onAddCart;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.util.ArrayList;

public class SalesRegisterAdapter extends BaseAdapter {

    public ArrayList<tbl_sales_bill> items;
    Context context;
    LayoutInflater inflater;
    DataContext dataContext;
    onAddCart onItemCart;

    double total=0;
    public SalesRegisterAdapter(Context context, ArrayList<tbl_sales_bill> items) {
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
            view = inflater.inflate(R.layout.sales_register_row, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvBillNo.setText(String.format("%.0f",items.get(i).BillNo));
        holder.tvDate.setText(items.get(i).sellDate);
        if(items.get(i).ItemUnit.equalsIgnoreCase("gram") || items.get(i).ItemUnit.equalsIgnoreCase("kg")){
            total=items.get(i).ItemPrice;
        }else
            total=(items.get(i).ItemQty*items.get(i).ItemPrice);
        holder.tvAmount.setText(String.format("%.2f",total));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TransactionItem_.class).putExtra("billno",items.get(i).BillNo));
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvBillNo;
        TextView tvDate,tvAmount;
        LinearLayout ll;

        public ViewHolder(View v) {
            tvBillNo = v.findViewById(R.id.tvBillNo);
            tvDate=v.findViewById(R.id.tvDate);
            tvAmount = v.findViewById(R.id.tvAmount);
            ll=v.findViewById(R.id.ll);
        }
    }
}
