package co.jlabs.famb;

/**
 * Created by Jlabs-Win on 08/02/2017.
 */

public class Message_fromDb  {

    public int id ;
    public String groupId ;
    public String timeStamp ;
    public String send_date;
    public int  isMine;
    public String  msg_Type;
    public String  message;
    public int  isDelivered;
    public String  media_url;
    public String message_id ;
    public int progress;
    public String sender_Name ;
    public String sender_id ;
    public String sender_phone;
    //public String senderNumber;
   // public String senderName;


   // public String url;
 // poll variable

//    public int poll_id;
//    public String poll_subject;
//    public String poll_notes;
//    public int poll_count ;
//    public String poll_text;


    public Message_fromDb(){

        groupId = new String("0");
        timeStamp = new String();
        send_date = new String();
        isMine = new Integer("0");
        msg_Type = new String();
        message = new String();
        isDelivered = new Integer("0");
        media_url = new String();
        message_id = new String();
        progress = new Integer("0");

        sender_Name = new String();
        sender_id = new String();
        sender_phone = new String();

    }
}
