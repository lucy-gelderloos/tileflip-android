package com.gelderloos.tileflip.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gelderloos.tileflip.MainActivity;
import com.gelderloos.tileflip.R;

public class WinGameDialogFragment extends DialogFragment {
    private int tries;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View congratsView = inflater.inflate(R.layout.win_game_layout,null);
        TextView congratsTextView = congratsView.findViewById(R.id.textViewWinGameDialogTries);
        congratsTextView.setText(getString(R.string.dialog_congrats,tries));

        builder
                .setView(congratsView)
                .setPositiveButton(R.string.dialog_play_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    public WinGameDialogFragment() {
        // Required empty public constructor
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}