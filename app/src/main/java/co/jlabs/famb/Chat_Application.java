package co.jlabs.famb;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Jlabs-Win on 23/01/2017.
 */

//public class Chat_Application extends Application {
public class Chat_Application  {
    private static Socket mSocket;

//    {
//
//
//        try {
//           // mSocket = IO.socket("http://192.168.0.114:3001");
//            mSocket = IO.socket("http://52.221.229.53:8000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    public  static  void setSocket(Socket mSocket){

        Chat_Application.mSocket = mSocket;

    }


    public static Socket getSocket() {
        return mSocket;
    }
}
