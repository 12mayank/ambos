package co.jlabs.famb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import co.jlabs.famb.R;

/**
 * Created by Jlabs-Win on 23/02/2017.
 */

public class Spinner_Adapter extends BaseAdapter {

    Context context;

    String[] gender;
    LayoutInflater inflter;

    public Spinner_Adapter(Context applicationContext,  String[] gender) {
        this.context = applicationContext;

        this.gender = gender;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);

        TextView names = (TextView) view.findViewById(R.id.male);

        names.setText(gender[i]);
        return view;
    }
}
