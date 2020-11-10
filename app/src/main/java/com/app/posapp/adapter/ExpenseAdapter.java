package com.app.posapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.ActivityItemAdd_;
import com.app.posapp.R;
import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.model.Items;
import com.app.posapp.model.tbl_expense;

import java.util.ArrayList;

public class ExpenseAdapter extends BaseAdapter {

    public ArrayList<tbl_expense> items;
    Context context;
    LayoutInflater inflater;

    onItemDelete onItemDelete;
    public ExpenseAdapter(Context context, ArrayList<tbl_expense> items) {
        this.context=context;
        this.items=items;
        inflater=LayoutInflater.from(context);
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
        if(view==null){
            view=inflater.inflate(R.layout.expense_row,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tvDate.setText("Date : "+items.get(i).ExpenseDate);
        holder.tvExpenseDetail.setText(items.get(i).ExpenseName);
        holder.tvAmount.setText("Rs. "+String.format("%.2f",items.get(i).ExpenseAmount));

        return view;
    }
    public class ViewHolder{
        TextView tvDate,tvExpenseDetail,tvAmount;
        public ViewHolder(View v){
            tvDate=v.findViewById(R.id.tvDate);
            tvExpenseDetail=v.findViewById(R.id.tvExpenseDetail);
            tvAmount=v.findViewById(R.id.tvAmount);

        }

    }
}
