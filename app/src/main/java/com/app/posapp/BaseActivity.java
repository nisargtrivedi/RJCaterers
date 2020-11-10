package com.app.posapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.posapp.Utility.DataContext;
import com.app.posapp.model.User;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.androidannotations.annotations.EActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class BaseActivity extends AppCompatActivity {

    DataContext dataContext;
    Activity activity;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");


    protected static final String TAG = "TAG";
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    public BluetoothAdapter mBluetoothAdapter;
    public UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public ProgressDialog mBluetoothConnectProgressDialog;
    public BluetoothSocket mBluetoothSocket;
    public BluetoothDevice mBluetoothDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataContext=new DataContext(this);
        activity=this;
    }
    public void showAlert(String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("");
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON1, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        } catch (Exception ex) {

        }
    }
    public  void showAlertFinish(final Activity act, String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(act).create();
            alertDialog.setTitle("");
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON1, "OK",
                    (dialog, which) -> {
                        dialog.dismiss();
                        act.finish();
                    });
            alertDialog.show();
        } catch (Exception ex) {

        }
    }

    public void getPrint(View lv,View v2){
        Bitmap b = getBitmapFromView(lv,v2.getHeight(), v2.getWidth());
        lv.setDrawingCacheEnabled(true);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/RJcaterers/" + File.separator + new Date().getTime()+".jpg");
        String path=f.getAbsolutePath();
        Log.i("PATH-->",path);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
        }
//        bitmap = b;
        String fname=createPdf(b);
    }
    public Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }


    public String createPdf(Bitmap bitmap){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);



        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RJcaterers/"+new Date().getTime()+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            // btn_convert.setText("Check PDF");
            // boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        return  targetPdf;
    }

    public void createExcel(Cursor cursor){
        String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RJcaterers/"+new Date().getTime()+".xls";
        File file = new File(targetPdf);

        try {
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Sales Register", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Bill Number"));
            sheet.addCell(new Label(1, 0, "Date"));
            sheet.addCell(new Label(2, 0, "Amount"));

            if (cursor.moveToFirst()) {
                do {
                    String billno = cursor.getString(cursor.getColumnIndex("bill_no"));
                    String selldate = cursor.getString(cursor.getColumnIndex("sell_date"));
                    String price = cursor.getString(cursor.getColumnIndex("item_price"));

                    int i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, billno));
                    sheet.addCell(new Label(1, i, selldate));
                    sheet.addCell(new Label(2, i, price));
                } while (cursor.moveToNext());
            }

            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    public void sendEmail(String msg){
        new Thread(new Runnable() {
            public void run() {

                try {

                    GMailSender sender = new GMailSender(
                            "nisarg.trivedi786@gmail.com",
                            "Macbook1192");

                    sender.addAttachment(Environment.getExternalStorageDirectory().getAbsolutePath()+"/RJcaterers/rj.db");

                    sender.sendMail("R J Caterers Backup",
                                    msg,
                            "nisarg.trivedi786@gmail.com",

                            "nisarg.trivedi1992@gmail.com");

                } catch (Exception e) {
                    System.out.println("EMAIL ERROR"+e.toString());
                    //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            }

        }).start();
    }
    public void sendEmail() {
        String filename="rj.db";
        File filelocation = new File(String.format("%s/RJcaterers/rj.db", Environment.getExternalStorageDirectory().getAbsolutePath()));
        Uri path = Uri.parse("file://"+filelocation);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test multiple attachments");
        intent.putExtra(Intent.EXTRA_TEXT, "Mail with multiple attachments");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nisarg.trivedi1192@gmail.com"});
        intent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(filelocation));
        //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(intent, "Send mail"));
    }


}
