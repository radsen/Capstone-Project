package com.autollow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.common.IConstants;
import com.autollow.model.Registration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by radsen on 8/9/17.
 */

public class VehicleInfoFragment extends BaseFragment implements IConstants, ValueEventListener {

    public static final String TAG = VehicleInfoFragment.class.getSimpleName();

    private Unbinder unbinder;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mVehiclesRef;

    @BindView(R.id.tv_brand)
    TextView brandTextView;

    @BindView(R.id.tv_model)
    TextView modelTextView;

    @BindView(R.id.tv_reg_date)
    TextView registrationDateTextView;

    @BindView(R.id.tv_license_plate)
    TextView licensePlateTextView;

    @BindView(R.id.spr_fuel_type)
    Spinner fuelTypeSpinner;

    @BindView(R.id.spr_service_type)
    Spinner serviceTypeSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        String vehicleId = getArguments().getString(VEHICLE_ID_KEY);
        Uri vehiclesUri = new Uri.Builder()
                .appendPath(getString(R.string.ref_vehicles_key))
                .appendPath(vehicleId)
                .build();
        mVehiclesRef = mFirebaseDatabase.getReference().child(vehiclesUri.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mVehiclesRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVehiclesRef.removeEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static VehicleInfoFragment newInstance(Bundle bundle) {
        VehicleInfoFragment vehicleInfoFragment = new VehicleInfoFragment();
        vehicleInfoFragment.setArguments(bundle);
        return vehicleInfoFragment;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Registration registration = dataSnapshot.getValue(Registration.class);

        brandTextView.setText(registration.getBrand());
        modelTextView.setText(registration.getModel());
        licensePlateTextView.setText(registration.getLicensePlate());
        registrationDateTextView.setText(registration.getRegistrationDate());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
