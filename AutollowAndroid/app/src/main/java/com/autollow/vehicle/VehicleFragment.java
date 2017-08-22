package com.autollow.vehicle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.R;
import com.autollow.adapter.VehicleAdapter;
import com.autollow.base.BaseFragment;
import com.autollow.common.IConstants;
import com.autollow.fragment.AddVehicleDialogFragment;
import com.autollow.model.Registration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by radsen on 7/10/17.
 */

public class VehicleFragment extends BaseFragment implements IVehicleView,
        AddVehicleDialogFragment.AddVehicleDialogListener, IConstants {

    public static final String TAG = VehicleFragment.class.getSimpleName();

    @BindView(R.id.rv_vehicles)
    RecyclerView rvVehicles;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private Unbinder unbinder;
    private VehicleAdapter mVehicleAdapter;

    List<String> keys;
    private VehiclePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autollow, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        keys = new ArrayList<>();

        mPresenter = new VehiclePresenterImpl(this, new VehicleInteractorImpl(getContext()));
        mVehicleAdapter = new VehicleAdapter(getContext(), mPresenter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvVehicles.setLayoutManager(mLayoutManager);
        rvVehicles.setAdapter(mVehicleAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mPresenter.loadVehicles();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
//        mVehicleAdapter.clear();
//        keys.clear();
//        mVehiclesUserRef.removeEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @OnClick(R.id.fab_add)
//    public void add(){
////        Intent intent = new Intent(getContext(), CameraReaderActivity.class);
////        startActivityForResult(intent, CameraReader.BARCODE_REQUEST);
//
//        showAddVehicle();
//    }

    @Override
    public void onRegistered(Registration registration) {
//        DatabaseReference vehicleRef = mVehiclesUserRef.getRoot()
//                .child(getString(R.string.ref_vehicles_key)).push();
//        vehicleRef.setValue(registration);
    }

    @Override
    public void vehiclesLoaded(List<Registration> registrationList) {
        mVehicleAdapter.setAdapter(registrationList);
    }

    @Override
    public void onVehicleClicked(int position) {
//        Intent intent = new Intent(getContext(), CardActivity.class);
//        intent.putExtra(VEHICLE_ID_KEY, keys.get(position));
//        startActivity(intent);
    }

    @Override
    @OnClick(R.id.fab_add)
    public void showAddVehicle() {
//        FragmentManager fm = getChildFragmentManager();
//        AddVehicleDialogFragment addVehicleDialogFragment = AddVehicleDialogFragment
//                .newInstance(getString(R.string.txt_add_vehicle_title));
//        addVehicleDialogFragment.setAddVehicleListener(this);
//        addVehicleDialogFragment.show(fm, AddVehicleDialogFragment.TAG);
    }
}
