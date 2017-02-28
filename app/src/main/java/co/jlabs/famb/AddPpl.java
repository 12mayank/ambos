package co.jlabs.famb;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.database.Contactdb;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class AddPpl extends AppCompatActivity implements ShareInf,View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter rc;
    private TextView skip;
    private LinearLayout activity_add_ppl;
    public static final String ARG_INDEX = "indexArg";
    Socket mSocket;
    public static final int MULTIPLE_PERMISSIONS = 10;
    Chat_Application app1;
    Contactdb contactdb;

    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    ProgressDialog pd;
    String clientSocket;
    ArrayList<String> send_num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ppl);
//        app1 = (Chat_Application) getApplication();
         mSocket = app1.getSocket();

        Log.i("TAG", "connected value : " + mSocket.toString());
        Log.i("TAG", "connected check : " + mSocket.connected());
       // Log.i("TAG","connect 2 :" + mSocket.connect().toString());
        contactdb = new Contactdb(this);
        clientSocket = Static_Catelog.getStringProperty(getApplicationContext(),"clientSocket");

        initView();

        if (checkPermissions()){
        //  permissions  granted.

            new Contact().execute();
    }
    }





    public  void onMethodCallback(String i) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        LinearLayout whatsapp=(LinearLayout)dialog.findViewById(R.id.whatsapp);
        TextView w=(TextView) dialog.findViewById(R.id.w);
        w.setText(i);
        LinearLayout sms=(LinearLayout)dialog.findViewById(R.id.sms);
        whatsapp.setOnClickListener(this);
        sms.setOnClickListener(this);
        dialog.show();


    }



    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        activity_add_ppl = (LinearLayout) findViewById(R.id.activity_add_ppl);
        skip = (TextView) findViewById(R.id.skip);
        AddPpl sf = new AddPpl();
        skip.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(1, this));
    }
    private static class RecyclerViewAdapter extends RecyclerView.Adapter<FakeViewHolder> {

        int[] drawables;
        public ShareInf mAdapterCallback;
        int[] names;





        public RecyclerViewAdapter(int index,ShareInf mAdapterCallback) {
            this.mAdapterCallback = mAdapterCallback;
            if (index==1) {
                drawables = new int[] {
                        R.drawable.plant1,
                        R.drawable.plant2,
                        R.drawable.plant3
                };
                names = new int[] {
                        R.string.plant1,
                        R.string.plant2,
                        R.string.plant3
                };
            }

        }

        @Override
        public FakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_invite_ppl, parent, false));
        }

        @Override
        public void onBindViewHolder(final FakeViewHolder holder, final int position) {
            holder.imageView.setImageResource(drawables[position % 3]);
            holder.name_ppl.setText(names[position % 3]);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        mAdapterCallback.onMethodCallback(holder.name_ppl.getText().toString());

                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }
    private static class FakeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_ppl;
        Button add;
        public FakeViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_ppl);
            name_ppl = (TextView) itemView.findViewById(R.id.name_ppl);
            add = (Button) itemView.findViewById(R.id.add);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.whatsapp:

                submit();
                break;
            case R.id.sms:
                submit1();
                break;
            case R.id.skip:
                Intent intent=new Intent(AddPpl.this, MakeBond.class);
                app1.setSocket(mSocket);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void submit(){
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "Download Fambond its awesome.";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        }
    }

    public void submit1(){


    }
    public  void onMycall(ArrayList<String> a,ArrayList<Integer> ars) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);
        LinearLayout whatsapp=(LinearLayout)dialog.findViewById(R.id.whatsapp);
        TextView w=(TextView) dialog.findViewById(R.id.w);
        //w.setText(i);
        LinearLayout sms=(LinearLayout)dialog.findViewById(R.id.sms);
        whatsapp.setOnClickListener(this);
        sms.setOnClickListener(this);

        dialog.show();


    }


    @Override
    public void onBackPressed()
    {
        if(mSocket.connected()){

            mSocket.disconnect();
        }

        finish();

    }


    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    new Contact().execute();

                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }


    class Contact extends AsyncTask<String, Void, String> {



        ArrayList<Local_Contact> alContacts = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(AddPpl.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait we are fetching contacts..");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

            Log.w("TAG", "contact is fetching");
        }

        @Override
        protected String doInBackground(String... args) {

            ContentResolver cr = getApplicationContext().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if(cursor.moveToFirst()){

                alContacts = new ArrayList<Local_Contact>();
                do
                {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                        while (pCur.moveToNext())
                        {
                            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String con_number = contactNumber.replaceAll("[^0-9]",""); // also remove +

                            Local_Contact mm1 = new Local_Contact(contactNumber, contactName);

                            alContacts.add(mm1);
                            Log.i("MAYA","all : " + alContacts);
                            break;


                        }
                        pCur.close();
                    }

                } while (cursor.moveToNext()) ;
            }

            return null;
        }

        protected void onPostExecute(String tyu) {

            Log.i("TAG","all789 : " + alContacts);
            contactdb.delete_Contacts();
         boolean success =  contactdb.Add_Contact(alContacts);
        Log.e("TAG", "contact added" + success);
            if(success){
                getFriend_List();

            }

        }

    }

    public void getFriend_List(){

        send_num = contactdb.getAllLocalContact();
        Log.d("check1","clientSocket : " + clientSocket);
        Log.d("789456","client_op : " + send_num);
        JSONArray contacts = new JSONArray(send_num);

        if(contacts.length() > 0){
         //   Log.d("getContact","rt "+ contacts+ "=" +clientSocket.toString());
            mSocket.emit("getContacts" , contacts, clientSocket);
            mSocket.on("gotContacts",contactResponse);
        }
    }


    private  Emitter.Listener contactResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {



                    Log.i("contact", "123" + args[0].toString());

                    try {
                        JSONObject obj = new JSONObject(args[0].toString());
                        boolean success = obj.getBoolean("success");

                        if (success) {

                            contactdb.delete_fambond_contact();

                            JSONArray arr = obj.getJSONArray("data");
                            // mModelList.clear();
                            for (int i = 0; i < arr.length(); i++) {

                                // JSONObject obj1 = arr.getJSONObject(i);
                                Log.w("TAMM", "value are ");
                                contactdb.Add_Fambond_Contact(arr.getJSONObject(i));
                            }
                        }

                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




        }
    };

}
