package com.augustosalazar.androidsensors;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Locale;


public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.d("AndroidSensors", "getItem " + position);
        if (position == 0) {
            return Frag1.newInstance(position + 1);
        } else if (position == 1) {
            return Frag2.newInstance(position + 1);
        } else {
            return Frag3.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        Resources res = Resources.getSystem();
        switch (position) {
            case 0:
                return res.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return res.getString(R.string.title_section2).toUpperCase(l);
            case 3:
                return res.getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
}
