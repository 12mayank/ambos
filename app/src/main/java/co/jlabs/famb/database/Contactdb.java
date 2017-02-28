package co.jlabs.famb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.Fambond_Contact_Var;
import co.jlabs.famb.Local_Contact;
import co.jlabs.famb.Models;
import co.jlabs.famb.R;
import co.jlabs.famb.Self_UserDetail;

/**
 * Created by Jlabs-Win on 06/02/2017.
 */

public class Contactdb extends SQLiteOpenHelper  {


    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "Contactdb";
    Context context;

    public Contactdb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LocalED_TABLE = "CREATE TABLE Contact ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "number TEXT, " +
                "name TEXT);";

        db.execSQL(CREATE_LocalED_TABLE);

        String CREATE_CONTACT_TABLE = "CREATE TABLE FambondContact ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "firstname TEXT, " +
                 "lastname TEXT, " +
                  "phone  TEXT, " +
                  "dob TEXT );" ;

        db.execSQL(CREATE_CONTACT_TABLE);

        String CREATE_SELFUSER_TABLE = "CREATE TABLE Self_user_detail ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "phone  TEXT, " +
                "dob TEXT );" ;

        db.execSQL(CREATE_SELFUSER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contact");
        db.execSQL("DROP TABLE IF EXISTS FambondContact");
        db.execSQL("DROP TABLE IF EXISTS Self_user_detail");

        this.onCreate(db);
    }

    private static final String TABLE_Contact = "Contact";
    private static final String TABLE_FAME_Contact = "FambondContact";
    private static final String TABLE_SELFUSER_DETAIL = "Self_user_detail";



    private static final String KEY_ID = "id";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_NAME = "name";

    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER= "gender";
    private static final String KEY_EMAIL= "email";
    private static final String KEY_PLATFORM= "platform";
    private static final String KEY_ISCONNECTED= "isconnected";

    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DOB = "dob";




    public Boolean  Add_Contact(ArrayList<Local_Contact>  all_contact){

          boolean success = false;

        Log.w("TAGG", "all contact :" + all_contact.toString());

        Local_Contact tp = new Local_Contact();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
           for (int i=0; i< all_contact.size(); i++){


               values.put(KEY_NUMBER, all_contact.get(i).getNumber());
               values.put(KEY_NAME, all_contact.get(i).getName());
               db.insert(TABLE_Contact, null, values);
               values.clear();
               success = true;
           }
        db.close();

        return success;
    }


    public ArrayList<String> getAllLocalContact(){

        ArrayList<String> LocalContact = new ArrayList<>();

        String query = " SELECT " + KEY_NUMBER  + " FROM " + TABLE_Contact + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();

        if (cursor.moveToFirst()) {
            do {

                String num  = cursor.getString(0);
                LocalContact.add(num);
                Log.w("TAG", "all num " + LocalContact.toString());
            } while (cursor.moveToNext());
        } db.close();
        return LocalContact;
    }



    public void  Add_Fambond_Contact(JSONObject tp){

        Log.w("TAMM", "values are adding " + tp.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_USER_ID, tp.getString("_id"));
            values.put(KEY_FIRSTNAME, tp.getString("name"));
            values.put(KEY_LASTNAME, tp.getString("name"));
            values.put(KEY_PHONE, tp.getString("phone"));
           // values.put(KEY_DOB, tp.getString("dob"));

            db.insert(TABLE_FAME_Contact, null, values);
            values.clear();

            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//   inserted detail  on login
    public void self_user_detail(JSONObject tp){

        Log.w("TAMM", "self detail " + tp.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(KEY_USER_ID, tp.getString("_id"));
            values.put(KEY_FIRSTNAME, tp.getString("name"));
            values.put(KEY_LASTNAME, tp.getString("name"));
            values.put(KEY_PHONE, tp.getString("phone"));
            values.put(KEY_DOB, tp.getString("dob"));

            db.insert(TABLE_SELFUSER_DETAIL, null, values);
            values.clear();

            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String return_userID(){

        String user_id = null;

        String query = " SELECT " + KEY_USER_ID  + " FROM " + TABLE_SELFUSER_DETAIL + " ORDER BY id";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            do {

                user_id  = cursor.getString(0);
                Log.w("TAG", "user_id " + user_id);
            } while (cursor.moveToNext());
        } db.close();
        return user_id;
    }



    // change this func acc to   new data

    public ArrayList<Self_UserDetail>  return_userDetail(){

        ArrayList<Self_UserDetail> detail_List = new ArrayList<>();

        String query = " SELECT  * FROM " + TABLE_SELFUSER_DETAIL + " ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();

        Self_UserDetail tp = null ;
        if (cursor.moveToFirst()) {
            do {
                tp = new  Self_UserDetail();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.userId= cursor.getString(1);
                tp.firstname =  cursor.getString(2);
                tp.lastname= cursor.getString(3);
                tp.phone = cursor.getString(4);
                tp.dob = cursor.getString(5);

                detail_List.add(tp);
            } while (cursor.moveToNext());
        }

        db.close();
        Log.e("userDetail", "user //" +detail_List.toString());
        return  detail_List ;

    }




    // return famBond contactName

    public ArrayList<Models> user_contactName(){

        ArrayList<Models> contactName= new ArrayList<>();

        String query = " SELECT " + KEY_FIRSTNAME  + " FROM " + TABLE_FAME_Contact + " ORDER BY id";
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
                tp.pic =  R.drawable.lorem;
                contactName.add(tp);

            } while (cursor.moveToNext());
        } db.close();
        return contactName;
    }


    // return fambond user_id on tha basis of  fambond name

    public ArrayList<String> return_ContactName_userId(ArrayList<String> contactName){

        ArrayList<String> user_List = new ArrayList<>();
       for (int i=0; i< contactName.size(); i++) {
           String query = " SELECT " +  KEY_USER_ID + " FROM " + TABLE_FAME_Contact + " where " + KEY_FIRSTNAME+ "  like '" + contactName.get(i) + "' ORDER BY id";
           SQLiteDatabase db = this.getWritableDatabase();
           while (db.inTransaction()) {

           }
           db.beginTransaction();
           Cursor cursor = db.rawQuery(query, null);
           db.endTransaction();
           if (cursor.moveToFirst()) {
               do {

                   String user_id = cursor.getString(0);
                   user_List.add(user_id);

               } while (cursor.moveToNext());
           }
           db.close();
       }
        Log.w("TAG", "Hii this is userID List "+ user_List.toString());
        return user_List;
    }




    public void delete_Contacts()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Contact, null, null);
        db.close();
    }

    public void delete_fambond_contact(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAME_Contact, null, null);
        db.close();
    }

    public void delete_self_user_detail(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELFUSER_DETAIL, null, null);
        db.close();
    }
}
