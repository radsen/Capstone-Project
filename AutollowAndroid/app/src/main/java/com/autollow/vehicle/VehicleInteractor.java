package com.autollow.vehicle;

import com.autollow.model.Registration;

import java.util.List;

/**
 * Created by radsen on 8/22/17.
 */

interface VehicleInteractor {

    void loadVehicles(LoadListener listener);

    public interface LoadListener {
        void onLoaded(List<Registration> mCurrentVehicleList);
    }
}
