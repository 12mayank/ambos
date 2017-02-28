package co.jlabs.famb;

import android.*;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

import co.jlabs.famb.activityArea.*;
import co.jlabs.famb.adapter.Chatlist_adapter;
import co.jlabs.famb.boon.apps.view.MainActivity;
import co.jlabs.famb.chatBox.GalleryFrag;
import co.jlabs.famb.chatBox.NonSwipeableViewPager;
import co.jlabs.famb.chatBox.TextFrag;
import co.jlabs.famb.chatBox.VoiceFrag;
import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.database.Maindb;
import co.jlabs.famb.frag.AnimationSet;
import co.jlabs.famb.frag.ChatList_Frag;
import co.jlabs.famb.frag.New_Feed;
import co.jlabs.famb.frag.TabFragment;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MakeBond extends AppCompatActivity implements ViewPager.OnPageChangeListener, Conn {
    private RecyclerView recyclerView;
    private ImageView closeDrawer;
    private static final String TAG = "MainActivity";
    Socket mSocket;
    Chat_Application app1;
    Contactdb contactdb;
    Maindb maindb;
    ArrayList<String> send_num;
    String clientSocket;
    String userId;
    public static final String URL = "http://52.221.229.53:8000/";
    String flag;
    ProgressDialog pDialog;
    ImageView img;
    ImageView image_button;
    TextView text_view12;

    String[] permissions= new String[]{
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS};

    public static final int MULTIPLE_PERMISSIONS = 10;

    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    reload_data reload_data;
    Poll_reload poll_reload;

    private int[] tabIcons = {
            R.drawable.fambond_white,
            R.drawable.feeds,
            R.drawable.contacts
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_bond);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        image_button  = (ImageView) findViewById(R.id.contactRefresh);
        text_view12 = (TextView) findViewById(R.id.yyyy);
        image_button.setVisibility(View.GONE);
        text_view12.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        mSocket = app1.getSocket();
        maindb = new Maindb(this);
        contactdb = new Contactdb(this);
        userId = contactdb.return_userID();
       // userId = Static_Catelog.getStringProperty(getApplicationContext(), "_id");
        send_num = contactdb.getAllLocalContact();
        clientSocket = Static_Catelog.getStringProperty(getApplicationContext(), "clientSocket");
        mSocket = app1.getSocket();
        flag = getIntent().getStringExtra("Flag");
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab1);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //Log.i("TAG", "conn" + mSocket.connected());


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) drawer.findViewById(R.id.recyclerView);

        closeDrawer = (ImageView) drawer.findViewById(R.id.close_drawer);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(1));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);

        get_ALl_Group();

        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {
                    //  permissions  granted.

                    new Contact().execute();
                }
            }
        });

        if (flag != null){
        if (flag.equals("prof.java")) {

            if (checkPermissions()) {
                //  permissions  granted.

                new Contact().execute();
            }

        }
        }
        //getMessageFromGroup();



    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    private void setupViewPager(NonSwipeableViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ChatList_Frag chatList_frag = new ChatList_Frag();
        reload_data = chatList_frag.return_reload_data_interface();
        adapter.addFragment(chatList_frag, "chat");

        New_Feed new_feed = new New_Feed();
        poll_reload = new_feed.retutn_poll_data_interface();
        adapter.addFragment(new_feed, "Feeds");

        TabFragment tabFragment = new TabFragment();
        adapter.addFragment(tabFragment, "Contacts");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return mFragmentTitleList.get(position);

            return null;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {

            if (position == 0) {
                 reload_data.now_reload_data();

                image_button.setVisibility(View.GONE);
                text_view12.setVisibility(View.GONE);

            }

            if (position == 1){

                poll_reload.reload_pollData();
                image_button.setVisibility(View.GONE);
                text_view12.setVisibility(View.GONE);

            }

            if (position == 2){

                image_button.setVisibility(View.VISIBLE);
                text_view12.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<FakeViewHolder> {

        int[] drawables;
        int[] text;
        int[] notif_count;


        public RecyclerViewAdapter(int index) {

            if (index == 1) {
                drawables = new int[]{
                        R.drawable.settings,
                        R.drawable.calendar,
                        R.drawable.fambond_white,
                        R.drawable.vault,
                        R.drawable.contacts,
                        R.drawable.feeds,
                        R.drawable.polls,
                        R.drawable.events,
                        R.drawable.tasks
                };
                text = new int[]{
                        R.string.settings,
                        R.string.calendar,
                        R.string.bonds,
                        R.string.vault,
                        R.string.contacts,
                        R.string.feed,
                        R.string.polls,
                        R.string.events,
                        R.string.tasks

                };
                notif_count = new int[]{
                        R.string.settings1,
                        R.string.calendar1,
                        R.string.bonds1,
                        R.string.vault1,
                        R.string.contacts1,
                        R.string.feed1,
                        R.string.polls1,
                        R.string.events1,
                        R.string.tasks1
                };
            }

        }

        @Override
        public FakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_dashboard, parent, false));
        }

        @Override
        public void onBindViewHolder(final FakeViewHolder holder, final int position) {
            holder.imageView.setImageResource(drawables[position]);
            holder.text.setText(text[position]);
            holder.notif_count.setText(notif_count[position]);
            holder.grid.setOnClickListener(new View.OnClickListener() {
                Intent intent;

                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 1: /** Start a new Activity MyCards.java */
                            intent = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(intent);
                            break;

                        case 6: /** Start a new Activity MyCards.java */
                             intent = new Intent(v.getContext(), NewPoll.class);
                            v.getContext().startActivity(intent);
                            break;
                        case 8: /** Start a new Activity MyCards.java */
                            intent = new Intent(v.getContext(), NewTask.class);
                            v.getContext().startActivity(intent);
                            break;

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }

    private static class FakeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView text, notif_count;
        RelativeLayout grid;

        public FakeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            text = (TextView) itemView.findViewById(R.id.text);
            grid = (RelativeLayout) itemView.findViewById(R.id.grid);
            notif_count = (TextView) itemView.findViewById(R.id.notif_count);

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (!mTabStacker.onBackPressed()) {
//            super.onBackPressed();
//        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if(mSocket.connected()){

                mSocket.disconnect();
            }
        }

    }


    public void restoreView(Fragment fragment, View view) {
//        mTabStacker.restoreView(fragment, view);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // (Keep this first) Saves the TabStacker instance
        // mTabStacker.saveInstance(outState);

        super.onSaveInstanceState(outState);
    }


    @Override
    public Socket getSocket() {

        mSocket = app1.getSocket();
        Log.i("TAAA", "jj" + mSocket.toString());
        return mSocket;
    }

    public Conn return_connection() {

        return this;
    }

    public void get_ALl_Group(){

        String bond_url = URL+"getBonds";
        String tag_json_obj = "json_obj_req";

        JSONObject obj = new JSONObject();
        try {
            obj.put("_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                bond_url,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "groupList :" + response);

                        try {
                            boolean   Success = response.getBoolean("success");

                            if (Success){

                                maindb.delete_group();
                                maindb.participantsList();
                                JSONArray arr = response.getJSONArray("data");
                                maindb.add_All_Group(arr);

                                joinRoom();


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


                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }

    private void joinRoom(){
        try {

            JSONObject roomInfo = new JSONObject();

            roomInfo.put("_id", userId);

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



    public  void getPhoneContact(){

        if (checkPermissions()){

            new Contact().execute();
        }
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    new Contact().execute();

                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }

    class Contact extends AsyncTask<String, Void, String> {



        ArrayList<Local_Contact> alContacts = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            pDialog = new ProgressDialog(MakeBond.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            ContentResolver cr = getApplicationContext().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if(cursor.moveToFirst()){

                alContacts = new ArrayList<>();
                do
                {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                        while (pCur.moveToNext())
                        {
                            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        String number = contactNumber.split("-");
                            String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String con_number = contactNumber.replaceAll("[^0-9]",""); // also remove +

                            String uuuu = normalizePhoneNumber(contactNumber);

                            Local_Contact mm1 = new Local_Contact(uuuu, contactName);
                            alContacts.add(mm1);
                            Log.i("MAYA","all : " + alContacts);
                            break;


                        }
                        pCur.close();
                    }

                } while (cursor.moveToNext()) ;
            }

            return null;
        }

        protected void onPostExecute(String tyu) {
            Log.i("TAG","all789 : " + alContacts);
            contactdb.delete_Contacts();
            boolean success =  contactdb.Add_Contact(alContacts);
            Log.e("TAG", "contact added" + success);
            if(success){
                getFriend_List();

            }
        }

    }

    public String normalizePhoneNumber(String number) {

        String perfactNumber = null ;
        number = number.replaceAll("[^0-9]","");

        if (number.length() > 10 ){

            String firstPosition = String.valueOf(number.charAt(0))  ;
            String firstAndSecondPosition = String.valueOf(number.charAt(0)) + String.valueOf(number.charAt(1));
            Log.e("Number", "number string" + firstAndSecondPosition);

            if (firstPosition.equals("0")){

                number = number.substring(1);
                perfactNumber = "+91" + number;
            }else if(firstAndSecondPosition.equals("91")) {

                number = number.substring(2);
                Log.w("TAG", "number " + number);
                perfactNumber = "+91" + number;

            }else {

                perfactNumber =  "+91" + number;
            }

        }else {

            perfactNumber = "+91" + number;
        }

        return perfactNumber;
    }


    public void getFriend_List(){

        send_num = contactdb.getAllLocalContact();
        Log.d("check1","clientSocket : " + clientSocket);
        Log.d("789456","client_op : " + send_num);
        JSONArray contacts = new JSONArray(send_num);


        String tag_json_obj = "json_obj_req";

        String url_contact =  URL +"getcontacts" ;

        JSONObject obj = new JSONObject();

        try {
            obj.put("phones", contacts);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_contact,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);
                        pDialog.dismiss();

                        try {
                            boolean   Success = response.getBoolean("success");

                            if (Success){

                                contactdb.delete_fambond_contact();
                                JSONArray arr = response.getJSONArray("data");
                                for (int i = 0; i < arr.length(); i++) {

                                    // JSONObject obj1 = arr.getJSONObject(i);
                                    Log.w("TAMM", "value are ");
                                    contactdb.Add_Fambond_Contact(arr.getJSONObject(i));
                                }

                            }else {


                                Log.i("False ", "response false");
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




//    public void get_ALl_Group() {
//
//        Log.w("TAG", "check user_id" + userId);
//
//        mSocket.emit("getGroups", userId);
//
//        mSocket.on("groupList", groupList);
//    }

//    public Emitter.Listener groupList = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    Log.i("groupList", "my123" + args[0].toString());
//
//                    try {
//                        JSONArray arr11 = new JSONArray(args[0].toString());
//                        maindb.delete_group();
//                        for (int i = 0; i < arr11.length(); i++) {
//
//                            JSONObject obj = arr11.getJSONObject(i);
//
//                            int isAdmin = 0;
//                           // maindb.add_All_Group(obj, userId, isAdmin);
//
//                            reload_data.now_reload_data();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            });
//        }
//    };


    public void getMessageFromGroup(){

        mSocket.on("groupMessage", receivedMessage);
    }

    private  Emitter.Listener receivedMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Log.i("Group Message","received "+ args[0].toString());

            try {
                JSONObject obj = new JSONObject(args[0].toString());

                String message = obj.getString("message");
                JSONObject obj1 = obj.getJSONObject("userInfo");

                String nickname1 = obj1.getString("nickname");
                String number  = obj1.getString("number");


                JSONObject obj2 = obj.getJSONObject("attributes");

                String message_type = obj2.getString("type");
                String message_id  = obj2.getString("id");
                JSONObject grp_info = obj.getJSONObject("groupInfo");

                String group_id = grp_info.getString("_id");

                if(message_type.equals("text")){


                        String mes =  StringEscapeUtils.unescapeJava(message);
                        Message_fromDb  msg_class = new Message_fromDb();
                        msg_class.groupId = group_id;
                        msg_class.timeStamp =return_current_time();
                        //msg_class.senderNumber = number;
                        //msg_class.senderName = nickname1;
                        msg_class.isMine = 0;
                        msg_class.isDelivered =0;
                        msg_class.media_url = null;
                        msg_class.message = mes;
                        msg_class.msg_Type = message_type;
                        msg_class.message_id = message_id;
                        if(msg_class.message_id != null){

                            send_to_push(mes,message_type, nickname1);
                            Log.v("TAGG", "message51 :" + msg_class.id);
//                            maindb.add_msg_toLocal(msg_class);
//                            setListAdapter(co.jlabs.famb.activityArea.ChatBox.this);
                        }


                }else if (message_type.equals("image")) {


                        // new DownloadImage().execute(message, message_id);

                        Message_fromDb  msg_class = new Message_fromDb();
                        msg_class.groupId = group_id;
                        msg_class.timeStamp =return_current_time();
                       // msg_class.senderNumber = number;
                       // msg_class.senderName = nickname1;
                        msg_class.isMine = 0;
                        msg_class.isDelivered =0;
                        msg_class.media_url = null;
                        msg_class.message = "";
                        msg_class.msg_Type = message_type;
                        msg_class.message_id = message_id;
                    //    msg_class.url = message;
                        Log.v("TAGG", "message52 :" + msg_class.id);
//                        maindb.add_msg_toLocal(msg_class);
                         send_to_push(  msg_class.message,message_type, nickname1);

                }else {

                      //  new DownloadVideo1().execute(message,message_id);

                        Message_fromDb  msg_class = new Message_fromDb();
                        msg_class.groupId = group_id;
                        msg_class.timeStamp =return_current_time();
                     //   msg_class.senderNumber = number;
                     //   msg_class.senderName = nickname1;
                        msg_class.isMine = 0;
                        msg_class.isDelivered =0;
                        msg_class.media_url = null;
                        msg_class.message = null;
                        msg_class.msg_Type = message_type;
                        msg_class.message_id = message_id;
                        //msg_class.url = message;
                        Log.v("TAGG", "message5222 :" + msg_class.id);
                        send_to_push(msg_class.message, message_type,nickname1);
//                        maindb.add_msg_toLocal(msg_class);
//                        setListAdapter(co.jlabs.famb.activityArea.ChatBox.this);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


    private String return_current_time(){

        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
        java.util.Calendar calobj = java.util.Calendar.getInstance();

        String time1 = formatDate.format(calobj.getTime());

        return  time1;
    }

    public void send_to_push(String message , String msg_type, String nickName){

          int ii = 1 ;
         int notify1 = ii++ ;

        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;

        if (msg_type.equals("text")){
            mBuilder =   new NotificationCompat.Builder(MakeBond.this)
                          .setSmallIcon(R.drawable.fameapp)
                          .setContentTitle("new Message From" + " " + nickName)
                          .setContentText(message)
                          .setWhen(System.currentTimeMillis())
                          .setStyle(new NotificationCompat.BigTextStyle())
                          .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//                     .setSound(Uri.parse("android.resource://" + "co.jlabs.famb"+ "/" +R.raw.notificationsound));

        }else if (msg_type.equals("image")){

            mBuilder = new NotificationCompat.Builder(MakeBond.this)
                       .setSmallIcon(R.drawable.fameapp)
                        .setContentTitle("New Image From" + " " + nickName)
                       .setContentText("image")
                       .setWhen(System.currentTimeMillis())
                       .setStyle(new NotificationCompat.BigTextStyle())
                       .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        }else {

            mBuilder = new NotificationCompat.Builder(MakeBond.this)
                          .setSmallIcon(R.drawable.fameapp)
                           .setContentTitle("New Video From "+  " " + nickName)
                           .setContentText("video")
                          .setWhen(System.currentTimeMillis())
                          .setStyle(new NotificationCompat.BigTextStyle())
                          .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        }

        notif.notify(notify1, mBuilder.build());
    }

}