package co.jlabs.famb.activityArea;

import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;
import com.tonyodev.fetch.request.RequestInfo;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.jlabs.famb.AppController;
import co.jlabs.famb.Chat_Application;


import co.jlabs.famb.GroupInfo_Class;
import co.jlabs.famb.Message_fromDb;
import co.jlabs.famb.MultipartRequest;
import co.jlabs.famb.NewPoll;
import co.jlabs.famb.R;
import co.jlabs.famb.Rounded.CircularImageView;
import co.jlabs.famb.Self_UserDetail;
import co.jlabs.famb.Static_Catelog;
import co.jlabs.famb.chatBox.ChatMessage;
import co.jlabs.famb.chatBox.ChatMessageAdapter;
import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.database.Maindb;
import co.jlabs.famb.fonts.ButtonFont;
import co.jlabs.famb.fonts.TextView_White;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatBox extends Activity implements View.OnClickListener, FetchListener, AdapterView.OnItemClickListener {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView_White back;
    private CircularImageView bond_img;
    private TextView bond_name;
    private TextView members;
    private TextView_White settings,textF,galleryF,cameraF,giphyF,voiceF;
    private TextView_White attachment;
    private TextView_White call;
    private RelativeLayout action_bar;
    private ListView listView;
    private LinearLayout tabs;
    private ImageView imgs;
    private EmojiconEditText et_message;
    private ButtonFont send;
    private View contentRoot,voice_sbtn;
    private ButtonFont voice_btn;
    private LinearLayout contentRootVoice;
    private RelativeLayout send_message_layout;
    private RelativeLayout content_chat_box;
    public ChatMessageAdapter mAdapter;
    private EmojIconActions emojIcon;
    private String userChoosenTask = "my";
    Context context;
    String bondname ;
    String room_id;
    Socket mSocket;
    Chat_Application app1;
    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<ChatMessage> conversation_ArrayList = new ArrayList<ChatMessage>();

    private ArrayList<Message_fromDb> oldData ;
    Maindb maindb;
    Contactdb contactdb ;

    String  firstname, lastname, phone, dob; //email, number, platform, isconnected, socketId ;
    private Uri fileUri;// file url to store image/video
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String user_id ;
    Fetch fetch;
    private long downloadId = -1;
    ArrayList<Message_fromDb> new_msg ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        context=this;
        Log.e("onRequestPermissionsRes","2");

//        app1 = (Chat_Application) getApplication();
        mSocket = app1.getSocket();
        maindb = new Maindb(this);
        contactdb = new Contactdb(this);
        Log.i("JAM", "hey cool" + mSocket.toString());

        bondname = getIntent().getStringExtra("group_name");
        room_id  = getIntent().getStringExtra("room_id");
        Log.i("Deny", "bloody" + room_id);


        ArrayList<Self_UserDetail> user_detailList  = contactdb.return_userDetail();

        for (int i=0; i < user_detailList.size(); i++){

             Self_UserDetail user_detail = user_detailList.get(i);

            user_id = user_detail.userId ;
            firstname = user_detail.firstname;
            lastname = user_detail.lastname;
            phone = user_detail.phone;
            dob = user_detail.dob;
//            number = user_detail.number;
//            platform = user_detail.platform;
//            isconnected = user_detail.isconnected;
        }

        Log.i("TAGG", "user_id" + user_id);
        joinRoom();
        initView();
        getMessageFromGroup();


        try {
            oldData = maindb.return_allMsg(room_id);
            mAdapter = new ChatMessageAdapter(context, oldData);
            listView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener( this);
    }


    protected void onResume(){

        super.onResume();
        mSocket = app1.getSocket();

//        userTypingUpdate();
//        userStopTypingUpdate();

       // setListAdapter(ChatBox.this);
        //mAdapter.notifyDataSetChanged();
        //setListAdapter(ChatBox.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "Some Permissions Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        back = (TextView_White) findViewById(R.id.back);
        bond_img = (CircularImageView) findViewById(R.id.bond_img);
        bond_name = (TextView) findViewById(R.id.bond_name);
        members = (TextView) findViewById(R.id.members);
        settings = (TextView_White) findViewById(R.id.settings);
        textF = (TextView_White) findViewById(R.id.textF);
        galleryF = (TextView_White) findViewById(R.id.galleryF);
        cameraF = (TextView_White) findViewById(R.id.cameraF);
        giphyF = (TextView_White) findViewById(R.id.giphyF);
        voiceF = (TextView_White) findViewById(R.id.voiceF);
        attachment = (TextView_White) findViewById(R.id.attachment);
        call = (TextView_White) findViewById(R.id.call);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        listView = (ListView) findViewById(R.id.listView);
        tabs = (LinearLayout) findViewById(R.id.tabs);
        imgs = (ImageView) findViewById(R.id.imgs);
        et_message = (EmojiconEditText) findViewById(R.id.et_message);
        send = (ButtonFont) findViewById(R.id.send);
        contentRoot = (View) findViewById(R.id.chatBox);
        voice_sbtn = (View) findViewById(R.id.voice_sbtn);
       // voice_btn = (ButtonFont) findViewById(R.id.voice_btn);
        contentRootVoice = (LinearLayout) findViewById(R.id.contentRootVoice);
        send_message_layout = (RelativeLayout) findViewById(R.id.send_message_layout);
        content_chat_box = (RelativeLayout) findViewById(R.id.content_chat_box);
        emojIcon = new EmojIconActions(this,contentRoot,et_message,imgs);
        emojIcon.ShowEmojIcon();
       // mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mAdapter = new ChatMessageAdapter(this, new ArrayList<Message_fromDb>());
        et_message.addTextChangedListener(edited);
        send.setOnClickListener(this);
        textF.setOnClickListener(this);
        galleryF.setOnClickListener(this);
        cameraF.setOnClickListener(this);
        giphyF.setOnClickListener(this);
        voiceF.setOnClickListener(this);
        //textF,galleryF,cameraF,giphyF,voiceF
       // voice_btn.setOnClickListener(this);

        bond_name.setText(bondname);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(ChatBox.this, NewPoll.class);
//                i.putExtra("userId", user_id);
//                i.putExtra("grp_name",bondname);
//                i.putExtra("grp_id",room_id);
//                startActivity(i);
//                app1.setSocket(mSocket);
            }
        });

        bond_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii = new Intent(ChatBox.this, GroupInfo_Class.class);
                ii.putExtra("room_id",room_id);
                ii.putExtra("room_name",bondname);

                startActivity(ii);
            }
        });
    }

