package com.example.hd.termproject;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeekFragment extends Fragment{
    View fragmentView;
    Context fragmentContext;
    Bundle fragmentBundle;

    public WeekFragment() { };

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_month, container, false);
        fragmentContext = container.getContext();
        fragmentBundle = new Bundle();
        fragmentBundle = getArguments();

        return fragmentView;
    }
}
