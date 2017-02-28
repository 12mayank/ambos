package co.jlabs.famb;



/**
 * Created by Jlabs-Win on 04/01/2017.
 */

public class Chat_Album  {

    public int id;
    public int image;
    public  String group_name;
    public String grp_id;
    public String user_id;
    public  int isAdmin;
    public String profile_pic;


   public Chat_Album(){

        image = new Integer("0");
        group_name = new String();
        grp_id = new String();
       user_id = new String();
       isAdmin = new Integer("0");
       profile_pic = new String();
    }

    public void setGroup_name (String grp_name){

        this.group_name = grp_name ;
    }

    public String getGroup_name(){

        return  group_name ;
    }

    public void setImage(int image){

        this.image = image;
    }

    public int getImage(){

        return image;
    }



}
