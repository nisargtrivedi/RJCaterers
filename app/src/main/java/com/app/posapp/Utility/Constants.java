package com.app.posapp.Utility;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {


    //Firebase Database URL
    public static String FirebaseURL ="https://drivewaychat-9aac4.firebaseio.com/";
    //Payment Keys
//    public static String PUBLISHABLE_KEY="pk_test_WpJ6982AirVKrVznN0DCDwgI00exDnAGGk";


    public static String EDIT_MESSAGE="Please enter ";

    private static DateFormat dateFormatFirst = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateFormatPDF = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat dateFormatSecond = new SimpleDateFormat("dd MMM yyyy");
    private static DateFormat dateFormatThird = new SimpleDateFormat("MM/dd/yyyy");

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    private static DateFormat dateFormat_notification = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static DateFormat dateFormatTwo = new SimpleDateFormat("hh:mm a");
    private static DateFormat dateFormatThree = new SimpleDateFormat("dd MMM yyyy");


    private static DateFormat dd_mm_yyyy = new SimpleDateFormat("dd/MM/yyyy");



    public static String converDate(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormatFirst.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatSecond.format(myDate);
    }
    public static String converDatePDF(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormatPDF.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatSecond.format(myDate);
    }

    public static String converDate_YYYYMMDD(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormatSecond.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatFirst.format(myDate);
    }

    public static String converDateProfile(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormatThird.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatSecond.format(myDate);
    }

    public static String converDateProfileTwo(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormatSecond.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatThird.format(myDate);
    }


    public static String convert(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormat.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatTwo.format(myDate);
    }
    public static String convertNotification(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormat.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatTwo.format(myDate);
    }

    public static String convertNotificationTwo(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormat.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatFirst.format(myDate);
    }

    public static String convertTwo(String currentDate){
        Date myDate = null;
        try {
            myDate = dateFormat.parse(currentDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatThree.format(myDate);
    }


    public static String addMinTime(String time, int amount){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date d = null;
        try {
            d = df.parse(time);
            cal.setTime(d);
            cal.add(Calendar.MINUTE, amount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTime = df.format(cal.getTime());
        return newTime;
    }

    public static String addDays(String time, int amount){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = df.parse(time);
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_MONTH, amount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTime = df.format(cal.getTime());
        return newTime;
    }

    public static String addDaysBooking(String time, int amount){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = df.parse(time);
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_MONTH, amount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTime = df.format(cal.getTime());
        return newTime;
    }

    public static String cuurentTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date d = cal.getTime();
        try {
            d = df.parse(cal.getTime().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newTime = df.format(d);
        return newTime;
    }


    public static int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age>0?age:0;
    }


    public static String getChatFormatDate(long time){
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(date);
    }



    public static Date getWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.DAY_OF_WEEK)== Calendar.MONDAY){
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, 1);
            }
            //calendar.add(Calendar.DATE, 1);
        }else if(calendar.get(Calendar.DAY_OF_WEEK)!= Calendar.MONDAY){
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DATE, 1);
            }
            calendar.add(Calendar.DATE, -1);
        }

        return calendar.getTime();
    }

    public static ArrayList<String> getPreviousYears() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        return years;
    }

    public static String getCurrentMonthName() {
        String month_name="";
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
             month_name= month_date.format(cal.getTime());
        }catch (Exception ex){
            System.out.println("getCurrentMonthName()===>"+ex.toString());
        }
        return month_name;
    }

    public static int currentQuarter(){
        int quarter=0;
        try {
            Calendar cal = Calendar.getInstance(Locale.US);
            int month = cal.get(Calendar.MONTH);
             quarter= (month / 3) + 1;
            System.out.println("Quarter = " + quarter);
        }catch (Exception ex){
            System.out.println("Current Quarter Error"+ex.toString());
        }
        return quarter;
    }

    public static boolean validateJavaDate(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return true;
        }
        /* Date is not 'null' */
        else
        {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try
            {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)
            {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }

}
