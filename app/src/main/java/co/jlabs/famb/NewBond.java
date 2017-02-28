package co.jlabs.famb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.jlabs.famb.Rounded.CircularImageView;
import co.jlabs.famb.checkBox.CircleCheckBox;
import co.jlabs.famb.database.Contactdb;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NewBond extends AppCompatActivity implements ShareInf, View.OnClickListener {

    private ImageView back;
    private TextView up;
    private TextView ppl_num;
    private ImageView search;
    private RelativeLayout action_bar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RelativeLayout activity_add_ppl;
     List<Models> mModelList ;

    RecyclerView.Adapter mAdapter;
    Context context;
    Socket mSocket;
    String clientSocket;
    Contactdb contactdb;
    Chat_Application app1;
     Button add_grp;
    ProgressDialog  pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_new);
        contactdb = new Contactdb(this);

        mSocket = app1.getSocket();

        clientSocket = Static_Catelog.getStringProperty(context,"clientSocket");

        Log.d("TAGGG","fg " + clientSocket );

        mModelList  = contactdb.user_contactName();
//        Intent intent = getIntent();
//        Bundle args = intent.getBundleExtra("BUNDLE");
//        mModelList = (ArrayList<Models>) args.getSerializable("ARRAYLIST");
        Log.i("TAG", "NEWBond " + mModelList.size());
        initView();

    }

    private void initView() {
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
       // add_grp.setOnClickListener(this);

       // getFriend_List();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                break;
            case R.id.search:

                break;
            case R.id.back:

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

    public  void onMycall(final ArrayList<String> ar,final ArrayList<Integer> arInt) {
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
                Intent intent=new Intent(context, NewBond2.class);
                Log.i("mayank", "arrayList "+ ar.size());
                intent.putExtra("ar", ar);
                intent.putExtra("arInt", arInt);
//                intent.putExtra("ar_userid",ar_userid);
                app1.setSocket(mSocket);
                startActivity(intent);

            }
        });

    }

}
