package com.trackathon.utn.track_a_thon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.Runner;
import com.trackathon.utn.track_a_thon.model.RunnerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

class RecycleViewRunnerAdapter extends RecyclerView.Adapter<RunnerViewHolder> {

    private BiConsumer<String, Runner> onClick;
    private HashMap<String, Runner> races;
    private List<String> keys;

    RecycleViewRunnerAdapter(HashMap<String, Runner> runners, BiConsumer<String, Runner> onClick) {
        this.keys = new ArrayList<>(runners.keySet());
        this.races = runners;
        this.onClick = onClick;
    }

    @Override
    public RunnerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.runner_card, viewGroup, false);
        return new RunnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RunnerViewHolder runnerViewHolder, int position) {
        String runnerId = this.keys.get(position);
        Runner runner = this.races.get(runnerId);

        runnerViewHolder.getRunnerName().setText(runner.getName());
        runnerViewHolder.getRunnerPhoto().setImageResource(R.drawable.ic_race);

        runnerViewHolder.getCardView().setOnClickListener((view) -> this.onClick.accept(runnerId, runner));
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
