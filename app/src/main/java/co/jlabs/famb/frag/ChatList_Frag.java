package co.jlabs.famb.frag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import co.jlabs.famb.AddPpl;
import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.Chat_Application;
import co.jlabs.famb.Conn;

import co.jlabs.famb.MakeBond;
import co.jlabs.famb.Models;
import co.jlabs.famb.NewBond;
import co.jlabs.famb.R;
import co.jlabs.famb.RecyclerTouchListener;
import co.jlabs.famb.ShareInf;
import co.jlabs.famb.Static_Catelog;
import co.jlabs.famb.activityArea.ChatBox;
import co.jlabs.famb.adapter.Chatlist_adapter;

import co.jlabs.famb.database.Contactdb;
import co.jlabs.famb.database.Maindb;
import co.jlabs.famb.reload_data;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



/**
 * Created by Jlabs-Win on 04/01/2017.
 */

public class ChatList_Frag extends Fragment implements reload_data {

    RecyclerView recyclerView;
    Chatlist_adapter adapter;
    ArrayList<Chat_Album> mArrayList;
    Maindb maindb;
    Conn Conn;
    Chat_Application app1;
    Contactdb contactdb ;
    Socket mSocket;
    String userId;
    FloatingActionButton bn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.chatlist_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_chatList);
        FloatingActionMenu fm=(FloatingActionMenu)view.findViewById(R.id.menu);
        bn = (FloatingActionButton)view.findViewById(R.id.material_design_floating_action_menu_item2);
        maindb = new Maindb(getContext());
        contactdb = new Contactdb(getContext());
        //userId = Static_Catelog.getStringProperty(getContext(),"_id");
        userId = contactdb.return_userID();

        //mArrayList =  new ArrayList<Chat_Album>();
        mArrayList = maindb.return_groupInfo();

        Log.e("TAG","grp list "+ mArrayList.toString());
        Conn = new MakeBond().return_connection();
        mSocket = Conn.getSocket();
        Log.d("fff", "Chat Frag"+ mSocket.connected());
      //  getAllGroup();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Chatlist_adapter(getContext(), mArrayList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Chat_Album chat_album = mArrayList.get(position);
                Toast.makeText(getContext(), chat_album.group_name + " is selected!", Toast.LENGTH_SHORT).show();

                Intent myIntent  = new Intent(getContext(), ChatBox.class);
                myIntent.putExtra("group_name", chat_album.group_name);
                myIntent.putExtra("room_id", chat_album.grp_id);
                Log.i("TAGG", "roomID"+ chat_album.grp_id +" =="+ chat_album.user_id);
                app1.setSocket(mSocket);
                startActivity(myIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent((MakeBond)getActivity(), NewBond.class);
//                Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST",(Serializable)mModelList);
//                i.putExtra("BUNDLE",args);
                app1.setSocket(mSocket);
                startActivity(i);
            }
        });

        return view;
    }





    @Override
     public void now_reload_data(){

        try {
           // mArrayList = maindb.return_groupInfo();
           // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
           // adapter = new Chatlist_adapter(getContext(), mArrayList);
           // recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public reload_data return_reload_data_interface(){

        return this;
    }
}
