package co.jlabs.famb;

import android.os.Bundle;


import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.adapter.CardAdapter;


/**
 * Created by Jlabs-Win on 27/02/2017.
 */

public class GroupInfo_Class extends AppCompatActivity   {


    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    String room_id, room_name;
    RecyclerView recyclerView;
    CardAdapter adapter ;
    ArrayList<Chat_Album> mArrayList;
    TextView textView ;

    private static final String URL = "http://52.221.229.53:8000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        room_id = getIntent().getStringExtra("room_id");
        room_name = getIntent().getStringExtra("room_name");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(room_name);
        mArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);
        textView = (TextView) findViewById(R.id.total_member);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new CardAdapter(this, )


        toolbarTextAppernce();
        getMemberList();
    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    public void getMemberList(){


        String tag_json_obj = "json_obj_req";

        String url_groupInfo =  URL +"getGroupInfo" ;

        JSONObject obj = new JSONObject();
        try {
            obj.put("_id", room_id);

            Log.e("obj","obj"+obj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                url_groupInfo,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Volley", "res :" + response);

                        try {
                            boolean Success = response.getBoolean("success");

                            if (Success){

                            mArrayList.clear();

                                JSONObject obj = response.getJSONObject("data");

                                JSONArray member = obj.getJSONArray("members");
                                Chat_Album album ;
                                 textView.setText(String.valueOf(member.length()) +" " + "Of 50");
                                Log.e("Total memeber", "mem" + member.length());
                                for (int i =0; i< member.length() ; i++){

                                    album = new Chat_Album();

                                    JSONObject obj1 = member.getJSONObject(i);

                                    String name = obj1.getString("name");
                                    String picture = obj1.getString("picture");

                                    album.group_name  = name;
                                    album.profile_pic = picture;

                                    mArrayList.add(album);
                                 }
                                adapter = new CardAdapter(getApplicationContext(), mArrayList);
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

   // use it

//    private void dynamicToolbarColor() {
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.profile_pic);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//
//            @Override
//            public void onGenerated(Palette palette) {
//                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
//                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark);
//            }
//        });
//    }
}