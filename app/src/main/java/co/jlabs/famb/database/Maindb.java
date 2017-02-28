package co.jlabs.famb.database;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.Message_fromDb;
import co.jlabs.famb.Models;

/**
 * Created by Jlabs-Win on 07/02/2017.
 */

public class Maindb  {

    Chatdb chatdb;
    Contactdb contactdb;

   public Maindb(Context context){

        chatdb = new Chatdb(context);
       contactdb = new Contactdb(context);
    }


    public void add_All_Group(JSONArray array){

        chatdb.add_All_Group(array);
    }

    public ArrayList<Chat_Album> return_groupInfo(){


      return   chatdb.return_groupInfo();
    }


    public void add_Group_Locally(String grp_id, String grp_name, String user_id, int isAdmin){

        chatdb.add_group_Locally(grp_id, grp_name, user_id, isAdmin);
    }

    public void add_msg_toLocal(Message_fromDb msg){

        chatdb.add_msg_ToDb(msg);
    }

    public ArrayList <Message_fromDb> return_allMsg(String grp_id){

        return chatdb.return_chat(grp_id);
    }

    public ArrayList<Message_fromDb> one_by_oneMesage(String grp_id){


        return chatdb.return_Last_Message(grp_id);
    }


    // update progress

    public Boolean updateProgressIn_db(int progress, String message_id, String grp_id){

        Log.d("Down" , "check downloading" + progress);
        boolean success = chatdb.update_progress(progress, message_id, grp_id);
        Log.i("TAGG", "boolean val"+ success);

        return success;
    }

    // update medial url

    public void Add_download_Image_url(String filePath, String message_id, String grp_id){

        Log.d("Down1" , "downloaded file path " + filePath);
        chatdb.update_media_url(filePath, message_id, grp_id);
    }


    public ArrayList<Models>  return_BondAndName(){

        //ArrayList<Models> arr = new ArrayList<>();

         //arr.addAll(chatdb.return_groupName());
        // arr.addAll(contactdb.user_contactName());
        //return arr;

        return  chatdb.return_groupName();
    }

    public ArrayList<String> return_UserId_grp_id(ArrayList<String> arr){

        ArrayList<String> arrayList = new ArrayList<>();

//        arrayList.addAll(contactdb.return_ContactName_userId(arr));
//        arrayList.addAll(chatdb.return_grp_ID(arr));
//         return arrayList;

        return  chatdb.return_grp_ID(arr);
    }



    // delete group_list

    public void delete_group(){

        chatdb.delete_All_Group();
    }

    public void participantsList(){

        chatdb.delete_ParticipantsList();
    }
}
