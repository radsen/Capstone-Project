package com.autollow.vehicle;

import com.autollow.model.Registration;

import java.util.Map;

/**
 * Created by radsen on 8/22/17.
 */

interface VehicleInteractor {

    void loadVehicles(LoadListener listener);

    public interface LoadListener {
        void onLoaded(Map<String, Registration> mCurrentVehicleList);
    }
}
