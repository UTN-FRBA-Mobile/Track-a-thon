package com.trackathon.utn.track_a_thon.model;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackathon.utn.track_a_thon.R;

public class RaceViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private TextView raceName;
    private TextView raceWatchersCount;
    private TextView raceRunnersCount;
    private ImageView racePhoto;

    public RaceViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cv);
        raceName = (TextView) itemView.findViewById(R.id.race_name);
        raceWatchersCount = (TextView) itemView.findViewById(R.id.race_watchers);
        raceRunnersCount = (TextView) itemView.findViewById(R.id.race_runners);
        racePhoto = (ImageView) itemView.findViewById(R.id.race_photo);
    }

    public CardView getCardView() {
        return cardView;
    }

    public TextView getRaceName() {
        return raceName;
    }

    public TextView getRaceWatchersCount() {
        return raceWatchersCount;
    }

    public TextView getRaceRunnersCount() {
        return raceRunnersCount;
    }

    public ImageView getRacePhoto() {
        return racePhoto;
    }
}

