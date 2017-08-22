package com.autollow.vehicle;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.autollow.R;
import com.autollow.model.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radsen on 8/22/17.
 */

public class VehicleInteractorImpl implements VehicleInteractor,
        ValueEventListener {

    private final String TAG = VehicleInteractorImpl.class.getSimpleName();

    private String UUId;

    private final Context context;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mVehiclesRef;

    private List<Registration> mCurrentVehicleList = new ArrayList<>();
    private LoadListener listener;

    public VehicleInteractorImpl(Context context){

        this.context = context;

        if(mAuth.getCurrentUser() != null){
            UUId = mAuth.getCurrentUser().getUid();
        }

        Uri uri = new Uri.Builder()
                .appendPath(context.getString(R.string.ref_vehicles_user_key))
                .appendPath(UUId).build();

        mVehiclesRef = mDatabase.getReference()
                .child(uri.toString());
    }

    @Override
    public void loadVehicles(LoadListener listener) {
        this.listener = listener;
        mVehiclesRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChange");
        mCurrentVehicleList.clear();

        for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
            Registration registration = messagesSnapshot.getValue(Registration.class);
            mCurrentVehicleList.add(registration);
        }

        listener.onLoaded(mCurrentVehicleList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled");
    }
}
