package com.trackathon.utn.track_a_thon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.RaceViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

class RecycleViewRaceAdapter extends RecyclerView.Adapter<RaceViewHolder> {

    private BiConsumer<String, Race> onClick;
    private HashMap<String, Race> races;
    private List<String> keys;

    RecycleViewRaceAdapter(HashMap<String, Race> races, BiConsumer<String, Race> onClick) {
        this.keys = new ArrayList<>(races.keySet());
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
        String raceId = this.keys.get(position);
        Race race = this.races.get(raceId);

        raceViewHolder.getRaceName().setText(race.getName());
        raceViewHolder.getRaceRunnersCount().setText(String.valueOf(race.getRunners().size()));
        raceViewHolder.getRaceWatchersCount().setText(String.valueOf(race.getWatchers().size()));
        raceViewHolder.getRacePhoto().setImageResource(R.drawable.ic_race);

        raceViewHolder.getCardView().setOnClickListener((view) -> this.onClick.accept(raceId, race));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

}
