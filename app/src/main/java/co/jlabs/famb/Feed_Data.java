package co.jlabs.famb;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Jlabs-Win on 21/02/2017.
 */

 public class Feed_Data {

    String subject;
    String poll_id;
    JSONArray pp ;


   public Feed_Data(){


    }

    public String getSubject(){

        return subject;
    }

    public void setSubject(String sub){

        this.subject = sub ;
    }


    public void setPp(JSONArray pp){

        this.pp = pp ;
    }

   public JSONArray getPp(){


       return pp;
   }

    public String getPoll_id(){

        return poll_id;
    }

    public void setPoll_id(String sub){

        this.poll_id = sub ;
    }



}
