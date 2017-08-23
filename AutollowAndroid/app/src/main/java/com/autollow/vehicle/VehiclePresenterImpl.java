package com.autollow.vehicle;

import android.util.Log;

import com.autollow.model.Registration;

import java.util.Map;

/**
 * Created by radsen on 8/22/17.
 */

public class VehiclePresenterImpl implements VehiclePresenter, VehicleInteractor.LoadListener {

    public static final String TAG = VehiclePresenterImpl.class.getSimpleName();

    private final VehicleInteractor vehicleInteractor;
    private final IVehicleView vehicleView;

    public VehiclePresenterImpl(IVehicleView view, VehicleInteractor interactor){
        vehicleView = view;
        vehicleInteractor = interactor;
    }

    @Override
    public void onVehicleClicked(int position) {
        Log.d(TAG, "onVehicleClicked");
        vehicleView.onVehicleClicked(position);
    }

    @Override
    public void loadVehicles() {
        vehicleInteractor.loadVehicles(this);
    }

    @Override
    public void onLoaded(Map<String, Registration> mCurrentVehicleList) {
        if(vehicleView != null){
            vehicleView.vehiclesLoaded(mCurrentVehicleList);
        }
    }
}
