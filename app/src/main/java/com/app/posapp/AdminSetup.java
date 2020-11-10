package com.app.posapp;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.app.posapp.Utility.Constants;
import com.app.posapp.model.User;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_admin_setup)
public class AdminSetup extends BaseActivity {

    @ViewById
    EditText edtFirstName;
    @ViewById
    EditText edtLastName;
    @ViewById
    EditText edtPassword;
    @ViewById
    EditText edtShopName;
    @ViewById
    EditText edtShopAddress;
    @ViewById
    EditText edtMobile;
    @ViewById
    Button btnSave;
    @AfterViews
    public void init(){

    }
    @Click
    public void btnSave(){
        if(TextUtils.isEmpty(edtFirstName.getText().toString().trim())){
            showAlert(getString(R.string.alert_firstname));
        }else if(TextUtils.isEmpty(edtLastName.getText().toString().trim())){
            showAlert(getString(R.string.alert_lastname));
        }else if(TextUtils.isEmpty(edtPassword.getText().toString().trim())){
            showAlert(getString(R.string.alert_password));
        }else if(TextUtils.isEmpty(edtShopName.getText().toString().trim())){
            showAlert(getString(R.string.alert_shop_name));
        }else if(TextUtils.isEmpty(edtShopAddress.getText().toString().trim())){
            showAlert(getString(R.string.alert_shop_address));
        }else if(TextUtils.isEmpty(edtMobile.getText().toString().trim())){
            showAlert(getString(R.string.alert_mobile_number));
        }else if(edtMobile.getText().toString().trim().length()<10){
            showAlert(getString(R.string.alert_phone_number));
        }else{
            User user=new User();
            user.FirstName=edtFirstName.getText().toString().trim();
            user.LastName=edtLastName.getText().toString().trim();
            user.Password=edtPassword.getText().toString().trim();
            user.Address=edtShopAddress.getText().toString().trim();
            user.Mobile=edtMobile.getText().toString().trim();
            user.ShopName=edtShopName.getText().toString().trim();
            user.setStatus(Entity.STATUS_NEW);
            try {
                dataContext.userObjectSet.save(user);
                finish();
                startActivity(new Intent(AdminSetup.this,MainActivity_.class));
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }

        }
    }
}
