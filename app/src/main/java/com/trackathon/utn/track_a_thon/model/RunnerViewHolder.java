package com.trackathon.utn.track_a_thon.model;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackathon.utn.track_a_thon.R;

public class RunnerViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private TextView runnerName;
    private TextView runnerSpeed;
    private ImageView runnerPhoto;
    private TextView runnerMaxSpeed;
    private TextView runnerAccumulatedDistance;

    public RunnerViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cv);
        runnerName = (TextView) itemView.findViewById(R.id.runner_name);
        runnerSpeed = (TextView) itemView.findViewById(R.id.runner_speed);
        runnerMaxSpeed = (TextView) itemView.findViewById(R.id.runner_max_speed);
        runnerAccumulatedDistance = (TextView) itemView.findViewById(R.id.runner_accumulated_distance);
        runnerPhoto = (ImageView) itemView.findViewById(R.id.runner_photo);
    }

    public CardView getCardView() {
        return cardView;
    }

    public TextView getRunnerName() {
        return runnerName;
    }

    public TextView getRunnerSpeed() {
        return runnerSpeed;
    }

    public ImageView getRunnerPhoto() {
        return runnerPhoto;
    }

    public TextView getRunnerMaxSpeed() {
        return runnerMaxSpeed;
    }

    public TextView getRunnerAccumulatedDistance() {
        return runnerAccumulatedDistance;
    }
}

