package co.jlabs.famb;

import android.annotation.SuppressLint;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.famb.adapter.Spinner_Adapter;
import co.jlabs.famb.database.Contactdb;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Prof extends AppCompatActivity {

    Button sub;
    EditText name11 ,lastName11 ;
    public static TextView age11;
    String phone_no, password, name,age,gender,FirstName;
    Spinner gender11;

    String clientSocket ;
    Socket mSocket; //Network Socket
    Chat_Application app1;
    Contactdb contactdb;
   private static final String URL = "http://52.221.229.53:8000/" ;
    ProgressDialog pDialog;
    String[] gender_string  = { "M", "F"};
    ArrayAdapter<String> gender_array;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        sub = (Button) findViewById(R.id.submit_user);
        name11 = (EditText) findViewById(R.id.name);
        age11 = (TextView) findViewById(R.id.age);
        //gender11 = (EditText) findViewById(R.id.gender);
        gender11 = (Spinner) findViewById(R.id.gender);
        //gender11.setFocusable(true);
        //gender11.setFocusableInTouchMode(true);
        phone_no = getIntent().getStringExtra("contactNo");
//        password = getIntent().getStringExtra("password55");
//        username  = getIntent().getStringExtra("username");
       // lastName11 = (EditText) findViewById(R.id.lastName);

        contactdb = new Contactdb(this);
//        app1 = (Chat_Application) getApplication();
//        gender_string = getResources ().getStringArray (R.array.gender);
//        gender_array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender_string);
//        gender_array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        gender11.setAdapter(gender_array);
        Spinner_Adapter customAdapter=new Spinner_Adapter(getApplicationContext(),gender_string);

        gender11.setAdapter(customAdapter);
        gender11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // changes here

               gender =  gender_string[arg2].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        if (NetworkCheck() == false) {
            Toast.makeText(this, "Could not connect with server", Toast.LENGTH_LONG).show();
        } else {


             mSocket = app1.getSocket();

           //  mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            //  mSocket.on("clientSocket",onConnectResponse);



        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // register_user(); // from socket
                FirstName = name11.getText().toString().trim();
               // lastName = lastName11.getText().toString().trim();
                String editDate = age11.getText().toString().trim();
                age = convertInDate(editDate);
                Log.e("EditDate", "date"+ age);
                //gender = gender11.getText().toString().trim();
                if (TextUtils.isEmpty(FirstName)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Full Name ", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (TextUtils.isEmpty(lastName)) {
//                    Toast.makeText(getApplicationContext(), "Please Fill Last Name ", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(getApplicationContext(), "Please Fill DOB ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Gender ", Toast.LENGTH_SHORT).show();
                    return;
                }

                registeration(); // from http
            }
        });

    }

        age11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectDate();

            }
        });

    }




    public void registeration(){

        pDialog = new ProgressDialog(Prof.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        String tag_json_obj = "json_obj_req";

        String url_register =  URL +"register" ;

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", FirstName);
//            obj.put("lastName", lastName);
            obj.put("phone",phone_no);
            obj.put("dob",age);
            obj.put("gender",gender);
            obj.put("picture","");
            obj.put("platform","android");

            Log.e("obj","obj"+obj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_register,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);

                        try {
                         boolean   Success = response.getBoolean("success");

                        if (Success){

                            Log.i("TAG", "registration is successfull");
                            login_user();

                        }else {
                            Log.i("TAG", "Already Exist ");
                            login_user();
                        }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyLog.e("TAG", "Error: ", error.getMessage());
                        pDialog.dismiss();

                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }




    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    Toast.makeText(getApplicationContext(),"Something wrong from server", Toast.LENGTH_LONG).show();


                }
            });

        }
    };


    private Emitter.Listener onConnectResponse = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("RES","client"+ args[0].toString());

                    clientSocket = args[0].toString();
                    Static_Catelog.setStringProperty(getApplicationContext(),"clientSocket", clientSocket);

                    Log.i("TAGG", "Client Socket"+ clientSocket);

                    Toast.makeText(getApplicationContext(),"response", Toast.LENGTH_LONG).show();

                     // from socket

                }
            });
        }
    };

    private void register_user(){

        JSONObject user = new JSONObject();
        name = name11.getText().toString().trim();
        age = age11.getText().toString().trim();
       // gender = gender11.getText().toString().trim();

        try {
            user.put("username", FirstName);
            user.put("name", name);
            user.put("age", age);
            user.put("gender", gender);
            user.put("number", phone_no);
            user.put("picture", "");
            user.put("platform", "android");

            Log.i("test","chk"+ user +"," + clientSocket);
            mSocket.emit("registerUser", user,clientSocket);
            mSocket.on("registeredUser", registerResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Emitter.Listener registerResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Register", "res" + args[0].toString());
                    boolean success = (boolean) args[0];
                    Log.i("TAG", "success" + success);

                    if (success) {

                        //login_user();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "User Already Exist Please Wait", Toast.LENGTH_SHORT).show();
                        login_user();
                    }
                }
            });

        }
    };


    private void login_user(){

        mSocket.emit("connectUser", phone_no);
        mSocket.on("userList",loginResponse);
    }

    private Emitter.Listener loginResponse = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("login", "res" + args[0].toString());

                    pDialog.dismiss();

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());

                       boolean success = obj.getBoolean("success");

                        JSONObject obj1 = obj.getJSONObject("data");
//                        String _id =  obj1.getString("_id");
//                        String firstname = obj1.getString("firstName");
//                        String lastname = obj1.getString("lastName");
//                        String phone_no = obj1.getString("phone");
//                        String dob = obj1.getString("dob");
//                        String joinDate =  obj1.getString("timestamp");

                        if (success) {

                            contactdb.delete_self_user_detail();
                            contactdb.self_user_detail(obj1);

                           Intent i = new Intent(Prof.this, MakeBond.class);
                            i.putExtra("Flag", "prof.java");
                            app1.setSocket(mSocket);
                            startActivity(i);
                           // ((Splash)getApplicationContext()).finish();
                            finish();
                        } else {

                            Toast.makeText(Prof.this, "Something wrong ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };


//    private void Authenticate() {
//        if (NetworkCheck() == false) {
//            Toast.makeText(this, "Could not connect with server", Toast.LENGTH_LONG).show();
//        }
//
//        if (connection != null && !connection.isAuthenticated()) {
//            Toast.makeText(this, "User already exist", Toast.LENGTH_LONG).show();
//
//
//        }
//
//        if (connection != null && connection.isAuthenticated()) {
//
//            Intent activity_chat_Intent = new Intent(Prof.this, AddPpl.class);
////            activity_chat_Intent.putExtra("username", use);
//            ConnectionHolder.setConnection(connection);
//            startActivity(activity_chat_Intent);
//        }
//
//    }

    private boolean NetworkCheck() {
        boolean check = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            check = true;
        }
        return check;
    }

    public void selectDate() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");

    }


    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            final Calendar calendar = Calendar.getInstance();

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
    }

    public  static void populateSetDate(int year, int month, int day) {

        age11.setText(month+"-"+day+"-"+year);
    }

    public String convertInDate(String editTextDate){

        String newDateString = null;
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date startDate;


        try {
            startDate = df.parse(editTextDate);
             newDateString = df.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


      return newDateString;
    }
}
