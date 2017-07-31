package com.autollow.camerareader.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.autollow.camerareader.R;
import com.autollow.camerareader.fragment.CameraReaderFragment;

/**
 * Created by radsen on 7/12/17.
 */

public final class CameraReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.preview_container, new CameraReaderFragment())
                    .commit();
        }

    }
}
