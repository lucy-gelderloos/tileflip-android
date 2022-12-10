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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

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

        KonfettiView konfettiView = congratsView.findViewById(R.id.winGameKonfettiView);
        EmitterConfig emitterConfig = new Emitter(3L, TimeUnit.SECONDS).perSecond(50);
        Party winParty = new PartyFactory(emitterConfig)
                .spread(360)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                .colors(Arrays.asList(0xfc0606,0xb300ff,0x073cf8,0x088A08,0xFDF000,0xff8e00))
                .setSpeedBetween(0f, 30f)
                .position(new Position.Relative(0.5, 0.1))
                .build();
        TextView scoreLabel = congratsView.findViewById(R.id.textViewScoreLabel);
        if(newHighScore) {
            scoreLabel.setText(R.string.newHighScoreLabel);
        } else scoreLabel.setText(R.string.finalScoreLabel);
        konfettiView.start(winParty);
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