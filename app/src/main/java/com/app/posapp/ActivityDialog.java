package com.app.posapp;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.model.Items;
import com.app.posapp.model.tbl_cart;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

@EActivity(R.layout.dialog_unit)
public class ActivityDialog extends BaseActivity {

    @ViewById
    RadioButton rdGram;
    @ViewById
    RadioButton rdKG;
    @ViewById
    RadioButton radAmount;
    @ViewById
    EditText edtAmount;

    String id="0";

    double price=0;
    @AfterViews
    public void init(){

        id=getIntent().getStringExtra("id");
    }
    @Click
    public void btnAdd(){
        if(TextUtils.isEmpty(edtAmount.getText().toString())){
            Toast.makeText(ActivityDialog.this,"Please enter any amount",Toast.LENGTH_LONG).show();
        }else{
            performCalculation();
        }

    }

    private void performCalculation(){
        try {
            dataContext.itemsObjectSet.fill("id=?",new String[]{id},null);
            Items items=dataContext.itemsObjectSet.get(0);

        if(rdGram.isChecked()){
            if(!TextUtils.isEmpty(edtAmount.getText().toString()))
                price = ((Integer.parseInt(edtAmount.getText().toString()) * items.ItemPrice)) / 1000;

            deleteSameItem(items);
            tbl_cart cart=new tbl_cart();
            cart.ItemName=items.ItemName;
            cart.ItemPrice=price;
            cart.ItemQty=Integer.parseInt(edtAmount.getText().toString());
            cart.ItemUnit="Gram";
            cart.sellDate=sdf.format(new Date().getTime());
            cart.setStatus(Entity.STATUS_NEW);
            dataContext.cartObjectSet.save(cart);
            setResult(RESULT_OK);
            finish();
        }
        else if(rdKG.isChecked()){
            if(!TextUtils.isEmpty(edtAmount.getText().toString()))
                price=((Integer.parseInt(edtAmount.getText().toString())*items.ItemPrice))/1;

            deleteSameItem(items);
            tbl_cart cart=new tbl_cart();
            cart.ItemName=items.ItemName;
            cart.ItemPrice=price;
            cart.ItemQty=Integer.parseInt(edtAmount.getText().toString());
            cart.ItemUnit="KG";
            cart.sellDate=sdf.format(new Date().getTime());
            cart.setStatus(Entity.STATUS_NEW);
            dataContext.cartObjectSet.save(cart);
            setResult(RESULT_OK);
            finish();
        }
        else if(radAmount.isChecked()){
            if(!TextUtils.isEmpty(edtAmount.getText().toString()))
                price=(((Integer.parseInt(edtAmount.getText().toString())*1)/items.ItemPrice))*1000;

            deleteSameItem(items);
            tbl_cart cart=new tbl_cart();
            cart.ItemName=items.ItemName;
            cart.ItemPrice=Double.parseDouble(edtAmount.getText().toString());
            cart.ItemUnit="Gram";
            cart.ItemQty=(int)price;
            cart.sellDate=sdf.format(new Date().getTime());
            cart.setStatus(Entity.STATUS_NEW);
            dataContext.cartObjectSet.save(cart);
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(ActivityDialog.this,"Please select any one option",Toast.LENGTH_LONG).show();
        }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }
    private void deleteSameItem(Items items){
        try {
            dataContext.cartObjectSet.fill("item_name=?",new String[]{items.ItemName},null);
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        if(dataContext.cartObjectSet.size()>0){
            for(int i=0;i<dataContext.cartObjectSet.size();i++)
                dataContext.cartObjectSet.remove(i).setStatus(Entity.STATUS_DELETED);
            try {
                dataContext.cartObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
        }
    }
}
