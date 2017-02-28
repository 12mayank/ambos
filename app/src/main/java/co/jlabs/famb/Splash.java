package co.jlabs.famb;

import android.*;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.frag.TabFragment;
import co.jlabs.famb.otp.OtpView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Splash extends AppCompatActivity implements View.OnClickListener {


    private ImageView icon;
    private EditText cont_code;
    private EditText contact;
    private Button signup;
    private TextView terms;
    private TextView text1;
    private TextView text2;
    private RelativeLayout phone_screen;
    private Button submit;
    private TextView sitback;
    private TextView or;
    private OtpView otpview;
    private RelativeLayout otp;
    private TextView at;
    private EditText username;
    private EditText password;
    private EditText retype;
    private Button submit_user;
    private RelativeLayout userscreen;
    private LinearLayout activity_splash;
    public static int splash_time = 3000;
    Context context;
    Animation move;
    String  user_phone;
    Socket mSocket;
    String clientSocket ;
    Chat_Application app1;
    Contactdb contactdb;
    String contactString;
    ArrayList<Self_UserDetail> user_detailList ;
    ProgressDialog pDialog;

    public static final String URL = "http://52.221.229.53:8000/" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context=this;
        contactdb = new Contactdb(this);
       // ph = Static_Catelog.getStringProperty(getApplicationContext(),"phone_no");
       // pass = Static_Catelog.getStringProperty(getApplicationContext(), "password");
       // user = Static_Catelog.getStringProperty(getApplicationContext(), "nickname");
        Log.e("TAG", "step 1");

        try {
            user_detailList  = contactdb.return_userDetail();

            if (user_detailList.size() >0){
            for (int i =0; i< user_detailList.size() ; i++){

                Self_UserDetail obj = user_detailList.get(i) ;
                user_phone = obj.phone ;
                Log.e("TAG", "step 2");
            }
                mSocket = IO.socket("http://52.221.229.53:8000");
                mSocket.connect();
            }



        Log.w("FFFF", "user name "+ user_phone );
        if (user_phone != null  ){

            if (NetworkCheck() == false) {
                Toast.makeText(this, "Could not connect with server", Toast.LENGTH_LONG).show();
            }else {
                Log.e("TAG", "step 3");
              //  Log.i("TAG","connect 1 :" + mSocket.connect().toString());
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);

                mSocket.on("clientSocket",onConnectResponse);
                login();
            }


        }else{
            Log.e("TAG", "step 4");
            initView();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initView() {
        move = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);
        icon = (ImageView) findViewById(R.id.icon);
        cont_code = (EditText) findViewById(R.id.cont_code);
        contact = (EditText) findViewById(R.id.contact);
        signup = (Button) findViewById(R.id.signup);
        terms = (TextView) findViewById(R.id.terms);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        phone_screen = (RelativeLayout) findViewById(R.id.phone_screen);
        submit = (Button) findViewById(R.id.submit);
        sitback = (TextView) findViewById(R.id.sitback);
        or = (TextView) findViewById(R.id.or);
        otpview = (OtpView) findViewById(R.id.otpview);
        otp = (RelativeLayout) findViewById(R.id.otp);
//        at = (TextView) findViewById(R.id.at);
//        username = (EditText) findViewById(R.id.username);
//        password = (EditText) findViewById(R.id.password);
//        retype = (EditText) findViewById(R.id.retype);
//        submit_user = (Button) findViewById(R.id.submit_user);
//        userscreen = (RelativeLayout) findViewById(R.id.userscreen);
        activity_splash = (LinearLayout) findViewById(R.id.activity_splash);
        contact.setMaxEms(10);
        contact.addTextChangedListener(passwordWatcher);

        signup.setOnClickListener(this);
        submit.setOnClickListener(this);
        //submit_user.setOnClickListener(this);
        TranslateAnimation slide = new TranslateAnimation(0, 0, 100,0 );
        slide.setDuration(500);
        slide.setFillAfter(true);
        icon.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                phone_screen.setVisibility(View.VISIBLE);
            }
        }, splash_time);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                submit();
                break;
            case R.id.submit:
                submit1();
                break;
