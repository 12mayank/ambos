package co.jlabs.famb;

/**
 * Created by Jlabs-Win on 06/02/2017.
 */

public class Fambond_Contact_Var {

    int id,user_id,isConnected ;
    String name, nickname, age, gender, email, number, platform ;

//
//    public Fambond_Contact_Var(int user_id,String name,String nickname,String age, String gender, String email, String number, String platform, int isConnected ){
//
//
//        this.user_id = user_id;
//        this.name = name ;
//        this.nickname = nickname;
//        this.age = age ;
//        this.gender = gender ;
//        this.email = email;
//        this.number = number;
//        this.platform = platform;
//        this.isConnected = isConnected;
//    }


    public Fambond_Contact_Var(){

        user_id = new Integer("0");
        name = new String();
        nickname = new String();
        age = new String();
        gender = new String();
        email = new String();
        number = new String();
        platform = new String();
        isConnected = new Integer("0");
    }

//    public int getUser_id(){
//        return  user_id;
//    }
//
//    public String getName(){
//
//        return name;
//    }
}
