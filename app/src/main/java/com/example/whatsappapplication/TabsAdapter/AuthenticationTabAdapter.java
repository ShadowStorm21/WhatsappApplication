package com.example.whatsappapplication.TabsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.whatsappapplication.Fragments.SignInFragment;
import com.example.whatsappapplication.Fragments.SignUpFragment;

public class AuthenticationTabAdapter extends FragmentStateAdapter {


    public AuthenticationTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0 : return new SignInFragment();
            case 1 : return new SignUpFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }




}
