package com.gelderloos.tileflip.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gelderloos.tileflip.MainActivity;
import com.gelderloos.tileflip.R;

import java.util.Objects;

public class WinGameDialogFragment extends DialogFragment {
    private int tries;
    private int score;
    private Context context;
    SharedPreferences preferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View congratsView = inflater.inflate(R.layout.win_game_layout,null);

        TextView congratsTextView = congratsView.findViewById(R.id.textViewWinGameDialogTries);
        congratsTextView.setText(getString(R.string.dialog_congrats,tries));
        int easyHighScore = preferences.getInt("HIGH_SCORE_EASY_TAG",0);
        TextView easyScoreTextView = congratsView.findViewById(R.id.highScoreEasy);
        easyScoreTextView.setText(getString(R.string.highScoreEasy,easyHighScore));
        int mediumHighScore = preferences.getInt("HIGH_SCORE_MEDIUM_TAG",0);
        TextView mediumScoreTextView = congratsView.findViewById(R.id.highScoreMedium);
        mediumScoreTextView.setText(getString(R.string.highScoreMedium,mediumHighScore));
        int hardHighScore = preferences.getInt("HIGH_SCORE_HARD_TAG",0);
        TextView highScoreTextView = congratsView.findViewById(R.id.highScoreHard);
        highScoreTextView.setText(getString(R.string.highScoreHard,hardHighScore));

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}