package co.jlabs.famb.boon.apps.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.jlabs.famb.AppController;
import co.jlabs.famb.NewTask;
import co.jlabs.famb.R;
import co.jlabs.famb.boon.apps.logic.presenter.MainPresenter;
import co.jlabs.famb.boon.apps.logic.presenter_view.MainView;
import co.jlabs.famb.boon.custom.view.CalendarView;
import co.jlabs.famb.database.Contactdb;

import static co.jlabs.famb.boon.apps.utils.AnimationUtils.animate;


public final class MainActivity extends AppCompatActivity implements MainView {
    private static final String DATE_TEMPLATE = "dd/MM/yyyy";
    private static final String MONTH_TEMPLATE = "MMMM yyyy";
    String userId;
    Contactdb contactdb ;

    private final MainPresenter presenter = new MainPresenter(this);

    @BindView(R.id.textview)
    TextView textView;



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.father)
    LinearLayout father;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boon_activity_cals);
        contactdb = new Contactdb(this);
        userId = contactdb.return_userID();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getData();

        presenter.addCalendarView();
        presenter.addTextView();
        presenter.animate();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NewTask.class);
                startActivity(intent);
            }
        });

    }



    public void getData(){

        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                "http://52.221.229.53:8000/getStickies",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("QSQSQS",""+response);
                        try {
                            JSONObject respo=new JSONObject(response);
                            if(respo.getBoolean("success")){

                            updateUI(respo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_id", userId);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjRequest);
    }
    @Override
    public void prepareTextView() {
       // textView.setText(String.format("Today is %s", formatDate(DATE_TEMPLATE, new Date(System.currentTimeMillis()))));
    }

    @Override
    public void prepareCalendarView() {
        Calendar disabledCal = Calendar.getInstance();
        disabledCal.set(Calendar.DATE, disabledCal.get(Calendar.DATE) - 1);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY)
                .setOnDateClickListener(this::onDateClick)
                .setOnMonthChangeListener(this::onMonthChange)
                .setCalendarBackgroundColor(Color.WHITE)
                .setOnDateLongClickListener(this::onDateLongClick)
                .setOnMonthTitleClickListener(this::onMonthTitleClick)
                .setDisabledDate(disabledCal.getTime());


        if (calendarView.isMultiSelectDayEnabled()) {
            calendarView.setOnMultipleDaySelectedListener((month, dates) -> {
                //Do something with your current selection
            });
        }

        calendarView.update(Calendar.getInstance(Locale.getDefault()));
    }



    @Override
    public void animateViews() {
        calendarView.shouldAnimateOnEnter(true);
        animate(fab, getApplicationContext());
        animate(father,getApplicationContext());
       // animate(textView, getApplicationContext());
    }

    private void onDateLongClick(@NonNull final Date date) {
       // textView.setText(formatDate(DATE_TEMPLATE, date));
    }

    private void onDateClick(@NonNull final Date date) {
        //textView.setText(formatDate(DATE_TEMPLATE, date));
    }

    private void onMonthTitleClick(@NonNull final Date date) {
        //Do something after month selection
    }

    private void onMonthChange(@NonNull final Date date) {
        final ActionBar actionBar = getSupportActionBar();

        if (null != actionBar) {
            String dateStr = formatDate(MONTH_TEMPLATE, date);
            dateStr = dateStr.substring(0, 1).toUpperCase() + dateStr.substring(1, dateStr.length());

            actionBar.setTitle(dateStr);
        }
    }

    private String formatDate(@NonNull String dateTemplate, @NonNull Date date) {
        return new SimpleDateFormat(dateTemplate, Locale.getDefault()).format(date);
    }


    public void updateUI(JSONObject respo){
        try {
            JSONArray arr=respo.getJSONArray("data");
            for (int i=0;i<arr.length();i++){
                JSONObject jsonObject=arr.getJSONObject(i);
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.adapter_calendar, null);
                TextView event_name=(TextView)v.findViewById(R.id.event_name);
                TextView event_time=(TextView)v.findViewById(R.id.event_time);
                TextView op1=(TextView)v.findViewById(R.id.op1);
                event_name.setText(jsonObject.getString("subject"));
                String month=jsonObject.getString("scheduled_date");
                String dat=jsonObject.getString("scheduled_date");
                month=month.substring(0,2);
                dat=dat.substring(3,5);
                Log.e("month:",""+dat);
                event_time.setText(getMonth(Integer.parseInt(month))+" "+jsonObject.getString("scheduled_time"));
                op1.setText(dat);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 0);
                v.setLayoutParams(params);
                father.addView(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
