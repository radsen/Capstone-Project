package com.autollow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.autollow.R;
import com.autollow.fragment.VehicleInfoFragment;

/**
 * Created by radsen on 8/9/17.
 */

public class VehicleInfoActivity extends BaseActivity {

    private VehicleInfoFragment vehicleInfoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            vehicleInfoFragment = VehicleInfoFragment.newInstance(bundle);
            attachToActivity(R.id.master, vehicleInfoFragment, VehicleInfoFragment.TAG);
        }
    }

}
