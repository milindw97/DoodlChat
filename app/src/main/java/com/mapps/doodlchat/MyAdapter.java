package com.mapps.doodlchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            if (position == 0) {
                fragment = new ChatsFragment();
            }
            else if (position == 1){
                fragment = new DoodleFragment();
            }
            else{
                fragment = new FriendsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
}

