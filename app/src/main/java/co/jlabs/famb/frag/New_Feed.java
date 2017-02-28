package co.jlabs.famb.frag;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.AppController;
import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.ConnectionDetector;
import co.jlabs.famb.Feed_Data;
import co.jlabs.famb.Poll_reload;
import co.jlabs.famb.R;
import co.jlabs.famb.adapter.Chatlist_adapter;
import co.jlabs.famb.adapter.New_Feed_Adapter;
import co.jlabs.famb.database.Contactdb;

/**
 * Created by Jlabs-Win on 04/01/2017.
 */

public class New_Feed extends Fragment  implements Poll_reload {



    RecyclerView recyclerView;
    New_Feed_Adapter adapter;
    ArrayList<Feed_Data> mArrayList =  new ArrayList<Feed_Data>();;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private static final String URL = "http://52.221.229.53:8000/";
    String userID;
    Contactdb contactdb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chatlist_fragment, container, false);
        FloatingActionMenu fm=(FloatingActionMenu)view.findViewById(R.id.menu);
        fm.setVisibility(View.GONE);
        cd = new ConnectionDetector(getContext());
        contactdb = new Contactdb(getContext());
        userID = contactdb.return_userID();

       recyclerView = (RecyclerView) view.findViewById(R.id.recycler_chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent){

            getDataFromServer();
        }else {

            Toast.makeText(getContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        }



        return view ;
    }




  @Override
  public void reload_pollData(){

      getDataFromServer();
  }

    public  Poll_reload retutn_poll_data_interface(){

        return this;
    }

      private void getDataFromServer(){

           String poll_url = URL+"getPolls";

          String tag_json_obj = "json_obj_req";

          JSONObject obj = new JSONObject();

          try {
              obj.put("_id", userID);
          } catch (JSONException e) {
              e.printStackTrace();
          }

          JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                  poll_url,obj,
                  new Response.Listener<JSONObject>(){
                      @Override
                      public void onResponse(JSONObject response) {

                          Log.e("Volley122", "poll ress :" + response);

                          try {
                              boolean   Success = response.getBoolean("success");

                              if (Success){

                                  mArrayList.clear();
                                  JSONArray array = response.getJSONArray("data");

                                  for (int i =0; i< array.length(); i++){

                                      JSONObject obj = array.getJSONObject(i);

                                      String poll_id = obj.getString("_id");
                                      String title = obj.getString("title");

                                      JSONArray option_array = obj.getJSONArray("options");

                                      Feed_Data feed_data = new Feed_Data();

                                            feed_data.setSubject(title);
                                            feed_data.setPoll_id(poll_id);
                                            feed_data.setPp(option_array);
                                            mArrayList.add(0,feed_data);
                                  }

                                  adapter = new New_Feed_Adapter(getContext(),mArrayList);
                                  recyclerView.setAdapter(adapter);
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

    }
