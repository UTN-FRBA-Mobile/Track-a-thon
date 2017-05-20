package com.trackathon.utn.track_a_thon;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackathon.utn.track_a_thon.model.Race;

import java.util.List;

/**
 * Created by federico on 5/20/17.
 */
public class RecycleViewRaceAdapter extends RecyclerView.Adapter<RecycleViewRaceAdapter.RaceViewHolder> {

    List<Race> races;

    public RecycleViewRaceAdapter(List<Race> races) {
        this.races = races;
    }

    @Override
    public RaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.race_card, viewGroup, false);
        return new RaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RaceViewHolder raceViewHolder, int position) {
        raceViewHolder.raceName.setText(races.get(position).getName());
        raceViewHolder.raceWatchers.setText(races.get(position).getWatchers().toString());
        raceViewHolder.raceRunners.setText(races.get(position).getRunners().toString());
        raceViewHolder.racePhoto.setImageResource(R.mipmap.ic_race_logo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class RaceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView raceName;
        TextView raceWatchers;
        TextView raceRunners;
        ImageView racePhoto;

        RaceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            raceName = (TextView) itemView.findViewById(R.id.race_name);
            raceWatchers = (TextView) itemView.findViewById(R.id.race_watchers);
            raceRunners = (TextView) itemView.findViewById(R.id.race_runners);
            racePhoto = (ImageView) itemView.findViewById(R.id.race_photo);
        }
    }

}
