package com.example.madgenius;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GreenButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Implements the logic behind the green button fragment.
 */
public class GreenButtonFragment extends Fragment {

    private Button button;
    private OnFragmentInteractionListener mListener;

    public GreenButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_green_button, container, false);
        button = rootView.findViewById(R.id.greenButton);
        button.setOnClickListener(view -> mListener.onGreenButtonClick());
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        void onGreenButtonClick();
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

    public static GreenButtonFragment newInstance() { return new GreenButtonFragment(); }

}
