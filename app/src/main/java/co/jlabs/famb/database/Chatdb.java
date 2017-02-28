package co.jlabs.famb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.Message_fromDb;
import co.jlabs.famb.Models;
import co.jlabs.famb.R;

/**
 * Created by Jlabs-Win on 07/02/2017.
 */

public class Chatdb extends SQLiteOpenHelper  {

    // Database Version
    private static final int DATABASE_VERSION = 7;
    // Database Name
    private static final String DATABASE_NAME = "Chatdb";
    Context context;

    public Chatdb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LocalED_TABLE = "CREATE TABLE Group_list ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "groupID TEXT, " +
                "group_name TEXT, " +
                "created_by TEXT, " +
                "isAdmin INTEGER);";

        db.execSQL(CREATE_LocalED_TABLE);


        String CREATE_PARTICIPANT_TABLE = "CREATE TABLE Participant_list ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "groupID INTEGER);";
        db.execSQL(CREATE_PARTICIPANT_TABLE);

        String CREATE_CHAT_TABLE = "CREATE TABLE Chat_detail ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "groupID TEXT, " +
                "timestamp TEXT, " +
                "sender_name TEXT, " +
                "sender_id TEXT, " +
                "isMine INTEGER, " +
                "Type TEXT, " +
                "Message TEXT, " +
                "isDelivered INTEGER, " +
                "Media_url TEXT, " +
                "message_id TEXT, " +
                "progress INTEGER, " +
                "sender_phone TEXT);";

        db.execSQL(CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Group_list");
        db.execSQL("DROP TABLE IF EXISTS Participant_list");
        db.execSQL("DROP TABLE IF EXISTS Chat_detail");

        this.onCreate(db);
    }


    private static final String TABLE_GROUP = "Group_list";
    private static final String TABLE_PARTICIPANTS = "Participant_list";
    private static final String TABLE_CHAT_MSG = "Chat_detail";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_GROUP_ID = "groupID";
    private static final String KEY_GROUP_NAME = "group_name";
    private static final String KEY_ISADMIN = "isAdmin";
    private static final String KEY_CREATEDBY = "created_by";

    //for chat Table
    private static final String KEY_TIME = "timestamp";

    private static final String KEY_ISMINE = "isMine";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_MESSAGE = "Message";
    private static final String KEY_MEDIA_URL = "Media_url";
    private static final String KEY_ISDELIVERED = "isDelivered";
    private static final String KEY_MESSAGEID = "message_id";
    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_SENDER_NAME = "sender_name";
    private static final String KEY_SENDER_ID = "sender_id";






    public void  add_All_Group(JSONArray array){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            for (int i =0; i< array.length();i++){

                JSONObject tp = array.getJSONObject(i);

            values.put(KEY_GROUP_ID, tp.getString("_id"));
            values.put(KEY_GROUP_NAME, tp.getString("name"));
            values.put(KEY_CREATEDBY, tp.getString("created_by"));
//            values.put(KEY_USER_ID, user_id);
            //values.put(KEY_ISADMIN, isAdmin);
                db.insert(TABLE_GROUP, null, values);
//            JSONArray arr = tp.getJSONArray("members");
//            add_participantsList(arr, tp.getString("_id"));
            values.clear();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }

    // add group from newBond2

    public void add_group_Locally(String grp_id, String grp_name, String user_id, int isAdmin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_GROUP_ID, grp_id);
        values.put(KEY_GROUP_NAME, grp_name);
        values.put(KEY_CREATEDBY, user_id);
        values.put(KEY_ISADMIN, isAdmin);

        db.insert(TABLE_GROUP, null, values);
        values.clear();
        db.close();
    }

    // add participants list

    public void add_participantsList(JSONArray arr, String grp_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            for (int i =0; i< arr.length(); i++){

                values.put(KEY_GROUP_ID, grp_id);
                values.put(KEY_USER_ID, arr.get(i).toString());
                db.insert(TABLE_PARTICIPANTS, null, values);
                values.clear();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }


    // ADD message to Chatdb

    public void add_msg_ToDb(Message_fromDb msg){

        Log.w("TAG", "values are added in local db");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_GROUP_ID, msg.groupId);
        values.put(KEY_TIME, msg.timeStamp);

        values.put(KEY_ISMINE, msg.isMine);
        values.put(KEY_TYPE, msg.msg_Type);
        values.put(KEY_MESSAGE, msg.message);
        values.put(KEY_ISDELIVERED, msg.isDelivered);
        values.put(KEY_MEDIA_URL, msg.media_url);
        values.put(KEY_MESSAGEID, msg.message_id);
        values.put(KEY_PROGRESS, msg.progress);

        db.insert(TABLE_CHAT_MSG, null, values);
        values.clear();
        db.close();
    }



    // return group-id and groupName

    public ArrayList<Chat_Album> return_groupInfo(){

        ArrayList<Chat_Album> groupInfo = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_GROUP + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Chat_Album tp = null;

        if (cursor.moveToFirst()) {
            do {
                tp = new Chat_Album();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.grp_id= cursor.getString(1);
                tp.group_name=cursor.getString(2);
                tp.user_id=cursor.getString(3);
                tp.isAdmin= cursor.getInt(4);

                groupInfo.add(0,tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return groupInfo;
    }

    // return group name

    public ArrayList<Models> return_groupName(){

        ArrayList<Models> groupName = new ArrayList<>();

        String query = " SELECT "+  KEY_GROUP_NAME  +" FROM " + TABLE_GROUP + " ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Models tp = null;

        if (cursor.moveToFirst()) {
            do {
                tp = new Models();
                tp.text = cursor.getString(0);
                tp.pic = R.drawable.lorem;

                groupName.add(tp);
            } while (cursor.moveToNext());
        }
       return groupName;
    }

    // return groupId on the bases of group Name

    public ArrayList<String> return_grp_ID(ArrayList<String> arr){


        ArrayList<String> grpID_List = new ArrayList<>();
        for (int i=0; i< arr.size(); i++) {
            String query = " SELECT " +  KEY_GROUP_ID + " FROM " + TABLE_GROUP + " where " + KEY_GROUP_NAME+ "  like '" + arr.get(i) + "' ORDER BY id";
            SQLiteDatabase db = this.getWritableDatabase();
            while (db.inTransaction()) {

            }
            db.beginTransaction();
            Cursor cursor = db.rawQuery(query, null);
            db.endTransaction();
            if (cursor.moveToFirst()) {
                do {

                    String user_id = cursor.getString(0);
                    grpID_List.add(user_id);

                } while (cursor.moveToNext());
            }
            db.close();
        }
        Log.w("TAG", "Hii this is userID List "+ grpID_List.toString());
        return grpID_List;
    }


    // return chat using roomID

    public ArrayList<Message_fromDb> return_chat(String grp_id){

        ArrayList<Message_fromDb> all_chat_msg = new ArrayList<>();
        String query = " SELECT  *  FROM " + TABLE_CHAT_MSG + " where " + KEY_GROUP_ID + " like '" +grp_id+"' ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Message_fromDb tp = null;

        if (cursor.moveToFirst()) {
            do {
                tp = new Message_fromDb();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.groupId= cursor.getString(1);
                tp.timeStamp= cursor.getString(2);
                tp.sender_Name= cursor.getString(3);
                tp.sender_id= cursor.getString(4);
                tp.isMine = cursor.getInt(5);
                tp.msg_Type = cursor.getString(6);
                tp.message = cursor.getString(7);
                tp.isDelivered = cursor.getInt(8);
                tp.media_url = cursor.getString(9);
                tp.message_id = cursor.getString(10);
                tp.progress = cursor.getInt(11);

                all_chat_msg.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return all_chat_msg;
    }


    public ArrayList<Message_fromDb> return_Last_Message(String grp_id){

        ArrayList<Message_fromDb> last_msg = new ArrayList<>();
        String query = " SELECT * FROM " + TABLE_CHAT_MSG + " where " + KEY_GROUP_ID + " like '" +grp_id + "' AND " + KEY_ID + " = " + " ( SELECT  MAX (id) " + " FROM " + TABLE_CHAT_MSG + " ) " ;

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Message_fromDb tp = null;

        if (cursor.moveToFirst()) {
            do {
                tp = new Message_fromDb();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.groupId= cursor.getString(1);
                tp.timeStamp= cursor.getString(2);
                tp.sender_Name= cursor.getString(3);
                tp.sender_id= cursor.getString(4);
                tp.isMine = cursor.getInt(5);
                tp.msg_Type = cursor.getString(6);
                tp.message = cursor.getString(7);
                tp.isDelivered = cursor.getInt(8);
                tp.media_url = cursor.getString(9);
                tp.message_id = cursor.getString(10);
                tp.progress = cursor.getInt(11);

                last_msg.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return last_msg;

    }


    public Boolean update_progress(int progress , String message_id, String grp_id){

        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROGRESS, progress);
        db.update(TABLE_CHAT_MSG, values, KEY_MESSAGEID +" like '"+message_id+"' AND "+ KEY_GROUP_ID + " like '"+ grp_id+"'", null);
        success = true;

        return success;
    }

    public void update_media_url(String filepath, String message_id, String grp_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("TAG", "check media URL" + filepath);
        ContentValues values = new ContentValues();
        values.put(KEY_MEDIA_URL, filepath);
        db.update(TABLE_CHAT_MSG, values, KEY_MESSAGEID+" like '"+message_id+"' AND "+ KEY_GROUP_ID + " like '"+ grp_id+"'", null);
    }


    public void delete_All_Group(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP, null, null);
        db.close();
    }

    public void delete_ParticipantsList(){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        db.delete(TABLE_PARTICIPANTS,null,null);
        db.close();
    }

}
