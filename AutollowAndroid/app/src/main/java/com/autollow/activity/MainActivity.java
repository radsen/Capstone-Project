package com.autollow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.autollow.R;
import com.autollow.fragment.MainFragment;
import com.autollow.fragment.CardFragment;

public class MainActivity extends BaseActivity {

    boolean multipane = false;
    private MainFragment autollowFragment;
    private CardFragment cardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        multipane = getResources().getBoolean(R.bool.multiPane);

        if(!multipane && savedInstanceState == null){
            autollowFragment = new MainFragment();
            attachToActivity(R.id.master, autollowFragment, MainFragment.TAG);
        } else if (!multipane && savedInstanceState != null) {
            autollowFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentByTag(MainFragment.TAG);
        } else if (multipane && savedInstanceState == null) {
            autollowFragment = new MainFragment();
            attachToActivity(R.id.master, autollowFragment, MainFragment.TAG);
            cardFragment = new CardFragment();
            attachToActivity(R.id.detail, cardFragment, CardFragment.TAG);
        } else if (multipane && savedInstanceState != null) {
            autollowFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentByTag(MainFragment.TAG);
            cardFragment = (CardFragment) getSupportFragmentManager()
                    .findFragmentByTag(CardFragment.TAG);
        }

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
}
