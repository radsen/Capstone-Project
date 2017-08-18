package com.autollow.vehicle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.R;
import com.autollow.activity.CardActivity;
import com.autollow.activity.VehicleInfoActivity;
import com.autollow.adapter.VehicleAdapter;
import com.autollow.base.BaseFragment;
import com.autollow.common.IConstants;
import com.autollow.fragment.AddVehicleDialogFragment;
import com.autollow.model.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by radsen on 7/10/17.
 */

public class VehicleFragment extends BaseFragment implements ChildEventListener,
        AddVehicleDialogFragment.AddVehicleDialogListener, VehicleAdapter.VehicleClickListener,
        IConstants {

    public static final String TAG = VehicleFragment.class.getSimpleName();

    @BindView(R.id.rv_vehicles)
    RecyclerView rvVehicles;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private Unbinder unbinder;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mVehiclesUserRef;
    private VehicleAdapter mVehicleAdapter;
    private FirebaseAuth mFirebaseAuth;
    private String UUId;

    List<String> keys;

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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mFirebaseDatabase.setPersistenceEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null){
            UUId = mFirebaseAuth.getCurrentUser().getUid();
        }

        Log.d(TAG, "onActivityCreated - UUID: " + UUId);

        Uri uri = new Uri.Builder()
                .appendPath(getString(R.string.ref_vehicles_user_key))
                .appendPath(UUId).build();

        mVehiclesUserRef = mFirebaseDatabase.getReference()
                .child(uri.toString());

        keys = new ArrayList<>();

        List<Registration> list = new ArrayList<>();
        mVehicleAdapter = new VehicleAdapter(getContext(), list);
        mVehicleAdapter.setItemClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvVehicles.setLayoutManager(mLayoutManager);
        rvVehicles.setAdapter(mVehicleAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mVehiclesUserRef.addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mVehicleAdapter.clear();
        keys.clear();
        mVehiclesUserRef.removeEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_add)
    public void add(){
//        Intent intent = new Intent(getContext(), CameraReaderActivity.class);
//        startActivityForResult(intent, CameraReader.BARCODE_REQUEST);

        showAddVehicleDialog();
    }

    private void showAddVehicleDialog() {
        FragmentManager fm = getChildFragmentManager();
        AddVehicleDialogFragment addVehicleDialogFragment = AddVehicleDialogFragment
                .newInstance(getString(R.string.txt_add_vehicle_title));
        addVehicleDialogFragment.setAddVehicleListener(this);
        addVehicleDialogFragment.show(fm, AddVehicleDialogFragment.TAG);
    }

    @Override
    public void onRegistered(Registration registration) {
        DatabaseReference vehicleRef = mVehiclesUserRef.getRoot()
                .child(getString(R.string.ref_vehicles_key)).push();
        vehicleRef.setValue(registration);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildAdded");
        Registration registration = dataSnapshot.getValue(Registration.class);
        keys.add(dataSnapshot.getKey());
        mVehicleAdapter.add(registration);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildChanged");
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved");
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildMoved");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled " + databaseError.getMessage());
    }

    @Override
    public void onClick(Registration registration, int position) {
        Intent intent = new Intent(getContext(), CardActivity.class);
        intent.putExtra(VEHICLE_ID_KEY, keys.get(position));
        startActivity(intent);
    }

    @Override
    public void onEditClicked(int position) {
        Intent intent = new Intent(getContext(), VehicleInfoActivity.class);
        intent.putExtra(VEHICLE_ID_KEY, keys.get(position));
        startActivity(intent);
    }
}
