package co.jlabs.famb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.frag.ChatList_Frag;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NewPoll extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView n_poll;
    private RelativeLayout header;
    private EditText poll_subject;
    private EditText add_note;
    private TextView enter;
    private EditText op1;
    private RelativeLayout option1;
    private EditText op2;
    private RelativeLayout option2;
    private TextView bar;
    private Button add_more;
    private LinearLayout activity_new_poll;
    private ImageView Ok;
    EditText op3,op4;
    RelativeLayout option3,option4;
    String bondname;
    String userId;
    int room_id;
    Chat_Application app1;
    Socket mSocket;
    public static final String URL = "http://52.221.229.53:8000/";
    Contactdb contactdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);
        mSocket = app1.getSocket();
        contactdb = new Contactdb(this);
//        bondname = getIntent().getStringExtra("grp_name");
//        user_id = getIntent().getIntExtra("userId",0);
//        room_id = getIntent().getIntExtra("grp_id", 0);
        userId = contactdb.return_userID();

        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);

        n_poll = (TextView) findViewById(R.id.n_poll);
        header = (RelativeLayout) findViewById(R.id.header);
        poll_subject = (EditText) findViewById(R.id.poll_subject);
        add_note = (EditText) findViewById(R.id.add_note);
        enter = (TextView) findViewById(R.id.enter);
        op1 = (EditText) findViewById(R.id.op1);
        option1 = (RelativeLayout) findViewById(R.id.option1);
        op2 = (EditText) findViewById(R.id.op2);
        option2 = (RelativeLayout) findViewById(R.id.option2);
        bar = (TextView) findViewById(R.id.bar);
        add_more = (Button) findViewById(R.id.add_more);
        activity_new_poll = (LinearLayout) findViewById(R.id.activity_new_poll);
        Ok = (ImageView) findViewById(R.id.OK);
        option3 = (RelativeLayout) findViewById(R.id.option3);
        option4 = (RelativeLayout) findViewById(R.id.option4);
        op3 = (EditText) findViewById(R.id.op3);
        op4 = (EditText) findViewById(R.id.op4);
        add_more.setOnClickListener(this);
        back.setOnClickListener(this);
        Ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_more:
                open_option();
                break;
            case R.id.back:
                onBackPressed();
                break;

            case R.id.OK:
                submit();
                break;

        }
    }


    public void open_option(){

        if(option3.getVisibility() == View.VISIBLE){

            option4.setVisibility(View.VISIBLE);
        }else {

            option3.setVisibility(View.VISIBLE);
        }


    }



    private void submit() {
        // validate
        String subject = poll_subject.getText().toString().trim();
        if (TextUtils.isEmpty(subject)) {
            Toast.makeText(this, "Enter poll subject", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = add_note.getText().toString().trim();
//        if (TextUtils.isEmpty(note)) {
//            Toast.makeText(this, "Add note", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String op1String = op1.getText().toString().trim();
        if (TextUtils.isEmpty(op1String)) {
            Toast.makeText(this, "Option 1", Toast.LENGTH_SHORT).show();
            return;
        }

        String op2String = op2.getText().toString().trim();
//        if (TextUtils.isEmpty(op2String)) {
//            Toast.makeText(this, "Option 2", Toast.LENGTH_SHORT).show();
//            return;
//        }
        String op3String =  op3.getText().toString().trim();
        String op4String = op4.getText().toString().trim();

        // TODO validate success, do something

        //send_pollToServer(subject, note, op1String,op2String);

        Intent i = new Intent(NewPoll.this, Add_Bond.class);
        i.putExtra("subject", subject);
        i.putExtra("op1",op1String);
        i.putExtra("op2",op2String);
        i.putExtra("op3",op3String);
        i.putExtra("op4",op4String);
        startActivity(i);
        finish();
    }

    public void send_pollToServer(String subject, String note, String op1, String op2){


        String tag_json_obj = "json_obj_req";

        String url_register =  URL +"createPoll" ;
         JSONArray arr = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("title" , subject);
           // obj.put("note" , note);

            obj.put("member",arr);
            obj.put("created_by",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
