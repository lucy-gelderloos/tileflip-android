package com.gelderloos.tileflip.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gelderloos.tileflip.MainActivity;
import com.gelderloos.tileflip.R;

public class WinGameDialogFragment extends DialogFragment {
    private int score;
    private Context context;
    SharedPreferences preferences;
    private boolean newHighScore = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View congratsView = inflater.inflate(R.layout.win_game_layout,null);
        TextView scoreLabel = congratsView.findViewById(R.id.textViewScoreLabel);
        if(newHighScore) {
            scoreLabel.setText(R.string.newHighScoreLabel);
        } else scoreLabel.setText(R.string.finalScoreLabel);
        TextView finalScore = congratsView.findViewById(R.id.textViewFinalScore);
        finalScore.setText(getString(R.string.finalScore,score));
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
                        ((MainActivity) requireActivity()).startNewGame();
                    }
                });
        return builder.create();
    }

    public WinGameDialogFragment() {
        // Required empty public constructor
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

    public boolean isNewHighScore() {
        return newHighScore;
    }

    public void setNewHighScore(boolean newHighScore) {
        this.newHighScore = newHighScore;
    }
}