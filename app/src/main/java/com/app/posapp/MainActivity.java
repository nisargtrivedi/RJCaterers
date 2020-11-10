package com.app.posapp;

import android.content.Intent;
import android.os.Bundle;

import com.app.posapp.Utility.KeyBoardHandling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById
    EditText edtOne;
    @ViewById
    EditText edtTwo;
    @ViewById
    EditText edtThree;
    @ViewById
    EditText edtFour;
    @ViewById
    Button btnGo;
    @ViewById
    TextView tvName;

    @AfterViews
    public void init(){

        try {
            dataContext.userObjectSet.fill();
            if(dataContext.userObjectSet.size()>0){
                tvName.setText("Welcome , "+dataContext.userObjectSet.get(0).FirstName);

            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        edtOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtOne.getText().toString().length()>0){
                    if(edtOne.getText().toString().length()==1){
                        edtTwo.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtTwo.getText().toString().length()>0){
                    if(edtTwo.getText().toString().length()==1){
                        edtThree.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtThree.getText().toString().length()>0){
                    if(edtThree.getText().toString().length()==1){
                        edtFour.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtFour.getText().toString().length()>0){
                    if(edtFour.getText().toString().length()==1){
                        btnGo.requestFocus();
                        KeyBoardHandling.hideSoftKeyboard(MainActivity.this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    @Click
    public void btnGo(){
        if(TextUtils.isEmpty(edtOne.getText().toString())){
                showAlert("Please enter pin");
        }else if(TextUtils.isEmpty(edtTwo.getText().toString())){
            showAlert("Please enter pin");
        }else if(TextUtils.isEmpty(edtThree.getText().toString())){
            showAlert("Please enter pin");
        }else if(TextUtils.isEmpty(edtFour.getText().toString())){
            showAlert("Please enter pin");
        }else{
            String pin=edtOne.getText().toString()+edtTwo.getText().toString()+edtThree.getText().toString()+edtFour.getText().toString();
            try {
                dataContext.userObjectSet.fill();
                if(dataContext.userObjectSet.size()>0){

                    if(pin.equalsIgnoreCase(dataContext.userObjectSet.get(0).Password)){
                        finish();startActivity(new Intent(MainActivity.this,ActivityItem_.class));
                    }
                }

            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }

        }
    }
}
