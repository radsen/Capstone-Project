package com.autollow.vehicle;

import com.autollow.model.Registration;

import java.util.List;

/**
 * Created by radsen on 8/22/17.
 */

public interface IVehicleView {

    void vehiclesLoaded(List<Registration> registrationList);

    void onVehicleClicked(int position);

    void showAddVehicle();

}
