package co.jlabs.famb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.jlabs.famb.boon.apps.view.MainActivity;

public class NewTask extends AppCompatActivity implements View.OnClickListener {


    private ImageView back;
    private TextView n_poll;
    private RelativeLayout header;
    private EditText task_name;
    private EditText assign;
    private EditText add_note;
    private TextView enter;
    private RadioGroup rdgrp;
    private TextView bar;
    private TextView dt;
    private TextView date_enter;
    private RelativeLayout date;
    private TextView time_edit;
    private RelativeLayout time;
    private FloatingActionButton fab_add;
    private RelativeLayout activity_new_poll;
    Calendar myCalendar;
    Context context;
    String times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_new_task);
        myCalendar = Calendar.getInstance();
        initView();

    }


    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        n_poll = (TextView) findViewById(R.id.n_poll);
        header = (RelativeLayout) findViewById(R.id.header);
        task_name = (EditText) findViewById(R.id.task_name);
        assign = (EditText) findViewById(R.id.assign);
        add_note = (EditText) findViewById(R.id.add_note);
        enter = (TextView) findViewById(R.id.enter);
        rdgrp = (RadioGroup) findViewById(R.id.rdgrp);
        bar = (TextView) findViewById(R.id.bar);
        dt = (TextView) findViewById(R.id.dt);
        date_enter = (TextView) findViewById(R.id.date_enter);
        date = (RelativeLayout) findViewById(R.id.date);
        time_edit = (TextView) findViewById(R.id.time_edit);
        time = (RelativeLayout) findViewById(R.id.time);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        activity_new_poll = (RelativeLayout) findViewById(R.id.activity_new_poll);
        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        times=new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        DateFormat df = new SimpleDateFormat("hh:mm a");
        String time = df.format(Calendar.getInstance().getTime());
        date_enter.setText(date);
        time_edit.setText(time);
        date_enter.setOnClickListener(this);
        fab_add.setOnClickListener(this);
        time_edit.setOnClickListener(this);




    }

    private void updateLabel() {
        String myFormat1 = "MM-dd-yyyy";
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        date_enter.setText(sdf.format(myCalendar.getTime()));

        times=sdf1.format(myCalendar.getTime());
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                submit();
                break;
            case R.id.date_enter:
                new DatePickerDialog(context, datea, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.time_edit:
                Calendar mcurrentTime = Calendar.getInstance();

                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm = "";
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        if (datetime.get(Calendar.AM_PM) == Calendar.AM){
                            am_pm = "AM";
                        }

                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM){
                            am_pm = "PM";
                        }


                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
                        String minu="";
                        if(datetime.get(Calendar.MINUTE)<10){
                            minu="0"+datetime.get(Calendar.MINUTE);
                        }else{
                            minu=""+datetime.get(Calendar.MINUTE);
                        }
                        time_edit.setText(strHrsToShow+":"+minu+" "+am_pm );
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;

        }
    }

    DatePickerDialog.OnDateSetListener datea = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void submit() {
        // validate
        String name = task_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Task title can not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = add_note.getText().toString().trim();
        if (TextUtils.isEmpty(note)) {
            Toast.makeText(this, "Please Add note", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeedit = time_edit.getText().toString().trim();


        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("subject",name);
            jsonObject.put("note",note);
            jsonObject.put("scheduled_date",times);
            jsonObject.put("scheduled_time",timeedit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("datasent",""+jsonObject.toString());
        Intent intent =new Intent(context, Send_New_Task.class);
//        intent.putExtra("jsonData",jsonObject.toString());
        intent.putExtra("subject",name);
        intent.putExtra("note",note);
        intent.putExtra("scheduled_date",times);
        intent.putExtra("scheduled_time",timeedit);
        startActivity(intent);
        finish();

        // TODO validate success, do something


    }
}


