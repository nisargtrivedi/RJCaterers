package com.app.posapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.ActivityCashItem;
import com.app.posapp.ActivityDialog_;
import com.app.posapp.ActivityItemAdd_;
import com.app.posapp.ItemAdapter;
import com.app.posapp.R;
import com.app.posapp.Utility.Constants;
import com.app.posapp.Utility.DataContext;
import com.app.posapp.listeners.onAddCart;
import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.model.Items;
import com.app.posapp.model.tbl_cart;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Cash_Item_Adapter extends BaseAdapter {

    public ArrayList<Items> items;
    Context context;
    LayoutInflater inflater;
    DataContext dataContext;
    com.app.posapp.listeners.onItemDelete onItemCart;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public Cash_Item_Adapter(Context context, ArrayList<Items> items) {
        this.context = context;
        this.items = items;
        dataContext=new DataContext(context);
        inflater = LayoutInflater.from(context);
    }

    public void onAddItem(onItemDelete onItemDelete) {
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
            view = inflater.inflate(R.layout.carh_item_row, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvItem.setText(items.get(i).ItemName);
        holder.tvPrice.setText("Rs. "+String.format("%.2f",items.get(i).ItemPrice) + "( "+items.get(i).Unit+" )");

        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(items.get(i).Unit.equalsIgnoreCase("kg")){
                        onItemCart.onDelete(items.get(i));
                    ((ActivityCashItem)context).countCart();
                }else if(items.get(i).Unit.equalsIgnoreCase("pcs.")) {
                    showPcsDialog(items.get(i));
                }

            }
        });
        return view;
    }

    public class ViewHolder {
        Button tvItem;
        TextView tvPrice;
        LinearLayout llCashItem;

        public ViewHolder(View v) {
            tvItem = v.findViewById(R.id.tvItem);
            tvPrice=v.findViewById(R.id.tvPrice);
            llCashItem = v.findViewById(R.id.llCashItem);
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

    public void showPcsDialog(Items items){
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the custom layout
        final View customLayout = inflater.inflate(R.layout.dialog_pcs, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.edtAmount);
        Button btnAdd = customLayout.findViewById(R.id.btnAdd);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(editText.getText().toString())) {
                    if(Integer.parseInt(editText.getText().toString())>0) {
                        try {
                            dataContext.cartObjectSet.fill();
                            if (dataContext.cartObjectSet.size() > 0) {
                                int j = 0;
                                for (tbl_cart cart : dataContext.cartObjectSet) {
                                    if (cart.ItemName.equalsIgnoreCase(items.ItemName)) {
                                        j = 1;
                                        break;
                                    }
                                }
                                if (j == 1) {
                                    dataContext.cartObjectSet.fill("item_name=?", new String[]{items.ItemName}, null);
                                    if (dataContext.cartObjectSet.size() > 0) {
                                        tbl_cart obj = dataContext.cartObjectSet.get(0);
                                        obj.ItemQty = obj.ItemQty + Integer.parseInt(editText.getText().toString());
                                        obj.sellDate = sdf.format(new Date().getTime());
                                        obj.setStatus(Entity.STATUS_UPDATED);
                                        dataContext.cartObjectSet.save();
//                                    Toast.makeText(context, "Quantity Updated", Toast.LENGTH_SHORT)
//                                            .show();
                                    }
                                } else {
                                    tbl_cart cart = new tbl_cart();
                                    cart.ItemName = items.ItemName;
                                    cart.ItemPrice = items.ItemPrice;
                                    cart.ItemQty = Integer.parseInt(editText.getText().toString());
                                    cart.ItemUnit = items.Unit;
                                    cart.sellDate = sdf.format(new Date().getTime());
                                    cart.setStatus(Entity.STATUS_NEW);
                                    addItem((cart));
                                }
                            } else {
                                tbl_cart cart = new tbl_cart();
                                cart.ItemName = items.ItemName;
                                cart.ItemPrice = items.ItemPrice;
                                cart.ItemQty = Integer.parseInt(editText.getText().toString());
                                ;
                                cart.ItemUnit = items.Unit;
                                cart.sellDate = sdf.format(new Date().getTime());
                                cart.setStatus(Entity.STATUS_NEW);
                                addItem((cart));
                            }
                        } catch (AdaFrameworkException e) {
                            e.printStackTrace();
                        }
                    }else
                        Toast.makeText(context,"Please enter number greater than 0",Toast.LENGTH_LONG).show();

                    ((ActivityCashItem)context).countCart();
                    dialog.dismiss();
                }else{
                    Toast.makeText(context,"Please enter number of pcs.",Toast.LENGTH_LONG).show();
                }



            }
        });


        dialog.show();
    }
}
