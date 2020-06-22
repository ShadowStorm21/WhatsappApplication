package com.example.whatsappapplication.TabsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsappapplication.Fragments.SignInFragment;
import com.example.whatsappapplication.Fragments.SignUpFragment;

public class AuthenticationTabAdapter extends FragmentPagerAdapter {

    public AuthenticationTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 : return new SignInFragment();
            case 1 : return new SignUpFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0 : return "Sign in";
            case 1 : return "Sign up";
            default: return null;
        }
    }
}
