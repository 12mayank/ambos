package co.jlabs.famb;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.jlabs.famb.Rounded.CircularImageView;
import co.jlabs.famb.chatBox.ChatMessage;
import co.jlabs.famb.chatBox.ChatMessageAdapter;
import co.jlabs.famb.chatBox.GalleryFrag;
import co.jlabs.famb.chatBox.NonSwipeableViewPager;
import co.jlabs.famb.chatBox.ShareAdap;
import co.jlabs.famb.chatBox.TextFrag;
import co.jlabs.famb.chatBox.VoiceFrag;
import co.jlabs.famb.fonts.TextView_White;

public class ChatBox extends FragmentActivity implements ShareAdap {

    private NonSwipeableViewPager viewPager;
    private TabLayout tabLayout;
    private TextView_White back;
    private CircularImageView bond_img;
    private TextView bond_name;
    private TextView members;
    private TextView_White settings;
    private TextView_White attachment;
    private TextView_White call;
    private RelativeLayout action_bar;
    private ListView listView;
    private NonSwipeableViewPager viewpager;
    private RelativeLayout send_message_layout;
    private RelativeLayout content_chat_box;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        initView();
    }

    private void setupTabIcons() {

        TextView_White tabOne = (TextView_White) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.text_inp));
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView_White tabTwo = (TextView_White) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.gallery));

        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView_White tabThree = (TextView_White) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.camera));
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView_White tabFour = (TextView_White) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(getString(R.string.gif));
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView_White tabFive = (TextView_White) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText(getString(R.string.mic));
        tabLayout.getTabAt(4).setCustomView(tabFive);



    }

    private void setupViewPager(NonSwipeableViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TextFrag(), getString(R.string.send));
        adapter.addFragment(new GalleryFrag(), getString(R.string.send));
        adapter.addFragment(new TextFrag(), getString(R.string.send));
        adapter.addFragment(new TextFrag(), getString(R.string.send));
        adapter.addFragment(new VoiceFrag(), getString(R.string.send));

        viewPager.setAdapter(adapter);
    }

    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        back = (TextView_White) findViewById(R.id.back);
        bond_img = (CircularImageView) findViewById(R.id.bond_img);
        bond_name = (TextView) findViewById(R.id.bond_name);
        members = (TextView) findViewById(R.id.members);
        settings = (TextView_White) findViewById(R.id.settings);
        attachment = (TextView_White) findViewById(R.id.attachment);
        call = (TextView_White) findViewById(R.id.call);
        action_bar = (RelativeLayout) findViewById(R.id.action_bar);
        listView = (ListView) findViewById(R.id.listView);
        viewpager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        send_message_layout = (RelativeLayout) findViewById(R.id.send_message_layout);
        content_chat_box = (RelativeLayout) findViewById(R.id.content_chat_box);

        //mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView.setAdapter(mAdapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
    }
    @Override
    public void onMethodCallback(String data) {
        Log.d("LOG","hello " + data);
       // sendMessage(data);
    }
    @Override
    public void onMethodCallbacks(Bitmap data) {
        Log.d("LOG","hello " + data);
        //sendMessage(data);
    }


//    private void sendMessage(String message) {
//        ChatMessage chatMessage = new ChatMessage(message, true, true,false);
//        mAdapter.add(chatMessage);
//
//        mimicOtherMessage(message);
//    }
//    private void sendMessage(Bitmap message) {
//        ChatMessage chatMessage = new ChatMessage(message, true, true,false);
//        mAdapter.add(chatMessage);
//        mimicOtherMessage(message);
//    }

//    private void mimicOtherMessage(Bitmap message) {
//        ChatMessage chatMessage = new ChatMessage(message, false, true,false);
//        mAdapter.add(chatMessage);
//    }
//    private void mimicOtherMessage(String message) {
//        ChatMessage chatMessage = new ChatMessage(message, false, true,false);
//        mAdapter.add(chatMessage);
//    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private int mCurrentPosition = -1;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }



        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (position != mCurrentPosition) {
                Fragment fragment = (Fragment) object;
                NonSwipeableViewPager pager = (NonSwipeableViewPager) container;
                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());
                }
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
