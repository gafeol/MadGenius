package com.example.madgenius;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RedButtonFragment extends Fragment {

    private Button button;


    public RedButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_red_button, container, false);
        button = rootView.findViewById(R.id.redButton);
        return rootView;
    }

    public static RedButtonFragment newInstance() {
        return new RedButtonFragment();
    }

}
