package com.app.posapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.app.posapp.adapter.CartAdapter;
import com.app.posapp.adapter.Cash_Item_Adapter;
import com.app.posapp.listeners.onAddCart;
import com.app.posapp.model.Items;
import com.app.posapp.model.tbl_cart;
import com.app.posapp.model.tbl_sales_bill;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@EActivity(R.layout.activity_cart)
public class ActivityCartItem extends BaseActivity implements Runnable {

    @ViewById
    ListView gvItems;
    @ViewById
    TextView tvTotal;

    TextView cart_badge;
    CartAdapter adapter;

    ArrayList<tbl_cart> list=new ArrayList<>();

    double billNo=0;
    @AfterViews
    public void init(){
        try {
            adapter=new CartAdapter(this,list);
            gvItems.setAdapter(adapter);
            adapter.onAddItem(new onAddCart() {
                @Override
                public void onAdd(tbl_cart cart) {

                }

                @Override
                public void onDelete(tbl_cart cart) {
                    try {
                        dataContext.cartObjectSet.fill("id=?",new String[]{cart.getID()+""},null);
                        if(dataContext.cartObjectSet.size()>0){
                            dataContext.cartObjectSet.remove(dataContext.cartObjectSet.get(0));
                            dataContext.cartObjectSet.save();
                            adapter.notifyDataSetChanged();
                            countCart();
                        }
                    } catch (AdaFrameworkException e) {
                        e.printStackTrace();
                    }
                }
            });
            adapter.notifyDataSetChanged();
            bluetoothSetup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataContext.cartObjectSet.fill();
            list.clear();
            list.addAll(dataContext.cartObjectSet);
            adapter.notifyDataSetChanged();
            if(list.size()>0) {
                gvItems.setVisibility(View.VISIBLE);

            }

        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }
    public void countCart(){
        double total=0;
        try {
            dataContext.cartObjectSet.fill();
            for(tbl_cart cart:dataContext.cartObjectSet) {
                if(cart.ItemUnit.equalsIgnoreCase("gram")||cart.ItemUnit.equalsIgnoreCase("kg")){
                    total=total+(cart.ItemPrice);
                }else
                    total=total+(cart.ItemQty*cart.ItemPrice);
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        cart_badge.setText(dataContext.cartObjectSet.size()+"");
        tvTotal.setText("Total Amount Rs. "+String.format("%.2f",total));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        MenuItem item2 = menu.findItem(R.id.filerList);
        item2.setVisible(false);
        countCart();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_cart:
                Toast.makeText(this, "Refresh Cart", Toast.LENGTH_SHORT)
                        .show();

                break;
            // action with ID action_settings was selected
            case R.id.action_done:
                Toast.makeText(this, "Save record.", Toast.LENGTH_SHORT)
                        .show();
                addAllDataIntoSales();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }

        return true;
    }

    private void addAllDataIntoSales(){
        try
        {
           billNo=new Date().getTime();
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dataContext.cartObjectSet.fill();
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        for (int i=0;i<dataContext.cartObjectSet.size();i++){
            tbl_sales_bill obj=new tbl_sales_bill();
            obj.BillNo=billNo;
            obj.ItemName=dataContext.cartObjectSet.get(i).ItemName;
            obj.ItemPrice=dataContext.cartObjectSet.get(i).ItemPrice;
            obj.ItemQty=dataContext.cartObjectSet.get(i).ItemQty;
            obj.ItemUnit=dataContext.cartObjectSet.get(i).ItemUnit;
            obj.sellDate=sdf.format(new Date().getTime());
            obj.setStatus(Entity.STATUS_NEW);

            try {
                dataContext.salesBillObjectSet.save(obj);
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            dataContext.cartObjectSet.remove(i).setStatus(Entity.STATUS_DELETED);
        }
        try {
            dataContext.cartObjectSet.save();
//            finish();
            printData();
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }

    public void printData(){

        try {
            dataContext.salesBillObjectSet.fill("bill_no=?",new String[]{billNo+""},null);
            if(dataContext.salesBillObjectSet.size()>0){
                    data(dataContext.salesBillObjectSet);
            }
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
    }
    private void data(ArrayList<tbl_sales_bill> list){
        DecimalFormat df = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String rounded = df.format(billNo);
              try{
                  double billTotal=0;
                  OutputStream os = mBluetoothSocket
                          .getOutputStream();
                  String BILL = "";
                  BILL = ("R.J. Caterers\n\n"+"Nishit Agnihotri - 9374014385\nAmit Kejriwal - 9913222537\nRavi Sharma - 9825098010\n");
                  BILL += ("Jagadiya Bridge Corner ,\nL.G Hospital Road,\nManinagar,Ahmedabad-380008\n\n");
                  BILL += ("Email :rjcaterers.amd@gmail.com\n\n");
                  BILL = BILL + "--------------------------------";
                  BILL += ("Bill No: "+rounded+"\n");
                  BILL += ("Date: "+sdf2.format(new Date().getTime())+"\n");

                  BILL = BILL + "--------------------------------";
                  BILL = BILL + String.format("%1$-10s %2$-5s %3$-5s %4$-10s%n","Items", "Qty", "Rate", "Amount");
                  BILL = BILL + "--------------------------------";
                  for(int i=0;i<list.size();i++){
                      if(list.get(i).ItemUnit.equalsIgnoreCase("gram") || list.get(i).ItemUnit.equalsIgnoreCase("kg")){
                          billTotal=billTotal+list.get(i).ItemPrice;
                          BILL = BILL + String.format("%1$-12s %2$-8s %3$-5s %4$-10s%n",list.get(i).ItemName,list.get(i).ItemQty+list.get(i).ItemUnit,String.format("%.0f",list.get(i).ItemPrice),String.format("%.0f",list.get(i).ItemPrice))+"\n";
                      }else {
                          BILL = BILL + String.format("%1$-12s %2$-8s %3$-5s %4$-10s%n", list.get(i).ItemName, list.get(i).ItemQty + list.get(i).ItemUnit, String.format("%.0f",list.get(i).ItemPrice), String.format("%.0f",list.get(i).ItemQty * list.get(i).ItemPrice)) + "\n";
                          billTotal=billTotal+(list.get(i).ItemQty * list.get(i).ItemPrice);
                      }
                  }
                  BILL = BILL + "--------------------------------";
                  BILL = BILL + "\n";
                  BILL = BILL + "Total Value:" + "" + billTotal + "\n";
                  BILL = BILL + "\n\n ";
                  BILL = BILL + "--------------------------------";
                  BILL = BILL + "-----THANK YOU FOR VISIT-------";
                  BILL = BILL + "\n\n\n\n";
                  os.write(BILL.getBytes());
                  //This is printer specific code you can comment ==== > Start

                  // Setting height
                  int gs = 29;
                  os.write(intToByteArray(gs));
                  int h = 104;
                  os.write(intToByteArray(h));
                  int n = 162;
                  os.write(intToByteArray(n));

                  // Setting Width
                  int gs_width = 29;
                  os.write(intToByteArray(gs_width));
                  int w = 119;
                  os.write(intToByteArray(w));
                  int n_width = 2;
                  os.write(intToByteArray(n_width));

                //os.close();

              }catch (Exception ex){

              }

    }
    public void bluetoothSetup(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(ActivityCartItem.this, "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);

                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(this,
                        DeviceListActivity_.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }
    }
    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    public void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ActivityCartItem.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };
    public void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         if(requestCode==REQUEST_CONNECT_DEVICE){
            if (resultCode == Activity.RESULT_OK) {
                Bundle mExtra = data.getExtras();
                String mDeviceAddress = mExtra.getString("DeviceAddress");
                Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                mBluetoothDevice = mBluetoothAdapter
                        .getRemoteDevice(mDeviceAddress);
                mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                        "Connecting...", mBluetoothDevice.getName() + " : "
                                + mBluetoothDevice.getAddress(), true, false);
                Thread mBlutoothConnectThread = new Thread(this);
                mBlutoothConnectThread.start();
                // pairToDevice(mBluetoothDevice); This method is replaced by
                // progress dialog with thread
            }
        }
        else if(requestCode==REQUEST_ENABLE_BT){
            if (resultCode == Activity.RESULT_OK) {
                ListPairedDevices();
                Intent connectIntent = new Intent(ActivityCartItem.this,
                        DeviceListActivity_.class);
                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
            } else {
                Toast.makeText(ActivityCartItem.this, "Message", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
