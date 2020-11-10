package com.app.posapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.posapp.model.Items;
import com.app.posapp.model.User;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@EActivity(R.layout.splash)
public class Splash extends BaseActivity{



    @AfterViews
    public void init(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }else{
            try {
                dataContext.itemsObjectSet.fill();
                if(dataContext.itemsObjectSet.size()==0){
                    setData();
                }
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            // bluetoothSetup();
        }
    }
    @Click
    public void btnCash(){
        startActivity(new Intent(Splash.this,ActivityCashItem_.class));

    }

    @Click
    public void btnAdmin(){
        startActivity(new Intent(Splash.this,ActivityMenu_.class));
//        startActivity(new Intent(Splash.this,ActivityItem_.class));
    }

    private void load(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    dataContext.userObjectSet.fill();

                    if(dataContext.userObjectSet.size()==0) {
                        finish();
                        startActivity(new Intent(Splash.this,AdminSetup_.class));
                    }
                    else{
                        finish();
                        startActivity(new Intent(Splash.this,MainActivity_.class));
                    }
                } catch (AdaFrameworkException e) {
                    e.printStackTrace();
                }
            }
        },300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1000){
               //bluetoothSetup();
            setData();
        }
    }


    public void setData(){
        try {
            dataContext.itemsObjectSet.save(new Items("JALEBI",400,"KG"));
            dataContext.itemsObjectSet.save(new Items("DAL KACHORI ",12,"Pcs."));
            dataContext.itemsObjectSet.save(new Items("KANDA KACHORI ",15,"Pcs."));
            dataContext.itemsObjectSet.save(new Items("MIRCHI VADA ",15,"Pcs."));
            dataContext.itemsObjectSet.save(new Items("NAMAKPARA ",200,"KG"));
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }
}
