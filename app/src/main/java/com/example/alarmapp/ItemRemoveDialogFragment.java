package com.example.alarmapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


public class ItemRemoveDialogFragment extends DialogFragment {
    private Fragment fragment;
    private DialogFragment dialogFragment;
    private boolean reply;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_remove_dialog, container, false);
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

        if(fragment != null){
            dialogFragment = (DialogFragment) fragment;

            final Button yesButton = (Button) view.findViewById(R.id.button);
            final Button noButton =(Button) view.findViewById(R.id.button2);

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reply = true;
                    dialogFragment.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reply = false;
                    dialogFragment.dismiss();
                }
            });
        }

        return view;
    }

public boolean getReply(){
        return reply;
}

}
