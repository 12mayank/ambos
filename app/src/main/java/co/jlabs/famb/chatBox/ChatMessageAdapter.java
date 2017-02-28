package co.jlabs.famb.chatBox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.tonyodev.fetch.Fetch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import co.jlabs.famb.Message_fromDb;
import co.jlabs.famb.R;
import co.jlabs.famb.Rounded.CircularImageView;
import co.jlabs.famb.activityArea.ChatBox;
import co.jlabs.famb.database.Maindb;

/**
 * Created by mayank on 06/09/15.
 */
public class ChatMessageAdapter extends ArrayAdapter<Message_fromDb>  {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3, MY_VIDEO = 4, OTHER_VIDEO = 5 , Not_Exist =6;

    //

    public ChatMessageAdapter(Context context,  List<Message_fromDb> data) {
      super(context, R.layout.item_mine_message,data);

    }

     @Override
     public void notifyDataSetChanged(){
         super.notifyDataSetChanged();
     }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image,my video, other video, no exist
        return 6;
    }




    @Override
    public int getItemViewType(int position) {
        //ChatMessage item = getItem(position);
        Message_fromDb item = getItem(position);

//        if (item.isMine() && !item.isImage() && !item.isVideo()) return MY_MESSAGE;
//        else if (!item.isMine() && !item.isImage() && !item.isVideo()) return OTHER_MESSAGE;
//        else if (item.isMine() && item.isImage() && !item.isVideo()) return MY_IMAGE;
//        else if(!item.isMine() && item.isImage() && !item.isVideo()) return OTHER_IMAGE;
//        else if (item.isMine() && !item.isImage() && item.isVideo()) return MY_VIDEO;
//        else  return OTHER_VIDEO ;


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);

          textView.setText(getItem(position).message);
            //textView.setText(getItem(position).getContent()); // this is from ChatMessage class


        } else if (viewType == OTHER_MESSAGE) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            ChatMessageView chat_layout = (ChatMessageView) convertView.findViewById(R.id.chatMessageView);
            ChatMessageView Poll_layout = (ChatMessageView) convertView.findViewById(R.id.pollMessageView);
            CircularImageView cc = (CircularImageView) convertView.findViewById(R.id.img);
            TextView subject_poll = (TextView) convertView.findViewById(R.id.subject_poll);
            TextView note_poll = (TextView) convertView.findViewById(R.id.note_poll);
            TextView optionText = (TextView) convertView.findViewById(R.id.optionText);
            TextView optionText2 = (TextView) convertView.findViewById(R.id.optionText2);
            TextView poll_count = (TextView) convertView.findViewById(R.id.poll_count);
            TextView poll_count2 = (TextView) convertView.findViewById(R.id.poll_count2);

//            textView.setText(getItem(position).message);
            String message = getItem(position).message;


                Log.i("hiie this is running", "message is not null");
                chat_layout.setVisibility(View.VISIBLE);
                cc.setVisibility(View.VISIBLE);
                textView.setText(getItem(position).message); // this is from ChatMessage class


        } else if (viewType == MY_IMAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
            ImageView img=(ImageView)convertView.findViewById(R.id.my_new_image);
            VideoView videoView = (VideoView) convertView.findViewById(R.id.my_video);

            img.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            String path = getItem(position).media_url;
            Log.d("TTYY","image path "+path);
            Uri uri = Uri.fromFile(new File(path));
            Picasso.with(getContext()).load(uri).resize(600,550).centerInside().into(img);
//            Bitmap myBitmap = BitmapFactory.decodeFile(path);
//            img.setImageBitmap(myBitmap);

        } else if (viewType == OTHER_IMAGE){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_image, parent, false);
//            ViewHolder viewHolder = new ViewHolder();
            FrameLayout f1 = (FrameLayout) convertView.findViewById(R.id.frame1);
            ImageView img1 = (ImageView) convertView.findViewById(R.id.other_image);
            VideoView vv2  = (VideoView) convertView.findViewById(R.id.other_video);
            CircleProgressView c1 = (CircleProgressView) convertView.findViewById(R.id.progress1);
          //  viewHolder.c1 = (CircleProgressView) convertView.findViewById(R.id.progress1);

            f1.setVisibility(View.VISIBLE);
            vv2.setVisibility(View.GONE);
            Log.i("TAG", "media value" + getItem(position).media_url);
//            convertView.setTag(viewHolder);

           // Bitmap bitmap1 = getItem(position).getContents();// this is from ChatMessage class
            String downloadPath = getItem(position).media_url;
            if (downloadPath != null){

                Log.i("TAG", "code is here :");
                String imagePath = getItem(position).media_url;
                Uri uri = Uri.fromFile(new File(imagePath));
                Picasso.with(getContext()).load(uri).resize(600,550).centerInside().into(img1);
//                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
//                img1.setImageBitmap(myBitmap);
            }else{
                Log.i("value ", " progress value :" + getItem(position).progress);
                // img1.setImageResource(R.drawable.download_image);
                Picasso.with(getContext()).load(R.drawable.download_image).resize(600,550).centerInside().into(img1);
            }

        } else if(viewType == MY_VIDEO) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
            ChatMessageView mview = (ChatMessageView) convertView.findViewById(R.id.chatMessageView);
            final VideoView videoView = (VideoView) convertView.findViewById(R.id.my_video);
            ImageView img = (ImageView) convertView.findViewById(R.id.my_new_image);
            img.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            Log.i("TAG45", "video " + getItem(position).media_url);
            // Uri uri = Uri.parse(getItem(position).getContent());
           // videoView.setVideoPath(getItem(position).getContent());
            videoView.setMediaController(new MediaController(getContext()));
            videoView.setVideoPath(getItem(position).media_url);
            videoView.requestFocus();


            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (videoView.isPlaying()) {

                        videoView.stopPlayback();
                    } else {
                        videoView.start();

                    }


                }
            });
        }else {

           // Log.i("TAG545","video "+ getItem(position).getContent());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_image, parent, false);
            FrameLayout f1 = (FrameLayout) convertView.findViewById(R.id.frame1);
            final VideoView vv2  = (VideoView) convertView.findViewById(R.id.other_video);
            f1.setVisibility(View.GONE);
            vv2.setVisibility(View.VISIBLE);

            String downloadPath = getItem(position).media_url;
            if (downloadPath != null){

                vv2.setMediaController(new MediaController(getContext()));
                vv2.setVideoPath(downloadPath);
                // vv2.setVideoURI(uri);
                vv2.requestFocus();

                vv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (vv2.isPlaying()) {

                            vv2.stopPlayback();
                        } else {
                            vv2.start();

                        }

                    }
                });

            }else {
                vv2.setBackgroundResource(R.drawable.videoview);

            }
        }


//        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
//            }
//        });


        return convertView;
    }





}
