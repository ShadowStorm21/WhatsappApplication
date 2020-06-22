package com.example.whatsappapplication.TabsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappapplication.Fragments.CallsFragment;
import com.example.whatsappapplication.Fragments.ChatFragment;
import com.example.whatsappapplication.Fragments.StatusFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0 : return new ChatFragment();
            case 1 : return new StatusFragment();
            case 2 : return new CallsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0 : return "Chats";
            case 1 : return "Status";
            case 2 : return "Calls";
            default: return null;
        }
    }
}
