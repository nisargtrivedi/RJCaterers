package com.app.posapp;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.posapp.listeners.onItemDelete;
import com.app.posapp.model.Items;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_all_item)
public class ActivityItem extends BaseActivity {

    @ViewById
    ListView lvItems;
    @ViewById
    FloatingActionButton fbAdd;
    @ViewById
    TextView lblMessage;

    ItemAdapter adapter;

    ArrayList<Items> list=new ArrayList<>();

    @AfterViews
    public void init(){
        try {
            adapter=new ItemAdapter(this,list);
            lvItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.onDeleteItem(new onItemDelete() {
                @Override
                public void onDelete(Items items) {
                    try {
                        dataContext.itemsObjectSet.fill("id=?",new String[]{items.getID()+""},null);
                    if(dataContext.itemsObjectSet.size()>0){
                        dataContext.itemsObjectSet.remove(dataContext.itemsObjectSet.get(0));
                            dataContext.itemsObjectSet.save();
                            adapter.notifyDataSetChanged();
                    }
                    } catch (AdaFrameworkException e) {
                        e.printStackTrace();
                    }
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
            if(list.size()>0){
                lvItems.setVisibility(View.VISIBLE);
                lblMessage.setVisibility(View.GONE);
            }else{
                lvItems.setVisibility(View.GONE);
                lblMessage.setVisibility(View.VISIBLE);
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }


    }

    @Click
    public void fbAdd(){
        startActivity(new Intent(this,ActivityItemAdd_.class));
    }
}
