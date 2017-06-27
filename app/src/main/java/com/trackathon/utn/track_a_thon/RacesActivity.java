package com.trackathon.utn.track_a_thon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.trackathon.utn.track_a_thon.firebase.Firebase;


public class RacesActivity extends AppCompatActivity {

    RecyclerView racesWatchers;
    private RecycleViewRaceAdapter raceAdapter;
    Class<? extends FragmentActivity> nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_races);
        setComponentsBinding();
        nextActivity = getIntent().getExtras().getBoolean(TrackatonConstant.IS_TRACKER)
                ? TrackingActivity.class
                : RaceActivity.class;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.races_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_races_hint));
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                raceAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    private void setComponentsBinding() {
        LinearLayoutManager racesLayout = new LinearLayoutManager(getApplicationContext());

        racesWatchers = (RecyclerView)findViewById(R.id.races);
        racesWatchers.setHasFixedSize(true);
        racesWatchers.setLayoutManager(racesLayout);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.fetching_races));
        dialog.show();

        Firebase.allRaces(races -> {
            RecycleViewRaceAdapter adapter = new RecycleViewRaceAdapter(races, (raceId, race) -> {
                Intent intent = new Intent(RacesActivity.this, nextActivity);
                intent.putExtra(TrackatonConstant.RACE_ID, raceId);
                intent.putExtra(TrackatonConstant.RACE, race);
                startActivity(intent);
            });
            racesWatchers.setAdapter(adapter);
            raceAdapter = adapter;
            dialog.hide();
        });
    }
}
