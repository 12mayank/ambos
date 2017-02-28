package co.jlabs.famb.chatBox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.jlabs.famb.R;
import co.jlabs.famb.ShareInf;
import co.jlabs.famb.fonts.ButtonFont;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by JLabs on 12/23/16.
 */

public class TextFrag extends Fragment implements  View.OnClickListener {

    private EmojiconEditText et_message;
    private ButtonFont send;
    ShareAdap shareAdap;
    private EmojIconActions emojIcon;
    private View contentRoot;
    private ImageView imgs;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        contentRoot = rootView.findViewById(R.id.contentRoot);
        et_message = (EmojiconEditText) rootView.findViewById(R.id.et_message);
        imgs = (ImageView) rootView.findViewById(R.id.imgs);
        send = (ButtonFont) rootView.findViewById(R.id.send);
        et_message.addTextChangedListener(edited);
        emojIcon = new EmojIconActions(getContext(),contentRoot,et_message,imgs);
        emojIcon.ShowEmojIcon();
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                submit();
                break;
        }
    }


    private void submit() {
        // validate
        String message = et_message.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Message Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        passData(message);
        et_message.setText("");

        // TODO validate success, do something


    }


    TextWatcher edited=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count>0){
                send.setText(getString(R.string.send));
            }else  send.setText(getString(R.string.mic));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        shareAdap = (ShareAdap) a;
    }

    public void passData(String data) {
        shareAdap.onMethodCallback(data);
    }


}