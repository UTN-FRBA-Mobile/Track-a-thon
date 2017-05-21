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
import java.util.function.Consumer;

/**
 * Created by federico on 5/20/17.
 */
public class RecycleViewRaceAdapter extends RecyclerView.Adapter<RecycleViewRaceAdapter.RaceViewHolder> {

    private Consumer<Race> onClick;
    private List<Race> races;

    public RecycleViewRaceAdapter(List<Race> races, Consumer<Race> onClick) {
        this.races = races;
        this.onClick = onClick;
    }

    @Override
    public RaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.race_card, viewGroup, false);
        return new RaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RaceViewHolder raceViewHolder, int position) {
        Race race = races.get(position);

        raceViewHolder.raceName.setText(race.getName());
        raceViewHolder.raceRunnersCount.setText(String.valueOf(race.getRunners().size()));
        raceViewHolder.raceWatchersCount.setText(String.valueOf(race.getWatchers().size()));
        raceViewHolder.racePhoto.setImageResource(R.drawable.ic_race);

        raceViewHolder.cv.setOnClickListener((view) -> this.onClick.accept(race));
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
        TextView raceWatchersCount;
        TextView raceRunnersCount;
        ImageView racePhoto;

        RaceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            raceName = (TextView) itemView.findViewById(R.id.race_name);
            raceWatchersCount = (TextView) itemView.findViewById(R.id.race_watchers);
            raceRunnersCount = (TextView) itemView.findViewById(R.id.race_runners);
            racePhoto = (ImageView) itemView.findViewById(R.id.race_photo);
        }
    }

}
