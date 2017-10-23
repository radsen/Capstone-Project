package com.autollow.vehicle;

/**
 * Created by radsen on 8/22/17.
 */

public interface VehiclePresenter {

    void onVehicleClicked(int position);

    void loadVehicles();

    void onEditVehicle(int position);
}
