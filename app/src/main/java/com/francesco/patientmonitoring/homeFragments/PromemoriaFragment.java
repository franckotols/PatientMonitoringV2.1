package com.francesco.patientmonitoring.homeFragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.Calendar;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.francesco.patientmonitoring.R;

public class PromemoriaFragment extends Fragment implements View.OnClickListener{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 100;

    Button datePicker;
    Button hourPicker;
    Button addEvent;

    TextView dateValue;
    TextView hourValue;

    long startMillis = 0;

    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private int mHour = -1;
    private int mMinute = -1;
    int currentYear;
    int currentMonth;
    int currentDay;
    int currentHour;
    int currentMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }

        View rootview = inflater.inflate(R.layout.fragment_promemoria, container, false);

        checkPermission();


        datePicker = (Button) rootview.findViewById(R.id.dateEventPicker);
        hourPicker = (Button) rootview.findViewById(R.id.hourEventPicker);
        addEvent = (Button)  rootview.findViewById(R.id.confirmEvent);

        dateValue = (TextView) rootview.findViewById(R.id.dateEventValue);
        hourValue = (TextView) rootview.findViewById(R.id.hourEventValue);

        datePicker.setOnClickListener(this);
        hourPicker.setOnClickListener(this);
        addEvent.setOnClickListener(this);




        return rootview;
    }

    @Override
    public void onClick(View view) {

        if (view == datePicker) {
            // Process to get Current Date
            final Calendar calD = Calendar.getInstance();
            currentYear = calD.get(Calendar.YEAR);
            currentMonth = calD.get(Calendar.MONTH);
            currentDay = calD.get(Calendar.DAY_OF_MONTH);
            // Launch Date Picker Dialog
            DatePickerDialog fromDatePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mDay = dayOfMonth;
                            mMonth = monthOfYear;
                            mYear = year;
                            // Display Selected date in textbox
                            dateValue.setText(formatDateForText(dayOfMonth, monthOfYear, year));


                        }
                    }, currentYear, currentMonth, currentDay);
            fromDatePickerDialog.show();
        }

        if (view == hourPicker) {
            // Process to get Current Time
            final Calendar calH = Calendar.getInstance();
            currentHour = calH.get(Calendar.HOUR_OF_DAY);
            currentMinute = calH.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            mHour = hourOfDay;
                            mMinute = minute;
                            hourValue.setText(formatDateForHour(hourOfDay, minute));
                        }
                    }, currentHour, currentMinute, true);
            timePickerDialog.show();
        }

        if (view == addEvent) {
            if (datesAreChosen(mYear, mMonth, mDay, mHour, mMinute)){
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(mYear, mMonth, mDay, mHour, mMinute);
                startMillis = beginTime.getTimeInMillis();

                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startMillis);
                intent.putExtra("allDay", false);
                intent.putExtra("endTime", startMillis+60*60*1000);
                intent.putExtra("title", "");
                intent.putExtra("exit_on_sent", true);
                startActivity(intent);
            }

            else{
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Attenzione");
                alertBuilder.setMessage("Seleziona giorno ed ora del promemoria");
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }

    }

    private static String formatDateForText (int day, int month, int year){
        String date;
        String sDay;
        String sMonth;
        String sYear;

        sDay = String.format("%02d", day);
        sMonth = String.format("%02d", month+1);
        sYear = Integer.toString(year);

        date = sDay + " - " + sMonth + " - "+ sYear ;
        return date;

    }

    private static String formatDateForHour (int hour, int minute){
        String hourTime;
        String sHour;
        String sMinute;

        sHour = String.format("%02d", hour);
        sMinute = String.format("%02d", minute);


        hourTime = sHour + " : " + sMinute ;
        return hourTime;

    }

    private static boolean datesAreChosen(int mYear, int mMonth, int mDay, int mHour, int mMinute){
        if(mYear==-1 || mMonth==-1 || mDay==-1 || mHour==-1 || mMinute==-1 ){
            return false;
        }
        return true;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_CALENDAR)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        REQUEST_CODE_ASK_PERMISSIONS);

            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setTitle("Attenzione");
                    alertBuilder.setMessage(this.getString(R.string.perm));
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
                return;
            }

        }
    }

}
