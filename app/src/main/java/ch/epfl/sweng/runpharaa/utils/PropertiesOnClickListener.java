package ch.epfl.sweng.runpharaa.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.runpharaa.R;

public class PropertiesOnClickListener implements View.OnClickListener {

    private final int DEFAULT_TEMPO = 133;
    private final Activity activity;
    private final boolean hasDefaultTime;
    private Callback<PropertiesOnClickListener> callback;

    private boolean propertiesSet;
    private double time;
    private int difficulty = 3;
    private double totalDistance;

    public PropertiesOnClickListener(Activity activity, boolean hasDefaultTime) {
        this.activity = activity;
        this.propertiesSet = false;
        this.hasDefaultTime = hasDefaultTime;
        totalDistance = 0.0;
        callback = null;
    }

    public PropertiesOnClickListener(Activity activity, Callback<PropertiesOnClickListener> callback) {
        this(activity, false);
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        final View mView = activity.getLayoutInflater().inflate(R.layout.dialog_properties, null);

        EditText mTime = mView.findViewById(R.id.time);

        TextView mDiffText = mView.findViewById(R.id.diff_text);
        mDiffText.setText(activity.getResources().getString(R.string.difficulty_is) + difficulty);

        SeekBar mSeekBar = mView.findViewById(R.id.difficulty_bar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficulty = progress;
                mDiffText.setText(activity.getResources().getString(R.string.difficulty_is) + difficulty);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(activity.getResources().getText(R.string.OK), (dialog, which) -> {

            if (!mTime.getText().toString().isEmpty()) {
                time = Double.parseDouble(mTime.getText().toString());
                propertiesSet = true;
            } else if (hasDefaultTime) {
                Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.default_time), Toast.LENGTH_SHORT).show();
                time = totalDistance / DEFAULT_TEMPO;
                propertiesSet = true;
            } else {
                propertiesSet = false;
            }

            if (callback != null) {
                callback.onSuccess(this);
            }

        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public boolean isPropertiesSet() {
        return propertiesSet;
    }

    public double getTime() {
        return time;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
