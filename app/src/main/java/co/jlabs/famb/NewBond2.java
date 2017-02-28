package co.jlabs.famb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.*;

import co.jlabs.famb.Rounded.CircularImageView;

import co.jlabs.famb.activityArea.ChatBox;
import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.database.Maindb;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class NewBond2 extends AppCompatActivity implements View.OnClickListener {
    
   // private ArrayList<String> ar;
    private ImageView back;
    private TextView up,ppl_num;
    private RelativeLayout action_bar;
    private CircularImageView bond_img;
    private RelativeLayout upper;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RelativeLayout activity_add_ppl;
    private EditText bondname;
    private CoordinatorLayout coordinatorLayout;
    Context context;

    ArrayList<String> ar;
    ArrayList<String> ar_userid;

    Chat_Application app1;
    String ph,pass;
    String userId ;
    Socket mSocket;
    String clientSocket, bondnm,user_phone, bondName;
    String grp_id;
    boolean success ;
    Contactdb contactdb;
    Maindb maindb;
    ArrayList<Models> participant_userId;
    ProgressDialog pd;
    private static final String URL = "http://52.221.229.53:8000/" ;
    ArrayList<Self_UserDetail> user_detailList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_new_bond2);
        contactdb = new Contactdb(this);
        maindb = new Maindb(this);
        participant_userId = new ArrayList<>();
        mSocket = app1.getSocket();


        try {
            user_detailList  = contactdb.return_userDetail();
            for (int i =0; i< user_detailList.size() ; i++){

                Self_UserDetail obj = user_detailList.get(i) ;
                user_phone = obj.phone ;
            }

            clientSocket = Static_Catelog.getStringProperty(getApplicationContext(),"clientSocket");
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initView() {

//        ph = Static_Catelog.getStringProperty(getApplicationContext(),"phone_number");
//        pass = Static_Catelog.getStringProperty(getApplicationContext(), "password");
       // userId = Static_Catelog.getStringProperty(getApplicationContext(), "_id");
          userId = contactdb.return_userID();  // STOP FOR TEMP
        ar = getIntent().getStringArrayListExtra("ar");
        ar_userid = contactdb.return_ContactName_userId(ar);
        Log.i("TT", "otp"+ ar.size() );
        ArrayList<Integer> arInt = getIntent().getIntegerArrayListExtra("arInt");


        Log.e(ar.size()+"","dats");


        back = (ImageView) findViewById(R.id.back);
        up = (TextView) findViewById(R.id.up);
        ppl_num = (TextView) findViewById(R.id.ppl_num);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        bondname = (EditText) findViewById(R.id.bond_name);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        bond_img = (CircularImageView) findViewById(R.id.bond_img);
        upper = (RelativeLayout) findViewById(R.id.upper);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        activity_add_ppl = (RelativeLayout) findViewById(R.id.activity_add_ppl);
        fab.setOnClickListener(this);
        ppl_num.setText(ar.size()+"/50");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(ar,arInt));
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<FakeViewHolder> {


        private ArrayList<String> mCustomObjects;
        private ArrayList<Integer> arInts;

        public RecyclerViewAdapter(ArrayList<String> ar,ArrayList<Integer> arInt) {
            mCustomObjects=ar;
            arInts=arInt;


        }
        @Override
        public int getItemCount() {
            return mCustomObjects.size();
        }

        @Override
        public FakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_add_ppl, parent, false));
        }

        @Override
        public void onBindViewHolder(final FakeViewHolder holder, final int position) {
            //holder.imageView.setImageResource(drawables[position % 3]);
            holder.name_ppl.setText(mCustomObjects.get(position));
            holder.imageView.setImageResource(arInts.get(position));

        }


    }
    private static class FakeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_ppl;
        Button add;
        public FakeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_ppl);
            name_ppl = (TextView) itemView.findViewById(R.id.name_ppl);
            add = (Button) itemView.findViewById(R.id.add);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                 bondnm=bondname.getText().toString().trim();
                if(TextUtils.isEmpty(bondnm)){
                    Log.e("hi","ssa");
                    Snackbar.make(coordinatorLayout, "Give your bond a name", Snackbar.LENGTH_LONG).show();

                }else  {

                       create_bond(bondnm);
//                        Intent i = new Intent(NewBond2.this, ChatBox.class);
//                        i.putExtra("group_name", bondnm);
//                        i.putExtra("room_id",grp_id);
//                        startActivity(i);
//                        finish();
                }

                break;
        }
    }
    public void submit(View v){
        String bondnm=bondname.getText().toString().trim();
        if(TextUtils.isEmpty(bondnm)){
            Snackbar.make(v, "Give your bond a name", Snackbar.LENGTH_LONG).show();
        }

    }





    private void create_bond(String bondname){

        pd = new ProgressDialog(NewBond2.this);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait .....");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        send_bond_request(bondname);

//      mSocket.emit("createGroup",bondname ,userId,clientSocket);
//      mSocket.on("createdGroup", bondResponse);
    }

    private  Emitter.Listener bondResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {



                    Log.i("bondName","created"+ args[0].toString());

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());
                       //  grp_id = obj.getInt("data");

                        addParticipant();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

        }
    };

    public void addParticipant(){

        try {
        JSONArray arr = new JSONArray();
        JSONObject obj ;
        ar_userid.add(userId);
        Log.i("YYY "," size " + ar_userid.size()+  "::" + ar_userid.toString());
        for (int i=0; i < ar_userid.size(); i++){
            obj = new JSONObject();

                obj.put("id", ar_userid.get(i));

               arr.put(obj);
        }
            Log.i("TAG", "iop "+ arr.toString() );
            Log.i("TAG", "IO"+ arr.toString() +","+ grp_id+ ","+ clientSocket);
            mSocket.emit("addParticipants",arr,grp_id,clientSocket);
            mSocket.on("addedParticipants", participant_Response);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private  Emitter.Listener participant_Response = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {


                    Log.i("parti","list"+ args[0].toString());

                   String ab  =  args[0].toString();

                    success =  Boolean.parseBoolean(ab);
                    pd.dismiss();
                    if (success){
                        ar_userid.clear();
                        int isAdmin = 1;
                       // maindb.add_Group_Locally(grp_id,bondnm,userId,isAdmin);

                        Intent i = new Intent(NewBond2.this, ChatBox.class);
                        i.putExtra("group_name", bondnm);
                        i.putExtra("room_id",grp_id);
                        app1.setSocket(mSocket);
                        startActivity(i);
                        finish();
                    }
        }
    };


    public void send_bond_request(final String bndName){


        ar_userid.add(userId);
        String tag_json_obj = "json_obj_req";
        bondName = bndName;
        String url_register =  URL +"createGroup" ;
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray(ar_userid);

        try {
            obj.put("name", bndName);
            obj.put("members", arr);
            obj.put("created_by", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_register,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);
                        pd.dismiss();

                        try {
                            boolean   Success = response.getBoolean("success");

                            if (Success){

                                JSONObject obj = response.getJSONObject("data");
                                 grp_id =  obj.getString("_id");
                                int isAdmin = 1;
                                maindb.add_Group_Locally(grp_id,bondName,userId,isAdmin);
                                Intent i = new Intent(NewBond2.this, ChatBox.class);
                                i.putExtra("room_id",grp_id);
                                i.putExtra("group_name",bndName);
                                app1.setSocket(mSocket);
                                startActivity(i);
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
                        pd.dismiss();

                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }
}
