package com.app.posapp;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.posapp.model.Items;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_item_add)
public class ActivityItemAdd extends BaseActivity {

    @ViewById
    EditText edtName;

    @ViewById
    EditText edtPrice;

    @ViewById
    Spinner spUnit;

    Items obj;

    String id = "0";

    @AfterViews
    public void init() {
        if (getIntent().getExtras() != null) {
            id = getIntent().getStringExtra("item");

            if (!id.equalsIgnoreCase("0")) {
                try {
                    dataContext.itemsObjectSet.fill("id=?", new String[]{id}, null);
                    if (dataContext.itemsObjectSet.size() > 0) {
                        obj = dataContext.itemsObjectSet.get(0);
                        edtName.setText(obj.ItemName);
                        edtPrice.setText((int) obj.ItemPrice + "");
                        if (obj.Unit.equalsIgnoreCase("gram")) spUnit.setSelection(0);
                        else if (obj.Unit.equalsIgnoreCase("kg")) spUnit.setSelection(1);
                        else spUnit.setSelection(2);
                    }

                } catch (AdaFrameworkException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Click
    public void btnSave() {
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            showAlert("Please enter item name");
        } else if (TextUtils.isEmpty(edtPrice.getText().toString())) {
            showAlert("Please enter item price");
        } else {
            if (obj == null) {
                try {
                    dataContext.itemsObjectSet.fill();
                    if (dataContext.itemsObjectSet.size() > 0) {
                        for(int i=0;i<dataContext.itemsObjectSet.size();i++){
                            if(dataContext.itemsObjectSet.get(i).ItemName.equalsIgnoreCase(edtName.getText().toString())){
                                showAlert("Item already exist");
                                return;
                            }
                        }

                    }
                } catch (AdaFrameworkException e) {
                    e.printStackTrace();
                }
                Items items = new Items();
                items.ItemName = edtName.getText().toString().trim();
                items.Unit = spUnit.getSelectedItem().toString();
                items.ItemPrice = Double.parseDouble(edtPrice.getText().toString());
                items.setStatus(Entity.STATUS_NEW);
                try {
                    dataContext.itemsObjectSet.save(items);
                    edtName.setText("");
                    edtPrice.setText("");
                    Toast.makeText(ActivityItemAdd.this, "Item added successfully", Toast.LENGTH_LONG).show();
                } catch (AdaFrameworkException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    obj.ItemName = edtName.getText().toString().trim();
                    obj.Unit = spUnit.getSelectedItem().toString();
                    obj.ItemPrice = Double.parseDouble(edtPrice.getText().toString());
                    obj.setStatus(Entity.STATUS_UPDATED);
                    try {
                        dataContext.itemsObjectSet.save();
                        Toast.makeText(ActivityItemAdd.this, "Item updated successfully", Toast.LENGTH_LONG).show();
                        finish();
                    } catch (AdaFrameworkException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
