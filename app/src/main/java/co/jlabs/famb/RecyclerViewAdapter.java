package co.jlabs.famb;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.jlabs.famb.checkBox.CircleCheckBox;

/**
 * Created by JLabs on 12/20/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FakeViewHolder> {

    int[] drawables;
    public ShareInf mAdapterCallback;
    int[] names;
    ArrayList <String>ar=new ArrayList<String>();
    ArrayList <String>ar1=new ArrayList<String>();
    ArrayList <Integer>arInt=new ArrayList<Integer>();
    ArrayList<Integer> ar_userid = new ArrayList<Integer>();

     List<Models> mModelList;


    public RecyclerViewAdapter(List<Models> modelList,ShareInf mAdapterCallback) {

        Log.i("arraylist", "check :" + modelList.size());
        mModelList = modelList;
        this.mAdapterCallback = mAdapterCallback;
    }


    public RecyclerViewAdapter(int index,ShareInf mAdapterCallback) {
        this.mAdapterCallback = mAdapterCallback;

        if (index==1) {
            drawables = new int[] {
                    R.drawable.plant1,
                    R.drawable.plant2,
                    R.drawable.plant3
            };
            names = new int[] {
                    R.string.name1,
                    R.string.name2,
                    R.string.name3
            };
        }

    }




    @Override
    public FakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_add_ppl, parent, false));
    }

    @Override
    public void onBindViewHolder(final FakeViewHolder holder, final int position) {
        final Models model = mModelList.get(position);


        holder.imageView.setImageResource(model.getPic());
        holder.imageView.setTag(Integer.valueOf(model.getPic()));
        holder.name_ppl.setText(model.getText());
//        holder.user_id11.setText(model.getId());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if( holder.circleCheckBox.getVisibility()==View.VISIBLE){
                    Log.e("yes",""+  (model.isSelected() ? holder.name_ppl.getText().toString():""))
                    ;

                    model.setSelected(!model.isSelected());
                    holder.add.setBackgroundColor(model.isSelected() ?  Color.parseColor("#216480"): Color.parseColor("#175E7B"));
                   // ar.add(model.isSelected() ?   (holder.name_ppl.getText().toString()) : "");
                if(model.isSelected()){
                    ar.add(holder.name_ppl.getText().toString());
                   // ar_userid.add(model.getId());
                    Log.i("TAG", "user_id value"+ ar_userid.toString());
                    arInt.add(model.getPic());
                }else{
                     Set<String> set = new HashSet<String>(ar);


                        if (set.contains(holder.name_ppl.getText().toString())){
                            ar.remove(holder.name_ppl.getText().toString());
                           // Log.i("TAG","user_id size :" + ar_userid.size());


//                                   for (int i = 0; i < ar_userid.size(); i++) {
//                                       try {
//                                           Log.i("TAG", "user_id remove" + ar_userid.get(position));
//                                           ar_userid.remove(ar_userid.get(position));
//
//                                       } catch (Exception e) {
//                                           e.printStackTrace();
//                                       }
//                                   }


                            arInt.remove(holder.imageView.getTag());
                        }

                }
                    holder.circleCheckBox.setVisibility(model.isSelected() ? View.VISIBLE : View.GONE);

                //if(ar.size()>0){
                    mAdapterCallback.onMycall(ar,arInt);
                //}

                //Arrays.asList(name).add(holder.name_ppl.getText().toString());


                   // holder.circleCheckBox.setVisibility(View.GONE);
                    //holder.add.setBackgroundColor(Color.parseColor("#175E7B"));

                //}
//                else  {
//                    holder.circleCheckBox.setVisibility(View.VISIBLE);
//                    //holder.circleCheckBox.performClick();

//                   // name[position]=holder.name_ppl.getText().toString();
//                   // Log.e("no",""+holder.name_ppl.getText().toString());
//                    holder.add.setBackgroundColor(Color.parseColor("#216480"));
//                    holder.circleCheckBox.setChecked(true);
//                    holder.add.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
//                }


                //mAdapterCallback.onMethodCallback(holder.name_ppl.getText().toString());

            }
        });


    }

    @Override
    public int getItemCount() {
        return mModelList.size();
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
            circleCheckBox = (CircleCheckBox) itemView.findViewById(R.id.circle_check_box);
        }
    }
}