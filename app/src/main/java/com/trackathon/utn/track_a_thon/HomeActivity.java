package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.trackathon.utn.track_a_thon.model.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        User user = User.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.navigation_drawer_name)).setText(user.getName());
        ((TextView)headerView.findViewById(R.id.navigation_drawer_email)).setText(user.getEmail());
        ImageView imageView = (ImageView) headerView.findViewById(R.id.navigation_drawer_avatar);
        Picasso.with(headerView.getContext()).load(user.getImageUrl()).into(imageView);

        createElements();
        createListeners();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_watchers) {
            openActivity(false);
        } else if (id == R.id.nav_trackers) {
            openActivity(true);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        User.setCurrentUser(null);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private Button btnWatchers;
    private Button btnTrackers;

    private void createElements() {
        btnWatchers = (Button) findViewById(R.id.btnWatchers);
        btnTrackers = (Button) findViewById(R.id.btnTrackers);
    }

    private void createListeners() {
        btnTrackers.setOnClickListener((View v) -> openActivity(true));
        btnWatchers.setOnClickListener((View v) -> openActivity(false));
    }

    private void openActivity (Boolean isTracker) {
        Intent intent = new Intent(HomeActivity.this, RacesActivity.class);
        intent.putExtra(TrackatonConstant.IS_TRACKER, isTracker);
        startActivity(intent);
    }
}
