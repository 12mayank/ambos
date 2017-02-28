/*
    Copyright 2016 Arnaud Guyon

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package co.jlabs.famb.frag;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.jlabs.famb.AddPpl;
import co.jlabs.famb.AppController;
import co.jlabs.famb.Chat_Application;
import co.jlabs.famb.Conn;
import co.jlabs.famb.Local_Contact;
import co.jlabs.famb.MakeBond;
import co.jlabs.famb.Models;
import co.jlabs.famb.NewBond;
import co.jlabs.famb.NewBond2;
import co.jlabs.famb.R;
import co.jlabs.famb.RecyclerViewAdapter;
import co.jlabs.famb.ShareInf;
import co.jlabs.famb.Static_Catelog;
import co.jlabs.famb.adapter.Contact_Adapter;
import co.jlabs.famb.database.Contactdb;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Example of Fragment using TabStacker.
 * It is constructed using the createInstance() pattern with arguments
 * The View hierarchy is saved and restored.
 * The title is displayed at a random place
 */

public class TabFragment extends Fragment implements TabStacker.TabStackInterface {

    private static final String TAG = "TabFragment";

    private static final String ARGUMENT_TITLE = "title";
    private static final String ARGUMENT_COLOR = "color";
    private static final String ARGUMENT_RANDOM_TOP = "randomTop";

    // a reference to the view must be kept so that the view can be saved
    private View mView;
    FloatingActionButton bn;
// interface for make connection in Tab fragment
    Conn Conn;
    Socket mSocket;
    String clientSocket;
    Chat_Application app1;
    List<Models> mModelList = new ArrayList<Models>();

    Contactdb contactdb;
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};


    public static final int MULTIPLE_PERMISSIONS = 10;
    ArrayList<String> send_num;
    private static final String URL = "http://52.221.229.53:8000/" ;
    ProgressDialog pDialog;
    private RecyclerView recyclerView;
    Contact_Adapter mAdapter;



    // constructor
    public static TabFragment createInstance(String title, int color) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TITLE, title);
        bundle.putInt(ARGUMENT_COLOR, color);
        bundle.putFloat(ARGUMENT_RANDOM_TOP, (float) (Math.random() * 0.6 + 0.2));

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.testfragment, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
      //  FloatingActionMenu fm=(FloatingActionMenu)mView.findViewById(R.id.menu);
       //  bn = (FloatingActionButton)mView.findViewById(R.id.material_design_floating_action_menu_item2);
       // fm.setIconAnimated(false);
        Conn = new MakeBond().return_connection();
        mSocket = Conn.getSocket();
        contactdb = new Contactdb(getContext());
        Log.i("TAG", "socket value in Frag"+ mSocket);
        mModelList  = contactdb.user_contactName();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i("Activity", "check " + mModelList.size());
        mAdapter=new Contact_Adapter(getContext(),mModelList);
        recyclerView.setAdapter(mAdapter);

        clientSocket = Static_Catelog.getStringProperty(getContext(),"clientSocket");
        if (checkPermissions()){
            //  permissions  granted.

           // new Contact().execute();
        }




        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Bundle arguments = getArguments();
//
//        String title = arguments.getString(ARGUMENT_TITLE);
//        int color = arguments.getInt(ARGUMENT_COLOR);
//        float randomTop = arguments.getFloat(ARGUMENT_RANDOM_TOP);
//
//        // SET BACKGROUND RANDOM COLOR
//        view.setBackgroundColor(color);
//
//        // SET FRAGMENT TITLE
//        TextView titleView = (TextView) view.findViewById(R.id.titleView);
//        titleView.setText(title);
//        centerTitle(randomTop);

        // Asks the MainActivity to restore the View hierarchy (the activity holds the TabStacker)
        MakeBond activity = (MakeBond) getActivity();
        activity.restoreView(this, view);
    }

    private void centerTitle(float topValue) {

        {
            View topView = getView().findViewById(R.id.topView);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
           // params.weight = topValue;
            topView.setLayoutParams(params);
        }

//        {
//            float bottomValue = 1 - topValue;
//            View bottomView = getView().findViewById(R.id.bottomView);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.weight = bottomValue;
//            bottomView.setLayoutParams(params);
//        }

    }

    // Called when a Fragment is presented on Screen
    @Override
    public void onTabFragmentPresented(TabStacker.PresentReason reason) {
        // Logs the Reason and Fragment name
        Bundle arguments = getArguments();
        String title = arguments.getString(ARGUMENT_TITLE);
        Log.i(TAG, "PRESENT " + title + " (" + reason.name() + ")");
    }

    // Called when a Fragment is dismissed from Screen
    @Override
    public void onTabFragmentDismissed(TabStacker.DismissReason reason) {
        // Logs the Reason and Fragment name
        Bundle arguments = getArguments();
        String title = arguments.getString(ARGUMENT_TITLE);
        Log.i(TAG, "DISMISS " + title + " (" + reason.name() + ")");
    }

    // called when it's time to save some precious data
    @Override
    public View onSaveTabFragmentInstance(Bundle outState) {
        // You can add here some precious data to be saved in the bundle
        // getView() is always null, so needs to keep a reference so that the view values can be restored correctly later
        return mView;
    }

    // called to restore your precious data
    @Override
    public void onRestoreTabFragmentInstance(Bundle savedInstanceState) {
        // You could retrieve here some precious data that has been saved with onSaveTabFragmentInstance
    }


    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
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
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            ContentResolver cr = getActivity().getContentResolver();
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




}
