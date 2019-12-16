package com.example.madgenius;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlueButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Implements the logic behind the blue button fragment.
 */
public class BlueButtonFragment extends Fragment {

    private Button button;
    private OnFragmentInteractionListener mListener;

    public BlueButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_blue_button, container, false);
        button = rootView.findViewById(R.id.blueButton);
        button.setOnClickListener(view -> mListener.onBlueButtonClick());
        return rootView;
    }

   interface OnFragmentInteractionListener {
        void onBlueButtonClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.exception_message));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static BlueButtonFragment newInstance() { return new BlueButtonFragment(); }
}
