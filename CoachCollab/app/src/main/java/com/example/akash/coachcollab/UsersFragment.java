package com.example.akash.coachcollab;

import android.app.FragmentManager;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UsersFragment extends Fragment {


    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        MainActivity.toolbar.setTitle("Students");


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new AllStudentsFragment();
                    case 1:
                        return new MyStudentsFragment();
                    default:
                        return new AllStudentsFragment();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "All Students";
                    case 1:
                        return "My Students";
                    default:
                        return "All Students";
                }
            }
        });

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

}
