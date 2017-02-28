package co.jlabs.famb.chatBox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by JLabs on 12/23/16.
 */

public class TabsPagerAdapter  extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new TextFrag();
            case 1:
                // Games fragment activity
                return new TextFrag();
            case 2:
                // Movies fragment activity
                return new TextFrag();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}