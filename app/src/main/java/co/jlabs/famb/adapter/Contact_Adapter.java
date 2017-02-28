package co.jlabs.famb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import co.jlabs.famb.Models;
import co.jlabs.famb.R;
import co.jlabs.famb.RecyclerViewAdapter;
import co.jlabs.famb.ShareInf;
import co.jlabs.famb.checkBox.CircleCheckBox;

/**
 * Created by Jlabs-Win on 25/02/2017.
 */

public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.FakeViewHolder> {

    List<Models> mModelList;
    Context context ;


    public Contact_Adapter(Context context,List<Models> modelList) {

       this.context = context ;
        this.mModelList = modelList;

    }


    public  class FakeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_ppl;
        RelativeLayout add;
        CircleCheckBox circleCheckBox;
        public FakeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_ppl);
            name_ppl = (TextView) itemView.findViewById(R.id.name_ppl);

            add = (RelativeLayout) itemView.findViewById(R.id.cool);
            //circleCheckBox = (CircleCheckBox) itemView.findViewById(R.id.circle_check_box);
        }
    }


    @Override
    public int getItemCount() {
        return mModelList.size();
    }




        @Override
        public Contact_Adapter.FakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_add_ppl, parent, false));
    }

    @Override
    public void onBindViewHolder(final Contact_Adapter.FakeViewHolder holder, final int position) {
        final Models model = mModelList.get(position);
        holder.imageView.setImageResource(model.getPic());
        holder.imageView.setTag(Integer.valueOf(model.getPic()));
        holder.name_ppl.setText(model.getText());

    }
}
