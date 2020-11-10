package com.app.posapp.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.app.posapp.model.Items;
import com.app.posapp.model.User;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_expense;
import com.app.posapp.model.tbl_opening_balance;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import java.io.File;

public class DataContext extends ObjectContext {

    final static String DATABASE_FOLDER  = "%s/RJcaterers/";
    final static String DATABASE_NAME    = "rj.db";
    final static int    DATABASE_VERSION = 1;


    public ObjectSet<User> userObjectSet;
    public ObjectSet<Items> itemsObjectSet;
    public ObjectSet<tbl_cart> cartObjectSet;
    public ObjectSet<tbl_sales_bill> salesBillObjectSet;
    public ObjectSet<tbl_expense> tblExpenseObjectSet;
    public ObjectSet<tbl_opening_balance> tblOpeningBalanceObjectSet;



    public SQLiteDatabase database;
    public DataContext(Context pContext) {
        //super(pContext, DATABASE_NAME, DATABASE_VERSION);
      super(pContext, String.format("%s%s", getDataBaseFolder(), DATABASE_NAME), DATABASE_VERSION);
        initializeContext();
//        database=this.getReadableDatabase();
    }
    public SQLiteDatabase getReadDataBase()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db;
    }

    @Override
    protected void onPopulate(SQLiteDatabase pDatabase, int action) {
        //database=pDatabase;
        try {
            AppLogger.info("On DB Populate:" + action);
        }
        catch (Exception e) {
            ExceptionsHelper.manage(getContext(), e);
        }
    }

    @Override
    protected void onError(Exception pException) {
        ExceptionsHelper.manage(getContext(), pException);
    }

    private void initializeContext() {
        try {
            initializeObjectSets();

            //Enable DataBase Transactions to be used by the Save process.
            this.setUseTransactions(true);

            //Enable the creation of DataBase table indexes.
            this.setUseTableIndexes(true);

            //Enable LazyLoading capabilities.
            //this.useLazyLoading(true);

            //Set a custom encryption algorithm.
            this.setEncryptionAlgorithm("AES");

            //Set a custom encryption master pass phrase.
            this.setMasterEncryptionKey("com.app.posapp");

            //Initialize ObjectSets instances.
//            initializeObjectSets();

        } catch (Exception e) {
            ExceptionsHelper.manage(e);
        }
    }

    private static String getDataBaseFolder() {
        String folderPath = "";
        try {

            folderPath = String.format(DATABASE_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());
            File dbFolder = new File(folderPath);
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
            }
        } catch (Exception e) {
            ExceptionsHelper.manage(e);
        }
        return folderPath;
    }

    private void initializeObjectSets() throws AdaFrameworkException {
        userObjectSet = new ObjectSet<User>(User.class,this);
        itemsObjectSet = new ObjectSet<Items>(Items.class,this);
        cartObjectSet=new ObjectSet<>(tbl_cart.class,this);
        salesBillObjectSet=new ObjectSet<>(tbl_sales_bill.class,this);
        tblExpenseObjectSet=new ObjectSet<>(tbl_expense.class,this);
        tblOpeningBalanceObjectSet=new ObjectSet<>(tbl_opening_balance.class,this);
    }


}

