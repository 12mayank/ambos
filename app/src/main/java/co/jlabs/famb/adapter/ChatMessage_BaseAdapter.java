package co.jlabs.famb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;
import co.jlabs.famb.Message_fromDb;
import co.jlabs.famb.R;
import co.jlabs.famb.Rounded.CircularImageView;

/**
 * Created by Jlabs-Win on 28/02/2017.
 */

public class ChatMessage_BaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<Message_fromDb> arrayList;

    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3, MY_VIDEO = 4, OTHER_VIDEO = 5 , Not_Exist =6;

    public ChatMessage_BaseAdapter(Context context, ArrayList<Message_fromDb> arrayList){

        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getItemViewType(int position){

        Message_fromDb item  = arrayList.get(position);

        if (item.isMine == 1){

            if (item.msg_Type.equals("text")){
                return MY_MESSAGE;
            }else if (item.msg_Type.equals("image")){

                return MY_IMAGE;
            }else {

                return MY_VIDEO ;
            }


        }else {
            if (item.msg_Type.equals("text")){
                return OTHER_MESSAGE;
            }else if (item.msg_Type.equals("image")){
                return OTHER_IMAGE;
            }else {

                return OTHER_VIDEO;
            }

        }
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }


   static class ViewHolder{

       public TextView textView;
       public CircularImageView circularImageView ;
       public ImageView imageView;
       public VideoView videoView;

       public FrameLayout frameLayout;
       public CircleProgressView circleProgressView;


   }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        final ViewHolder viewHolder;

        if (viewType == MY_MESSAGE){

            if (convertView == null){

                convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_message, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.textView = (TextView) convertView.findViewById(R.id.text);


            }else {


            }



        }else if (viewType == OTHER_MESSAGE){


        }else if (viewType == MY_IMAGE){

        }else if (viewType == OTHER_IMAGE){


        }else if (viewType == MY_VIDEO){


        }else {


        }

        return convertView;
    }

}