// join room ;
    private void joinRoom(){
        try {

        JSONObject roomInfo = new JSONObject();

            roomInfo.put("_id", user_id);

         Log.i("TAG", "roomInfo"+ roomInfo.toString());
        mSocket.emit("joinRoom", roomInfo);
        mSocket.on("joinedRoom", joinedRoomInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  Emitter.Listener joinedRoomInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                    Log.i("joined","roomjoined"+ args[0].toString());
        }
    };

    @Override
    public void onClick(View v) {
        Log.e("onRequestPermissionsRes","3");

        boolean result= Utility.checkPermission(context);
        switch (v.getId()) {
            case R.id.send:
                submit();
                break;
            case R.id.textF:
                voice_sbtn.setVisibility(View.GONE);
                contentRoot.setVisibility(View.VISIBLE);

                break;
            case R.id.galleryF:
              //  selectImage();
                userChoosenTask ="Choose from Library";
                if(result)
                    galleryIntent();
                break;
            case R.id.cameraF:
                userChoosenTask ="Take Photo";
                if(result)
                    cameraIntent();
                break;
            case R.id.giphyF:
                break;
            case R.id.voiceF:
                voice_sbtn.setVisibility(View.VISIBLE);
                contentRoot.setVisibility(View.GONE);

                break;
        }
    }

    private void submit() {
        // validate
        Log.e("onRequestPermissionsRes","4");

        String message = et_message.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Nothing to Send", Toast.LENGTH_SHORT).show();
            return;
        }

       String check  =  message.substring(0);
        Log.i("TAH", "starting char " + check);
        if(check.equals("#")){
            Message_fromDb msg_class = new Message_fromDb();


        }else {

//            String mes =  StringEscapeUtils.unescapeJava(message);
            Message_fromDb msg_class = new Message_fromDb();
            msg_class.groupId = room_id;
            msg_class.timeStamp = return_current_time();
            msg_class.isMine = 1 ;
            msg_class.isDelivered = 0;
            msg_class.media_url = "";
            msg_class.message = message;
            msg_class.msg_Type= "text";
            msg_class.message_id =uniqueId() ;
            msg_class.sender_Name = firstname ;
            msg_class.sender_id = user_id ;

            Log.v("TAGG", "message53 :" + msg_class.id);
            maindb.add_msg_toLocal(msg_class);
            et_message.setText("");
            String type = "text";
            send_to_server(message, type);
            setListAdapter(ChatBox.this);

        }

       // sendMessage(message); // this is direcr
        //msg_class.message_id =  Long.parseLong(ah);
        // TODO validate success, do something
    }



