package co.jlabs.famb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.database.Maindb;

/**
 * Created by Jlabs-Win on 25/02/2017.
 */

public class Send_New_Task extends AppCompatActivity  implements View.OnClickListener,ShareInf {


    List<Models> mModelList ;
    Maindb maindb;
    Context context;
    RecyclerView.Adapter mAdapter;
    Contactdb contactdb;
    String user_id;

    private ImageView back;
    private TextView up;
    private TextView ppl_num;
    private ImageView search;
    private RelativeLayout action_bar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RelativeLayout activity_add_ppl;
    String subject, note, date,time ;
    private static final String URL = "http://52.221.229.53:8000/" ;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bond);

        context = this;
        maindb = new Maindb(this);
        contactdb = new Contactdb(this);
        //mModelList = maindb.return_BondAndName();
        mModelList = contactdb.user_contactName();;
        user_id = contactdb.return_userID();

        subject = getIntent().getStringExtra("subject");
        note = getIntent().getStringExtra("note");
        date = getIntent().getStringExtra("scheduled_date");
        time = getIntent().getStringExtra("scheduled_time");


        initView();
    }

    public void initView(){

        back = (ImageView) findViewById(R.id.back);
        up = (TextView) findViewById(R.id.up);
        ppl_num = (TextView) findViewById(R.id.ppl_num);
        search = (ImageView) findViewById(R.id.search);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        activity_add_ppl = (RelativeLayout) findViewById(R.id.activity_add_ppl);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i("Activity", "check " + mModelList.size());
        mAdapter=new RecyclerViewAdapter(mModelList,this);
        recyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(this);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab:

                break;
            case R.id.search:

                break;
            case R.id.back:
                onBackPressed();
                break;


        }
    }


    public  void onMethodCallback(String i) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        LinearLayout whatsapp=(LinearLayout)dialog.findViewById(R.id.whatsapp);
        TextView w=(TextView) dialog.findViewById(R.id.w);
        w.setText(i);
        LinearLayout sms=(LinearLayout)dialog.findViewById(R.id.sms);
        whatsapp.setOnClickListener(this);
        sms.setOnClickListener(this);
        dialog.show();
    }


    public  void onMycall(final ArrayList<String> ar, final ArrayList<Integer> arInt) {
        Log.e("meda:"+ar.size(),"");
        if (ar.size()>0){
            fab.setVisibility(View.VISIBLE);
            ppl_num.setText(ar.size()+" of 9 selected");

        }else {
            fab.setVisibility(View.GONE);
            ppl_num.setText("Add People");
        }
        try {
            for(int i=0;i<=ar.size();i++ ){
                Log.e("meda"+ar.size(),""+ ar.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(context, NewBond2.class);
                Log.i("mayank", "arrayList "+ ar.size());
                //  intent.putExtra("ar", ar);
                //intent.putExtra("arInt", arInt);
//                intent.putExtra("ar_userid",ar_userid);
                //app1.setSocket(mSocket);
                //startActivity(intent);

                send_to_server(ar);

            }
        });

    }

    public void send_to_server(ArrayList<String> arr){

       ArrayList<String> member_userID   = contactdb.return_ContactName_userId(arr);


        if (member_userID.size() !=0){

              send_Task(member_userID);
        }else {


        }
    }


    public void send_Task(ArrayList<String> memberList){

        pDialog = new ProgressDialog(Send_New_Task.this);
        pDialog.setMessage("Poll is creating ....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        String tag_json_obj = "json_obj_req";

        String url_poll =  URL +"createStickie" ;


        JSONObject obj = new JSONObject();
        JSONArray members_array = new JSONArray(memberList);

        try {
            obj.put("subject",subject);
            obj.put("note", note);
            obj.put("scheduled_date",date);
            obj.put("scheduled_time",time);
            obj.put("users",members_array);
            obj.put("created_by",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_poll,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "stickie res :" + response);
                        pDialog.dismiss();

                        try {
                            boolean   Success = response.getBoolean("success");

                            if (Success){

                                finish();

                                Toast.makeText(getApplicationContext(),"New Task Created", Toast.LENGTH_SHORT).show();


                            }else {

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

}
