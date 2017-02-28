package co.jlabs.famb.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import co.jlabs.famb.AppController;
import co.jlabs.famb.Feed_Data;
import co.jlabs.famb.R;
import co.jlabs.famb.checkBox.CircleCheckBox;
import co.jlabs.famb.database.Contactdb;

/**
 * Created by Jlabs-Win on 21/02/2017.
 */

public class New_Feed_Adapter  extends RecyclerView.Adapter<New_Feed_Adapter.MyViewHolder> {


     Context mContext;
      ArrayList<Feed_Data> pollList;
    int selectionPosition = -1 ;
    String userID ;



    String nameOP;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subject;
        public LinearLayout ll;
        public ImageView im;


        public MyViewHolder(View view) {
            super(view);
            subject = (TextView) view.findViewById(R.id.subject);
            ll = (LinearLayout) view.findViewById(R.id.optionLayout);
            im = (ImageView) view.findViewById(R.id.imageOption);
        }
    }


    public New_Feed_Adapter(Context mContext, ArrayList<Feed_Data> pollList) {
        this.mContext = mContext;
        this.pollList = pollList;

        Log.e("TAAY", "Poll : "+ pollList.toString());
    }

    @Override
    public New_Feed_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_feed_adapter, parent, false);

        return new MyViewHolder(itemView);
    }
//

    @Override
    public void onBindViewHolder(final New_Feed_Adapter.MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
//        Chat_Album album = albumList.get(position);
//        holder.name.setText(album.getGroup_name());
//        holder.image.setImageResource(drawables[position % 3]);
          Feed_Data data1 = pollList.get(position);
          holder.subject.setText(data1.getSubject());

        JSONArray arr = data1.getPp();

        for (int i=0; i< arr.length(); i++){

            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.option_layout, null);

            final  TextView tv = (TextView) v.findViewById(R.id.optiontext1);
            final  ProgressBar pb = (ProgressBar)v.findViewById(R.id.progressBar1);

            try {
                JSONObject obj = arr.getJSONObject(i);
                String text = obj.getString("name");
                int count = obj.getInt("count");

                tv.setText(text+"("+count+")");
                pb.setProgress(count);

             holder.ll.addView(v);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String poll_id = pollList.get(position).getPoll_id();

                JSONArray ab = pollList.get(position).getPp();
                Log.i("TAG", "UIOP"+ ab.toString() + ",,,," + poll_id);

                ArrayList<String> arrra = new ArrayList<String>();

                for (int i=0; i< ab.length(); i++){

                    try {
                        JSONObject obb = ab.getJSONObject(i);

                        arrra.add(obb.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Alert_dialog( poll_id, arrra);

            }
        });

    }
    @Override
    public int getItemCount() {

       return pollList.size();
    }



     public void Alert_dialog(final String poll_id, final ArrayList<String> arrayList){

         //final ListView listView ;
          RadioGroup listView;

         Button done;
         RadioButton radioButton;


         final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        // LayoutInflater inflater = mContext.getLayoutInflater();
         LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        // dialogBuilder.setCustomTitle(dialogView);
         dialogBuilder.setView(dialogView);
        // listView = (ListView) dialogView.findViewById(R.id.alert_listview);
         listView = (RadioGroup) dialogView.findViewById(R.id.alert_listview);
         listView.clearCheck();
         done = (Button) dialogView.findViewById(R.id.done);
         Contactdb contactdb = new Contactdb(mContext);
          userID =  contactdb.return_userID();
//         dialogBuilder.setTitle("Cast Your Poll");

         dialogBuilder.setCancelable(true);

         for (int i=0; i<arrayList.size(); i++){

             LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View v = vi.inflate(R.layout.alert_item_layout, null);

              radioButton = (RadioButton) v.findViewById(R.id.radioButton);
            // final  ProgressBar pb = (ProgressBar)v.findViewById(R.id.progressBar1);

             radioButton.setText(arrayList.get(i).toString());
             radioButton.setId((i*2)+i);
             Log.e("TAG", "radio Button ID" + radioButton.getId());
             listView.addView(v);

         }

         listView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(RadioGroup group, int checkedId) {



                                                     for (int i = 0; i < listView.getChildCount(); i++) {
                                                         RadioButton btn = (RadioButton) listView.getChildAt(i);
                                                         Log.e("TAG11", "radio 22" + btn.getId());
                                                         if (btn.getId() == checkedId) {
                                                            // btn.setChecked(true);
                                                             nameOP = btn.getText().toString();
                                                             // do something with text
                                                             return;
                                                         }
                                                     }
                                                 }
                                             }
                                    );



         final AlertDialog alert11 = dialogBuilder.create();

         done.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

//                    listView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                         @Override
//                                         public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                                             for (int i = 0; i < listView.getChildCount(); i++) {
//                                                 RadioButton btn = (RadioButton) listView.getChildAt(i);
//                                                 if (btn.getId() == checkedId) {
//                                                     nameOP = btn.getText().toString();
//                                                     // do something with text
//                                                     return;
//                                                 }
//                                             }
//                                         }
//                                     }
//                                );
//
//                 nameOP = tv.getText().toString();
                 send_count(nameOP, poll_id);

                alert11.dismiss();
             }
         });


         alert11.show();

     }

    public void send_count(String name , String poll_id){


         String UrLL = "http://52.221.229.53:8000/sendPollResponse" ;
        String tag_json_obj = "json_obj_req";


        JSONObject obj = new JSONObject();

        try {
            obj.put("user_id",userID);
            obj.put("poll_id", poll_id);
            obj.put("option", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
         Log.e("send volley" ,"obj" + obj.toString());
        JsonObjectRequest req= new JsonObjectRequest(Request.Method.POST,
                UrLL,obj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("POll count", "res :" + response);

                        try {
                            boolean   Success = response.getBoolean("success");

                            if (Success){



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
}