//    private void sendMessage(String message) {
//        ChatMessage chatMessage = new ChatMessage(message, true, false,false);
//        conversation_ArrayList.add(chatMessage);
//        setListAdapter(ChatBox.this);
//    }
    private void sendMessage(Bitmap message) {
        ChatMessage chatMessage = new ChatMessage(message, true, true,false);
        conversation_ArrayList.add(chatMessage);
        setListAdapter(ChatBox.this);
    }

    private void mimicOtherMessage(Bitmap message) {
        Log.i("downloaded ", "images :" + message);
        ChatMessage chatMessage = new ChatMessage(message, false, true,false);
        conversation_ArrayList.add(chatMessage);
        setListAdapter(ChatBox.this);
    }
    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false,false);
        conversation_ArrayList.add(chatMessage);
        setListAdapter(ChatBox.this);
    }

    private void videoMessage(String message){

        Log.i("TAG", "uuu" + message);
        ChatMessage chatMessage = new ChatMessage(message,true, false, true);
        conversation_ArrayList.add(chatMessage);
        setListAdapter(ChatBox.this);
    }

    private void otherVideoMessage(String message){

        ChatMessage chatMessage = new ChatMessage(message,false, false, true);
        conversation_ArrayList.add(chatMessage);
        setListAdapter(ChatBox.this);

    }



    TextWatcher edited=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //sendStartTypingMessage();
            if(count>0){
                send.setText(getString(R.string.send));
            }else  send.setText(getString(R.string.mic));
        }

        @Override
        public void afterTextChanged(Editable s) {
            //sendStopTypingMessage();
        }
    };
    private void galleryIntent()
    {        Log.e("onRequestPermissionsRes","5");
        Intent i = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/* video/*");
//        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RESULT_LOAD_IMAGE);


    }

    private void cameraIntent()
    {        Log.e("onRequestPermissionsRes","6");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE )
               // SELECT_FILE
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte [] b=bytes.toByteArray();

        File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".jpg");


        Log.d("FGHJ", "check file "+ destination.toString());

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         String im = Base64.encodeToString(b, Base64.DEFAULT)+ ".jpg";
        Log.i("chk", "String image "+ im);
        send_image_toServer(destination);
//        sendMessage(thumbnail);

        Message_fromDb msg_class = new Message_fromDb();
        msg_class.groupId = room_id;
        msg_class.msg_Type = "image";
        msg_class.message = "";
        msg_class.media_url = destination.toString();
        msg_class.timeStamp = return_current_time();
        msg_class.isMine = 1;
        msg_class.isDelivered = 0;
        msg_class.sender_Name = firstname;
        msg_class.sender_id = user_id;

        maindb.add_msg_toLocal(msg_class);
        setListAdapter(ChatBox.this);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        String picturePath = null;

        if (data != null) {

            Uri uri = data.getData();
            // temp check
            // bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            String[] projection = { MediaStore.Images.Media.DATA , MediaStore.Images.Media.MIME_TYPE };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            int mimeTypeColumnIndex = cursor.getColumnIndex(projection[1] );

             picturePath = cursor.getString(columnIndex);
            String mimeType = cursor.getString(mimeTypeColumnIndex);
            Log.d("TAFF", "picture Path" + picturePath);
            cursor.close();

            if(mimeType.startsWith("image")) {
                //It's an image
                //bm =   StringToBitMap(picturePath);
               // Log.i("TAH", "bitmap "+ bm.toString());
                File file = new File(picturePath);
                send_image_toServer(file);
                Message_fromDb msg_class = new Message_fromDb();
                msg_class.groupId = room_id;
                msg_class.msg_Type = "image";
                msg_class.message = "";
                msg_class.media_url = picturePath;
                msg_class.timeStamp = return_current_time();
                msg_class.isMine = 1;
                msg_class.isDelivered = 0;
                msg_class.message_id = uniqueId();
                msg_class.sender_Name = firstname;
                msg_class.sender_id = user_id;
                Log.v("TAGG", "message54 :" + msg_class.id);
                maindb.add_msg_toLocal(msg_class);
                setListAdapter(ChatBox.this);

            }
            else if(mimeType.startsWith("video")) {
                //It's a video
                Log.i("YOOO","video "+ picturePath);
               // videoMessage(picturePath); // for set in arrayList
                File file1 = new File(picturePath);
                send_video_toServer(file1);

                Message_fromDb msg_class = new Message_fromDb();
                msg_class.groupId = room_id;
                msg_class.msg_Type = "video";
                msg_class.message = "";
                msg_class.media_url = picturePath;
                msg_class.timeStamp = return_current_time();
                msg_class.isMine = 1;
                msg_class.isDelivered = 0;
                msg_class.message_id = uniqueId();
                msg_class.sender_Name = firstname;
                msg_class.sender_id = user_id;
                Log.v("TAGG", "message54 :" + msg_class.id);
                maindb.add_msg_toLocal(msg_class);
                setListAdapter(ChatBox.this);
            }

        }

    }


    private void setListAdapter(final Context context) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // mAdapter = new ChatMessageAdapter(context, conversation_ArrayList);

                try {
//                    ArrayList<Message_fromDb> new_msg ;
                    new_msg = maindb.one_by_oneMesage(room_id);
//                  int idd =  oldData.get(oldData.size()-1).id;
//                  int new_idd = new_msg.get(new_msg.size() - 1).id;
                    Log.e("TAG", "checka array id ");
                        oldData.addAll(new_msg);
                        mAdapter = new ChatMessageAdapter(context, oldData);
                        listView.setAdapter(mAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

   public void  send_to_server(String message , String type){

       String unique_id = uniqueId();
       String current_time = return_current_time();

       try {
            JSONObject obj = new JSONObject();
            JSONObject obj1 = new JSONObject();
                 obj.put("id", unique_id);
                 obj.put("text", message);
                 obj.put("type", type);

                 obj1.put("name", firstname);
                 obj1.put("phone",phone);
                 obj1.put("_id", user_id);

                 obj.put("sender",obj1);
                 obj.put("bondid",room_id);
                 obj.put("bondname",bondname);
                 obj.put("media_height", "");
                 obj.put("media_width", "");

           Log.i("TAGGG", "message send " + obj);
           mSocket.emit("sendGroupMessage",obj);

       } catch (JSONException e) {
           e.printStackTrace();
       }

   }


    public void getMessageFromGroup(){

        mSocket.on("groupMessage", receivedMessage);
    }

    private  Emitter.Listener receivedMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                    Log.i("Group Message","received "+ args[0].toString());

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());
                        JSONObject obj1 = obj.getJSONObject("message");

                        String message_id = obj1.getString("id");
                        String message  = obj1.getString("text");
                        String message_type = obj1.getString("type");
                        String bond_id =obj1.getString("bondid");
                        String bondname = obj1.getString("bondname");
                        String media_height = obj1.getString("media_height");
                        String media_width = obj1.getString("media_width");

                        String timestamp = obj1.getString("timestamp");

                        JSONObject obj3 =  obj1.getJSONObject("sender");

                        String sender_name = obj3.getString("name");
                        String sender_phn = obj3.getString("phone");
                        String sender_id = obj3.getString("_id");



                        if(message_type.equals("text")){
                            if(user_id.equals(sender_id)){


                            }else {

//                                //mimicOtherMessage(message);
                                String mes =  StringEscapeUtils.unescapeJava(message);
                                Message_fromDb  msg_class = new Message_fromDb();
                                msg_class.groupId = bond_id;
                                msg_class.timeStamp =return_current_time();
                                msg_class.sender_Name = sender_name;
                                msg_class.sender_id = sender_id;
                                msg_class.sender_phone = sender_phn;
                                msg_class.isMine = 0;
                                msg_class.isDelivered =0;
                                msg_class.media_url = null;
                                msg_class.message = mes;
                                msg_class.msg_Type = message_type;
                                msg_class.message_id = message_id;
                                if(msg_class.message_id != null){

                                    Log.v("TAGG", "message51 :" + msg_class.id);
                                    maindb.add_msg_toLocal(msg_class);
                                    setListAdapter(ChatBox.this);
                                }
                            }
//
                        }else if (message_type.equals("image")) {
//
                             if(user_id.equals(sender_id)){

                             }else {

                                // new DownloadImage().execute(message, message_id);
                                 new DownloadImage1().execute(message, message_id);
                                 Message_fromDb  msg_class = new Message_fromDb();
                                 msg_class.groupId = bond_id;
                                 msg_class.timeStamp =return_current_time();
                                 msg_class.sender_Name = sender_name;
                                 msg_class.sender_id = sender_id;
                                 msg_class.sender_phone = sender_phn;
                                 msg_class.isMine = 0;
                                 msg_class.isDelivered =0;
                                 msg_class.media_url = null;
                                 msg_class.message = null;
                                 msg_class.msg_Type = message_type;
                                 msg_class.message_id = message_id;

                                 Log.v("TAGG", "message52 :" + msg_class.id);
                                 maindb.add_msg_toLocal(msg_class);
                                 setListAdapter(ChatBox.this);


                             }
                       }else {
//
                            if(user_id.equals(sender_id)){

                            }else {

                                //new DownloadVideo().execute(message);
                               // download_video_fromServer(message);
                                new DownloadVideo1().execute(message,message_id);

                                Message_fromDb  msg_class = new Message_fromDb();
                                msg_class.groupId = bond_id;
                                msg_class.timeStamp =return_current_time();
                                msg_class.sender_Name = sender_name;
                                msg_class.sender_id = sender_id;
                                msg_class.sender_phone = sender_phn;
                                msg_class.isMine = 0;
                                msg_class.isDelivered =0;
                                msg_class.media_url = null;
                                msg_class.message = null;
                                msg_class.msg_Type = message_type;
                                msg_class.message_id = message_id;

                                Log.v("TAGG", "message5222 :" + msg_class.id);
                                maindb.add_msg_toLocal(msg_class);
                                setListAdapter(ChatBox.this);


                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

         String type = oldData.get(position).msg_Type;
        if (type.equals("image")){
        }

    }

    public String uniqueId(){

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(java.util.Calendar.getInstance().getTime());

        return timeStamp;
    }


    public Bitmap StringToBitMap(String encodedString) {

        Log.i("check "," string" + encodedString);

        Bitmap bitmap = BitmapFactory.decodeFile(encodedString);

        return  bitmap ;
    }


    private  void send_image_toServer(File file){

        Log.i("TAG", "picturePath : "+ file);
        String tag_json_obj = "json_obj_req";

        final String url = "http://jlabs.co/socket/imageupload/upload.php" ;
//       File file = new File(picturePath);
       final String mimeType = "image/jpeg";

        MultipartRequest multipartRequest = new MultipartRequest(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("TAG", " not upload file " + error.toString()); // if file not uploaded then send again to server
            }
        }, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                Log.i("download", " json response :" + response.toString());


                try {
                    String url_download = response.getString("Url");
                    String mimeType1 = "image";
                    send_to_server(url_download, mimeType1);
                    Log.i("down","URL :"+ url_download);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ChatBox.this, "Upload successfully!", Toast.LENGTH_SHORT).show();
            }
        }, file, mimeType);

        AppController.getInstance().addToRequestQueue(multipartRequest,tag_json_obj);

    }


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
//            image.setImageBitmap(result);
            Log.i("TAGH", "download Image"+ result.toString());
            //mimicOtherMessage(result);

        }
    }



    private  void send_video_toServer(File file){

        Log.i("TAG", "picturePath : "+ file);
        String tag_json_obj = "json_obj_req";

        final String url = "http://jlabs.co/socket/imageupload/video.php" ;

        final String mimeType = "application/octet-stream";

        MultipartRequest multipartRequest = new MultipartRequest(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("TAG", " not upload file " + error.toString());
            }
        }, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                Log.i("download", " json response :" + response.toString());

                try {
                    String url_download = response.getString("Url");
                    String mimeType1 = "video";
                   send_to_server(url_download, mimeType1);
                    Log.i("down","URL :"+ url_download);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ChatBox.this, "Upload successfully!", Toast.LENGTH_SHORT).show();
            }
        }, file, mimeType);

        AppController.getInstance().addToRequestQueue(multipartRequest,tag_json_obj);

    }
// download image

       private class DownloadImage1 extends AsyncTask<String, String, String>{

           File  outFile ;
           String fileName = "";
           String root = "";
           String message_id ;

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               Log.d("file in preExecute ", "Lenght of file: " );
           }

           @Override
           protected String doInBackground(String... aurl) {
               int count;

               try {

                  // String root = Environment.getExternalStorageDirectory(Environment.DIRECTORY_DCIM)+ "FamBond video";
                  root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + System.nanoTime() + ".jpg";

                   outFile = new File(root);
                   URL url = new URL(aurl[0]);
                   message_id = aurl[1];

                   Log.i("downloading URL", "URL is " + url.toString() + " message id :" + message_id);
                   URLConnection conexion = url.openConnection();
                   conexion.connect();


                   fileName = url.getFile();

                   int lenghtOfFile = conexion.getContentLength();
                   Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                   // input stream to read file - with 8k buffer
                   InputStream input = new BufferedInputStream(url.openStream(), 8192);

                   //InputStream input = new BufferedInputStream(url.openStream());
                   OutputStream output = new FileOutputStream(outFile ,true);

                   byte data[] = new byte[1024];

                   long total = 0;

                   while ((count = input.read(data)) != -1) {
                       total += count;
                       publishProgress(""+(int)((total*100)/lenghtOfFile));
                       Log.i("length" , "progress of downloading :" + (int)((total*100)/lenghtOfFile));
                       output.write(data, 0, count);
                   }

                   output.flush();
                   output.close();
                   input.close();
               } catch (Exception e) {}
               return null;

           }

           @Override
           protected void onProgressUpdate(String... progress) {
               // setting progress percentage
               //pDialog.setProgress(Integer.parseInt(progress[0]));

             boolean success =  maindb.updateProgressIn_db(Integer.parseInt(progress[0]), message_id, room_id);
               if(success){


                   Log.e("TAG", "checka array id ");
//                   mAdapter.clear();
//                   new_msg = maindb.one_by_oneMesage(room_id);
//                   mAdapter.addAll(new_msg);
                   mAdapter.notifyDataSetChanged();
                   Log.i("TAAAA", " downloading...");
               }


           }

           @Override
           protected void onPostExecute(String unused) {

               Log.i("download ","downloaded video chk ::"+ unused);
               Log.i("download ","downloaded 546 ::"+  outFile.getAbsolutePath()+ File.separator+ fileName);
               Log.i("filepath" , "hey File " + root);
               //otherVideoMessage(outFile.getAbsolutePath()+ File.separator + fileName);

                maindb.Add_download_Image_url(root, message_id, room_id);
////               new_msg.clear();
//               new_msg = maindb.one_by_oneMesage(room_id);
//               mAdapter.addAll(new_msg);
//               mAdapter.notifyDataSetChanged();
                setListAdapter(context);
               //asyncComplete(true);

           }

       }



    private class DownloadVideo1 extends AsyncTask<String, String, String>{

        File  outFile ;
        String fileName = "";
        String root = "";
        String message_id ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("file in preExecute ", "Lenght of file: " );
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                // String root = Environment.getExternalStorageDirectory(Environment.DIRECTORY_DCIM)+ "FamBond video";
                root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + System.nanoTime() + ".mp4";

                outFile = new File(root);
                URL url = new URL(aurl[0]);
                message_id = aurl[1];

                Log.i("downloading URL", "URL is " + url.toString() + " message id :" + message_id);
                URLConnection conexion = url.openConnection();
                conexion.connect();


                fileName = url.getFile();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(outFile ,true);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    Log.i("length" , "progress of downloading :" + (int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));

            boolean success =  maindb.updateProgressIn_db(Integer.parseInt(progress[0]), message_id, room_id);
            if(success){

                Log.e("TAG", "checka array id ");
//                   mAdapter.clear();
//                   new_msg = maindb.one_by_oneMesage(room_id);
//                   mAdapter.addAll(new_msg);
//                   mAdapter.notifyDataSetChanged();
                Log.i("TAAAA", " downloading..." + progress);
            }


        }

        @Override
        protected void onPostExecute(String unused) {

            Log.i("download ","downloaded video chk ::"+ unused);
            Log.i("download ","downloaded 546 ::"+  outFile.getAbsolutePath()+ File.separator+ fileName);
            Log.i("filepath" , "hey File " + root);
            //otherVideoMessage(outFile.getAbsolutePath()+ File.separator + fileName);

            maindb.Add_download_Image_url(root, message_id, room_id);
            setListAdapter(context);

        }

    }

    public void download_video_fromServer(String url){

        fetch = Fetch.getInstance(this);
        clearAllDownloads(url);
    }

    private void clearAllDownloads(String url) {

        List<RequestInfo> infos = fetch.get();

        for (RequestInfo info : infos) {
            fetch.remove(info.getId());
        }

       // createRequest();
        enqueueDownload(url);
    }



    private void enqueueDownload(String url) {

        //String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/" + System.nanoTime() + ".mp4";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+ System.nanoTime() + ".mp4";

        Request request = new Request(url,filePath);
        Log.i("TAG","URL"+ url);
        downloadId = fetch.enqueue(request);

        setTitleView(request.getFilePath());
        setProgressView(Fetch.STATUS_QUEUED,0);
    }

    private void setTitleView(String fileName) {

        Uri uri = Uri.parse(fileName);
        //titleTextView.setText(uri.getLastPathSegment());
        Log.i("TAG"," download path"+ uri.getPathSegments());
//        otherVideoMessage(fileName);
    }

    private void setProgressView(int status,int progress) {

        Log.i("check download","Status"+ status + ":::"+ progress);
        switch (status) {

            case Fetch.STATUS_QUEUED : {
               // progressTextView.setText(R.string.queued);
                Log.i("TAG"," download  progress"+ "queued");
                break;
            }
            case Fetch.STATUS_DOWNLOADING :
            case Fetch.STATUS_DONE : {

                if(progress == -1) {

                   // progressTextView.setText(R.string.downloading);
                    Log.i("TAG"," download  progress"+ "downloading");
//                    Log.i("TAG"," file path"+ filepath);
                }else {

                   // String progressString = getResources() .getString(R.string.percent_progress,progress);




                    Log.i("TAG","progress in %"+ progress);
                    //progressTextView.setText(progressString);

                }
                break;
            }
            default: {
               // progressTextView.setText(R.string.status_unknown);
                Log.i("TAG","Download Failed"+ "status_unknown");
                break;
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(downloadId != -1) {
//
//            RequestInfo info = fetch.get(downloadId);
//
//            if (info != null) {
//                setProgressView(info.getStatus(),info.getProgress());
//            }
//
//            fetch.addFetchListener(ChatBox.this);
//        }
//    }

    @Override
    public void onUpdate(long id, int status, int progress, int error) {

        if(id == downloadId) {

            if(status == Fetch.STATUS_ERROR) {

               // showDownloadErrorSnackBar(error);

                Log.i("TAG", "error "+ error);
            }else {

                setProgressView(status,progress);
            }
        }
    }

//    private void showDownloadErrorSnackBar(int error) {
//
//        final Snackbar snackbar = Snackbar.make(rootView,"Download Failed: ErrorCode: "+ error,Snackbar.LENGTH_INDEFINITE);
//
//
//        snackbar.setAction(R.string.retry, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                fetch.retry(downloadId);
//                snackbar.dismiss();
//            }
//        });
//
//        snackbar.show();
//    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        fetch.release();
    }

    private String return_current_time(){

        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
        Calendar calobj = Calendar.getInstance();

        String time1 = formatDate.format(calobj.getTime());

        return  time1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         this.finish();
    }


//    public void sendStartTypingMessage(){
//
//        mSocket.emit("startType", nickname);
//    }
//
//    public void sendStopTypingMessage(){
//
//        mSocket.emit("stopType", nickname);
//    }

    public void userTypingUpdate(){

        mSocket.on("userTypingUpdate", typingUpdate);
    }

    public Emitter.Listener typingUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {


                    Log.d("Typing Update", "my123" + args[0].toString());
        }
    };

    public void  userStopTypingUpdate(){

        mSocket.on("userStopTypingUpdate", stopTyingUpdate);
    }
    public Emitter.Listener stopTyingUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                    Log.d("Typing Stop Update", "my123" + args[0].toString());
        }
    };
}


