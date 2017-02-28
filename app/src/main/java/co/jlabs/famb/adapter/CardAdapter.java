package co.jlabs.famb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.R;

/**
 * Created by Jlabs-Win on 27/02/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {


    private Context mContext;

    private ArrayList<Chat_Album>albumList ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image ;
        public Button add_btn ;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_ppl);
            image = (ImageView) view.findViewById(R.id.img_ppl);
            add_btn = (Button) view.findViewById(R.id.add);

        }
    }


    public CardAdapter(Context mContext, ArrayList<Chat_Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adap_invite_ppl, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.add_btn.setVisibility(View.GONE);
        Chat_Album album = albumList.get(position);
        holder.name.setText(album.group_name); //here group name means memberName
      //  holder.image.setImageResource(drawables[position % 3]);

        Log.e("group_Info", "group_Memeber" + album.group_name);

       if (album.profile_pic.equals("") ){

           Picasso.with(mContext).load(R.drawable.lorem).placeholder(R.drawable.lorem).error(R.drawable.lorem).into(holder.image);
       }else
        Picasso.with(mContext).load(album.profile_pic).placeholder(R.drawable.lorem).error(R.drawable.lorem).into(holder.image);
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
