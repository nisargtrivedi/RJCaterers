package com.app.posapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.app.posapp.adapter.Menu_Adapter;
import com.app.posapp.listeners.onMenuClick;
import com.app.posapp.model.Items;
import com.app.posapp.model.tblMenu;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_opening_balance;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

@EActivity(R.layout.admin_menu)
public class ActivityMenu extends BaseActivity {


    @ViewById
    GridView gvItems;
    @ViewById
            TextView tvBottom;
    Menu_Adapter adapter;

    ArrayList<tblMenu> menus=new ArrayList<>();
    double total=0;

    double totalAmount=0;
    double expenseTotal=0;
    double openingTotal=0;

    @AfterViews
    public void init(){

        menus.add(new tblMenu("ITEMS"));
        menus.add(new tblMenu("SALES REGISTER"));
        menus.add(new tblMenu("EXPENSE"));
        menus.add(new tblMenu("OPENING BALANCE"));
        menus.add(new tblMenu("ACCOUNT"));
        menus.add(new tblMenu("CLEAR OPENING BALANCE"));
        //menus.add(new tblMenu("BACKUP"));
        adapter=new Menu_Adapter(this,menus);
        gvItems.setAdapter(adapter);



        adapter.onAddItem(new onMenuClick() {
            @Override
            public void onClick(tblMenu cart) {
                if(cart.MenuName.equalsIgnoreCase("ITEMS")){
                    startActivity(new Intent(ActivityMenu.this,ActivityItem_.class));
                }
                else if(cart.MenuName.equalsIgnoreCase("SALES REGISTER")){
                    startActivity(new Intent(ActivityMenu.this,ActivitySalesRegister_.class));
                }
                else if(cart.MenuName.equalsIgnoreCase("EXPENSE")){
                    startActivity(new Intent(ActivityMenu.this,ExpenseListActivity_.class));
                }else if(cart.MenuName.equalsIgnoreCase("OPENING BALANCE")){
                        showPcsDialog();
                }else if(cart.MenuName.equalsIgnoreCase("ACCOUNT")){
                    startActivity(new Intent(ActivityMenu.this,Account_.class));
                }else if(cart.MenuName.equalsIgnoreCase("BACKUP")){
//                        sendEmail(sdf2.format(new Date().getTime()));
                    sendEmail();

                }else if(cart.MenuName.equalsIgnoreCase("CLEAR OPENING BALANCE")){
                    try {
                        dataContext.tblOpeningBalanceObjectSet.fill();
                        for(int i=0;i<dataContext.tblOpeningBalanceObjectSet.size();i++)
                                dataContext.tblOpeningBalanceObjectSet.remove(i).setStatus(Entity.STATUS_DELETED);
                        dataContext.tblOpeningBalanceObjectSet.save();
                        openingTotal=0;
                        showTotal();
                    } catch (AdaFrameworkException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        showTotal();

    }
    public void showPcsDialog(){
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_pcs, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.edtAmount);
        Button btnAdd = customLayout.findViewById(R.id.btnAdd);
        TextView lbl = customLayout.findViewById(R.id.lbl);
        editText.setHint("Enter opening amount");
        btnAdd.setText("Save");
        lbl.setText("Opening balance");
        AlertDialog dialog = builder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    tbl_opening_balance balance=new tbl_opening_balance();
                    balance.OpeningBalance= Double.parseDouble(editText.getText().toString());
                    dataContext.tblOpeningBalanceObjectSet.save(balance);

                    try {
                        dataContext.tblOpeningBalanceObjectSet.fill();
                        for(tbl_opening_balance balance1:dataContext.tblOpeningBalanceObjectSet){
                            total=total+balance1.OpeningBalance;
                        }
                        tvBottom.setText("Opening balance rs. "+String.format("%.2f",total));
                        showTotal();
                    } catch (AdaFrameworkException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showTotal(){
        total=0;

        totalAmount=0;
        expenseTotal=0;
        openingTotal=0;

        try {
            dataContext.tblOpeningBalanceObjectSet.fill();

            if(dataContext.tblOpeningBalanceObjectSet.size()>0)
                openingTotal=dataContext.tblOpeningBalanceObjectSet.get(0).OpeningBalance;
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        try {
            dataContext.tblExpenseObjectSet.fill();
            if(dataContext.tblExpenseObjectSet.size()>0) {
                for (int j = 0; j < dataContext.tblExpenseObjectSet.size(); j++) {
                    expenseTotal = expenseTotal + dataContext.tblExpenseObjectSet.get(j).ExpenseAmount;

                }
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        try {
            dataContext.salesBillObjectSet.fill();
            if(dataContext.salesBillObjectSet.size()>0) {
                for (int i = 0; i < dataContext.salesBillObjectSet.size(); i++) {
                    if (dataContext.salesBillObjectSet.get(i).ItemUnit.equalsIgnoreCase("gram") || dataContext.salesBillObjectSet.get(i).ItemUnit.equalsIgnoreCase("kg")) {
                        totalAmount = totalAmount+dataContext.salesBillObjectSet.get(i).ItemPrice;
                    } else
                        totalAmount = totalAmount+(dataContext.salesBillObjectSet.get(i).ItemQty * dataContext.salesBillObjectSet.get(i).ItemPrice);
                }
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        tvBottom.setText(String.format("%.2f",((openingTotal+totalAmount)-expenseTotal)));
    }
}
