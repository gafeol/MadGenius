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
 */
public class RedButtonFragment extends Fragment {

    private Button button;
    OnFragmentInteractionListener mListener;

    public RedButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_red_button, container, false);
        button = rootView.findViewById(R.id.redButton);
        // When button is clicked, listener activates
        button.setOnClickListener(view -> mListener.onRedButtonClick());
        return rootView;
    }

    interface OnFragmentInteractionListener {
        void onRedButtonClick();
    }

    /**
     * Runs as soon as the fragment is attached to the main activity.
     * This function makes sure the main activity has implemented the listener function.
     * @param context
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                + getResources().getString(R.string.exception_message));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static RedButtonFragment newInstance() {
        return new RedButtonFragment();
    }

}
