package co.jlabs.famb.chatBox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import co.jlabs.famb.R;
import co.jlabs.famb.fonts.ButtonFont;

/**
 * Created by JLabs on 12/23/16.
 */

public class VoiceFrag extends Fragment implements View.OnClickListener {

    private ButtonFont voice_btn;
    private LinearLayout contentRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_voice_rec, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        voice_btn = (ButtonFont) rootView.findViewById(R.id.voice_btn);
        contentRoot = (LinearLayout) rootView.findViewById(R.id.contentRoot);

        voice_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_btn:

                break;
        }
    }
}