package com.app.posapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.account)
public class Account extends BaseActivity {

    @ViewById
    TextView tvIncome;
    @ViewById
    TextView tvExpense;
    @ViewById
    TextView tvTotal;

    double total=0;
    double expenseTotal=0;
    double openingTotal=0;
    @AfterViews
    public void init(){
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
            tvExpense.setText(String.format("%.2f",expenseTotal));
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        try {
            dataContext.salesBillObjectSet.fill();
            if(dataContext.salesBillObjectSet.size()>0) {
                for (int i = 0; i < dataContext.salesBillObjectSet.size(); i++) {
                    if (dataContext.salesBillObjectSet.get(i).ItemUnit.equalsIgnoreCase("gram") || dataContext.salesBillObjectSet.get(i).ItemUnit.equalsIgnoreCase("kg")) {
                        total = total+dataContext.salesBillObjectSet.get(i).ItemPrice;
                    } else
                        total = total+(dataContext.salesBillObjectSet.get(i).ItemQty * dataContext.salesBillObjectSet.get(i).ItemPrice);
                }
            }
            tvIncome.setText(String.format("%.2f",(openingTotal+total)));
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        tvTotal.setText(String.format("%.2f",((openingTotal+total)-expenseTotal)));
    }
}
