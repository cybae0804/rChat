package com.example.james.rchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch(position) {
            case 0:
                return "GROUPS";

            case 1:
                return "CHATS";

            default:
                return null;
        }

    }

}
