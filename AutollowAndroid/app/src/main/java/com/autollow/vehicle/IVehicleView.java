package com.autollow.vehicle;

import com.autollow.model.Registration;

import java.util.List;
import java.util.Map;

/**
 * Created by radsen on 8/22/17.
 */

public interface IVehicleView {

    void vehiclesLoaded(Map<String, Registration> registrationList);

    void onVehicleClicked(int position);

    void showAddVehicle();

}
