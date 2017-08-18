package com.autollow.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.autollow.R;
import com.autollow.base.BaseActivity;
import com.autollow.vehicle.VehicleFragment;
import com.autollow.profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    public static final String TAG = MainActivity.class.getSimpleName();

    MainPresenterImpl<MainView> mainPresenterImpl;

    @BindView(R.id.dwr_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view_menu)
    NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mainPresenterImpl = new MainPresenter<>();
        mainPresenterImpl.onAttach(this);

        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUp() {

        if (getSupportActionBar()!= null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                0,
                0)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed");
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setUpNavMenu();
    }

    private void setUpNavMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START, true);
                switch (item.getItemId()){
                    case R.id.nav_item_profile:
                        mainPresenterImpl.onProfileClick();
                        return true;
                    case R.id.nav_item_vehicles:
                        mainPresenterImpl.onVehiclesClick();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if(requestCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void showVehicles() {
        attachToActivity(R.id.frame, new VehicleFragment(), VehicleFragment.TAG);
    }

    @Override
    public void showProfile() {
        attachToActivity(R.id.frame, new ProfileFragment(), ProfileFragment.TAG);
    }

    @Override
    public void closeNavMenu(){
        if(mDrawerLayout != null){
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
        }
    }
}
