package com.example.madgenius;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwitchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SwitchFragment extends Fragment {

    private Switch mSwitch;
    private OnFragmentInteractionListener mListener;

    public SwitchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_switch, container, false);
        mSwitch = rootView.findViewById(R.id.mSwitch);
        mSwitch.setOnCheckedChangeListener((view, isChecked) -> mListener.onSwitch(isChecked));
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        void onSwitch(Boolean val);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static SwitchFragment newInstance() { return new SwitchFragment(); }
}
