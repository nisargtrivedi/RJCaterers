package com.app.posapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.posapp.ActivityCashItem;
import com.app.posapp.R;
import com.app.posapp.Utility.DataContext;
import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.listeners.onMenuClick;
import com.app.posapp.model.Items;
import com.app.posapp.model.tblMenu;
import com.app.posapp.model.tbl_cart;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.util.ArrayList;
import java.util.Date;

public class Menu_Adapter extends BaseAdapter {

    public ArrayList<tblMenu> items;
    Context context;
    LayoutInflater inflater;
    DataContext dataContext;
    onMenuClick onItemCart;

    public Menu_Adapter(Context context, ArrayList<tblMenu> items) {
        this.context = context;
        this.items = items;
        dataContext=new DataContext(context);
        inflater = LayoutInflater.from(context);
    }

    public void onAddItem(onMenuClick onItemDelete) {
        onItemCart = onItemDelete;
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
            view = inflater.inflate(R.layout.admin_menu_row, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvItem.setText(items.get(i).MenuName);
        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCart.onClick(items.get(i));
            }
        });

        return view;
    }

    public class ViewHolder {
        Button tvItem;
        LinearLayout llItem;

        public ViewHolder(View v) {
            tvItem = v.findViewById(R.id.tvItem);
            llItem=v.findViewById(R.id.llItem);


        }
    }
}
