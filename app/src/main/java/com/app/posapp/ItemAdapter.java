package com.app.posapp;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.model.Items;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    public ArrayList<Items> items;
    Context context;
    LayoutInflater inflater;

    onItemDelete onItemDelete;
    public ItemAdapter(Context context, ArrayList<Items> items) {
        this.context=context;
        this.items=items;
        inflater=LayoutInflater.from(context);
    }

    public void onDeleteItem(onItemDelete onItemDelete){
        this.onItemDelete=onItemDelete;
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
            view=inflater.inflate(R.layout.item_row,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tvName.setText(items.get(i).ItemName);
        holder.tvUnit.setText(items.get(i).Unit);
        holder.tvPrice.setText(items.get(i).ItemPrice+"");
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(i);
            }
        });
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    context.startActivity(new Intent(context,ActivityItemAdd_.class).
                            putExtra("item",items.get(i).getID()+"")
                    );
                }catch (Exception ex){
                }

            }
        });
        return view;
    }
    public class ViewHolder{
        TextView tvName,tvUnit,tvPrice;
        ImageView imgDelete;
        LinearLayout ll_item;
        public ViewHolder(View v){
            tvName=v.findViewById(R.id.tvName);
            tvUnit=v.findViewById(R.id.tvUnit);
            tvPrice=v.findViewById(R.id.tvPrice);
            imgDelete=v.findViewById(R.id.imgDelete);
            ll_item=v.findViewById(R.id.ll_item);
        }

    }

    public void removeItem(int i){
        new AlertDialog.Builder(context)
                .setTitle("Item Remove")
                .setMessage("Do you really want to remove this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        onItemDelete.onDelete(items.get(i));
                        items.remove(i);
                        notifyDataSetChanged();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
}
