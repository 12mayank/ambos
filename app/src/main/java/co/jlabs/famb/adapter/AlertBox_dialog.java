package co.jlabs.famb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.famb.R;
import co.jlabs.famb.checkBox.CircleCheckBox;

/**
 * Created by Jlabs-Win on 24/02/2017.
 */

public class AlertBox_dialog extends BaseAdapter {

    Context mContext;
    ArrayList<String> arr;




    public AlertBox_dialog(Context mContext, ArrayList<String> arr){

        this.mContext = mContext;
        this.arr = arr;


    }


    static class ViewHolder{

        CircleCheckBox chk;
       // CheckBox chk;
        TextView name;
    }





    public View getView(final int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.alert_item_layout,null);
            viewHolder = new ViewHolder();

           // viewHolder.chk = (CircleCheckBox) gridView.findViewById(R.id.circle_check_box);
           // viewHolder.name = (TextView) gridView.findViewById(R.id.option_alert);
            gridView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) gridView.getTag();
        }

//           viewHolder.chk.setTag(position);
//           viewHolder.name.setTag(position);
        viewHolder.name.setText(arr.get(position).toString());



        //viewHolder.chk.setOnClickListener(onStateChangedListener(viewHolder.chk, position));

        return gridView;
    }






    @Override
    public int getCount() {

        Log.e("TAFF", "arrayLength" + arr.size());
        return arr.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
