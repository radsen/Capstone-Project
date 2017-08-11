package com.autollow.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.adapter.SpinnerAdapter;
import com.autollow.common.IConstants;
import com.autollow.model.Registration;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by radsen on 8/9/17.
 */

public class VehicleInfoFragment extends BaseFragment implements IConstants, ValueEventListener,
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = VehicleInfoFragment.class.getSimpleName();

    private Unbinder unbinder;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mVehiclesRef;

    @BindView(R.id.imageView)
    ImageView pictureImageView;

    @BindView(R.id.tv_brand)
    TextView brandTextView;

    @BindView(R.id.tv_model)
    TextView modelTextView;

    @BindView(R.id.tv_reg_date)
    TextView registrationDateTextView;

    @BindView(R.id.tv_license_plate)
    TextView licensePlateTextView;

    @BindView(R.id.spr_vehicle_type)
    Spinner vehicleTypeSpinner;

    @BindView(R.id.spr_fuel_type)
    Spinner fuelTypeSpinner;

    @BindView(R.id.spr_service_type)
    Spinner serviceTypeSpinner;

    @BindView(R.id.imagePlaceHolder)
    RelativeLayout pictureButton;

    private SpinnerAdapter vehicleTypeAdapter;
    private SpinnerAdapter fuelTypeAdapter;
    private SpinnerAdapter serviceTypeAdapter;
    private Registration registration;

    Map<String, Object> childUpdates = new HashMap<>();

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

        pictureButton.setOnClickListener(this);

        String[] vehicleTypes = getResources().getStringArray(R.array.vehicle_type_array);
        vehicleTypeAdapter = new SpinnerAdapter(getContext(),
                R.layout.item_spinner, vehicleTypes);
        vehicleTypeSpinner.setAdapter(vehicleTypeAdapter);
        vehicleTypeSpinner.setOnItemSelectedListener(this);

        String[] fuelTypes = getResources().getStringArray(R.array.fuel_type_array);
        fuelTypeAdapter = new SpinnerAdapter(getContext(),
                R.layout.item_spinner, fuelTypes);
        fuelTypeSpinner.setAdapter(fuelTypeAdapter);
        fuelTypeSpinner.setOnItemSelectedListener(this);

        String[] serviceTypes = getResources().getStringArray(R.array.service_type_array);
        serviceTypeAdapter = new SpinnerAdapter(getContext(),
                R.layout.item_spinner, serviceTypes);
        serviceTypeSpinner.setAdapter(serviceTypeAdapter);
        serviceTypeSpinner.setOnItemSelectedListener(this);

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
        registration = dataSnapshot.getValue(Registration.class);

        brandTextView.setText(registration.getBrand());
        modelTextView.setText(registration.getModel());
        licensePlateTextView.setText(registration.getLicensePlate());
        registrationDateTextView.setText(registration.getRegistrationDate());
        vehicleTypeSpinner.setSelection(registration.getVehicleType());
        fuelTypeSpinner.setSelection(registration.getFuelType());
        serviceTypeSpinner.setSelection(registration.getServiceType());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(registration == null){
            return;
        }

        switch (parent.getId()){
            case R.id.spr_vehicle_type:
                childUpdates.put("/vehicle_type", position);
                break;
            case R.id.spr_fuel_type:
                childUpdates.put("/fuel_type", position);
                break;
            case R.id.spr_service_type:
                childUpdates.put("/service_type", position);
                break;
        }

        mVehiclesRef.updateChildren(childUpdates);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagePlaceHolder:
                Intent intent =ImagePicker.create(getActivity())
                        .returnAfterFirst(true)
                        .single()
                        .getIntent(getContext());
                startActivityForResult(intent, IMAGE_PICKER_INTENT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_INTENT && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).getPath());
            pictureImageView.setImageBitmap(bitmap);
        }
    }
}