//            case R.id.submit_user:
//                submit2();
//                break;
        }
    }


    private void submit() {
        // validate


         contactString = "+91"+ contact.getText().toString();
        if (TextUtils.isEmpty(contactString)) {
            Toast.makeText(this, "Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

       // Static_Catelog.setStringProperty(getApplicationContext(), "phone_number", contactString);
        phone_screen.setVisibility(View.GONE);
        otp.setVisibility(View.VISIBLE);

        // TODO validate success, do something


    }


    private void submit1() {
        // validate
        Log.e("some",otpview.getOTP());
        String otpM = otpview.getOTP();
        if (TextUtils.getTrimmedLength(otpM)<4) {
            Toast.makeText(this, "OTP invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        otp.setVisibility(View.GONE);
      //  userscreen.setVisibility(View.VISIBLE);

        // TODO validate success, do something

           check_userIsExists();

//        Intent intent =new Intent(this, Prof.class);
//        intent.putExtra("contactNo" ,contactString);
//        app1.setSocket(mSocket);
//        startActivity(intent);
    }


    private void submit2() {


        String usernameString = username.getText().toString().trim();
        if (TextUtils.isEmpty(usernameString)) {
            Toast.makeText(this, "usernameString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "passwordString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        String retypeString = retype.getText().toString().trim();
        if (TextUtils.isEmpty(retypeString)) {
            Toast.makeText(this, "passwordString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


    }
    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 10) {
                InputMethodManager inputManager =
                        (InputMethodManager) context.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    };




    private boolean NetworkCheck() {
        boolean check = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            check = true;
        }
        return check;
    }


    public void   check_userIsExists(){

        pDialog = new ProgressDialog(Splash.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        Log.e("TAG", "checking user is exists" +contactString);
        String tag_json_obj = "json_obj_req";

        String url_register =  URL +"isRegistered" ;

        JSONObject obj = new JSONObject();

        try {
            obj.put("phone", contactString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_register,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);
                        pDialog.dismiss();

                        try {
                        boolean   Success = response.getBoolean("success");

                            if (Success){

                                boolean isRegistered  = response.getBoolean("isRegistered");
                                if (isRegistered){

                                    login_user();
                                }else {

                                    moveforRegister();
                                }



                            }else {
                                Log.i("TAG", "user not  Exist ");

                                moveforRegister();

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


                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }

    public void moveforRegister(){

        Intent intent =new Intent(this, Prof.class);
        intent.putExtra("contactNo" ,contactString);
        app1.setSocket(mSocket);
        startActivity(intent);
    }


    // check from OTP
    private void login_user(){

        mSocket.emit("connectUser",contactString);
        mSocket.on("userList",loginResponse1);
    }

    private Emitter.Listener loginResponse1 = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.i("login","res"+ args[0].toString());

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());

                        boolean success = obj.getBoolean("success");

                        JSONObject obj1 = obj.getJSONObject("data");
                        if(success){

                            contactdb.delete_self_user_detail();
                            contactdb.self_user_detail(obj1);

                            Intent i = new Intent(Splash.this, MakeBond.class);
                            app1.setSocket(mSocket);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(Splash.this, "something wrong ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    private void login(){

        mSocket.emit("connectUser",user_phone);
        mSocket.on("userList",loginResponse);
    }

    private Emitter.Listener loginResponse = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.i("login","res"+ args[0].toString());

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());

                        boolean success = obj.getBoolean("success");

                        JSONObject obj1 = obj.getJSONObject("data");
//                        String _id =  obj1.getString("_id");
//                        String firstname = obj1.getString("firstName");
//                        String lastname = obj1.getString("lastName");
//                        String phone_no = obj1.getString("phone");
//                        String dob = obj1.getString("dob");
                        //String joinDate =  obj1.getString("join_date");

                        if(success){

                           contactdb.delete_self_user_detail();
                           contactdb.self_user_detail(obj1);

                            Intent i = new Intent(Splash.this, MakeBond.class);
                            app1.setSocket(mSocket);
                            startActivity(i);
                            finish();
                        }else {
                            //Toast.makeText(Splash.this, "something wrong ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


//                    Toast.makeText(getApplicationContext(),"Something wrong from server", Toast.LENGTH_LONG).show();

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
                }
            });
        }
    };




}
