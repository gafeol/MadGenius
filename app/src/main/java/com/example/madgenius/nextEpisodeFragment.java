package com.example.madgenius;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class nextEpisodeFragment extends DialogFragment {
    private int numActions;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_next_episode, null);
        builder.setView(view);
        TextView txt = view.findViewById(R.id.txtActionsNumber);
        txt.setText(txt.getText() +" " +  numActions + " actions.");

        TextView btn = view.findViewById(R.id.txtDismiss);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        return  builder.create();
    }

    /**
    * Sets number of actions in order to display it correctly on the dialog.
    */

    public void setNumActions(int numActions){
        this.numActions = numActions;
    }
}
