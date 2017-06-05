package com.trackathon.utn.track_a_thon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.RaceViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

class RecycleViewRaceAdapter extends RecyclerView.Adapter<RaceViewHolder> implements Filterable {

    private BiConsumer<String, Race> onClick;
    private HashMap<String, Race> races;
    private HashMap<String, Race> filteredRaces;
    private List<String> keys;
    private List<String> filteredKeys;

    RecycleViewRaceAdapter(HashMap<String, Race> races, BiConsumer<String, Race> onClick) {
        this.keys = new ArrayList<>(races.keySet());
        this.filteredKeys = new ArrayList<>(races.keySet());
        this.races = races;
        this.filteredRaces = races;
        this.onClick = onClick;
    }

    @Override
    public RaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.race_card, viewGroup, false);
        return new RaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RaceViewHolder raceViewHolder, int position) {
        String raceId = this.filteredKeys.get(position);
        Race race = this.filteredRaces.get(raceId);

        raceViewHolder.getRaceName().setText(race.getName());
        raceViewHolder.getRaceLocation().setText(race.getLocation());
        raceViewHolder.getRaceStartTime().setText(race.getStartTime());
        raceViewHolder.getRaceDistance().setText(race.getDistance());
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
        return filteredRaces.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    filteredKeys = keys;
                    filteredRaces = races;
                } else {
                    List<String> auxKeys = new ArrayList<>();
                    HashMap<String, Race> auxFilteredRaces = new HashMap<>();

                    for (String key : keys) {
                        Race race = races.get(key);
                        if (race.getName().toLowerCase().contains(charString)) {
                            auxKeys.add(key);
                            auxFilteredRaces.put(key, race);
                        }
                    }
                    filteredKeys = auxKeys;
                    filteredRaces = auxFilteredRaces;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredRaces;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredRaces = (HashMap<String, Race>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
