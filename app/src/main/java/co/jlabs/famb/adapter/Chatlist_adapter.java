package co.jlabs.famb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.Chat_Album;
import co.jlabs.famb.R;

/**
 * Created by Jlabs-Win on 04/01/2017.
 */

public class Chatlist_adapter extends RecyclerView.Adapter<Chatlist_adapter.MyViewHolder> {

       private Context mContext;

       private ArrayList<Chat_Album>albumList ;
    int[] drawables = new int[] {
        R.drawable.plant1,
                R.drawable.plant2,
                R.drawable.plant3
    };


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




       public Chatlist_adapter(Context mContext, ArrayList<Chat_Album> albumList) {
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
        holder.name.setText(album.getGroup_name());
        holder.image.setImageResource(drawables[position % 3]);



        // loading album cover using Glide library
       // Glide.with(mContext).load(album.getImage()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
