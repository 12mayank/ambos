package co.jlabs.famb.chatBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import co.jlabs.famb.ChatBox;
import co.jlabs.famb.R;
import co.jlabs.famb.activityArea.Utility;

import static android.app.Activity.RESULT_OK;

/**
 * Created by JLabs on 12/23/16.
 */

public class GalleryFrag extends Fragment {
    private int  SELECT_FILE = 0;
    ShareAdap shareAdap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        boolean result=Utility.checkPermission(getContext());
        if(result){
            galleryIntent();}
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void galleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        //intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
        getActivity().startActivityForResult(intent,SELECT_FILE);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        passData(bm);
        //ivImage.setImageBitmap(bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_FILE) {
            ChatBox activity = (ChatBox)getActivity();
            Bitmap bitmap = getBitmapFromCameraData(data, activity);
            passData(bitmap);
                //onSelectFromGalleryResult(data);

        }
    }
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        shareAdap = (ShareAdap) a;
    }

    public void passData(Bitmap data) {
        shareAdap.onMethodCallbacks(data);
    }
    public static Bitmap getBitmapFromCameraData(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }

}