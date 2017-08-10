package com.autollow.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.R;
import com.autollow.adapter.CardAdapter;
import com.autollow.common.IConstants;
import com.autollow.model.Document;
import com.autollow.model.DocumentFactory;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Radsen on 7/10/17.
 */

public class CardFragment extends BaseFragment implements ChildEventListener, IConstants {

    public static final String TAG = CardFragment.class.getSimpleName();

    @BindView(R.id.rv_cards)
    RecyclerView cardRecyclerView;

    private Unbinder unbinder;
    private CardAdapter mCardAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCardRef;

    public static CardFragment newInstance(Bundle bundle) {
        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(bundle);
        return cardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String vehicleId = getArguments().getString(VEHICLE_ID_KEY);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Uri vehicleDocsUri = new Uri.Builder()
                .appendPath(getString(R.string.ref_vehicle_doc_key))
                .appendPath(vehicleId)
                .build();
        mCardRef = mFirebaseDatabase.getReference().child(vehicleDocsUri.toString());

        List<Document> list = new ArrayList<>();
        mCardAdapter = new CardAdapter(getContext(), list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        cardRecyclerView.setLayoutManager(mLayoutManager);
        cardRecyclerView.setAdapter(mCardAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCardRef.addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCardRef.removeEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String type = dataSnapshot.child("type").getValue().toString();
        Class objectClass = DocumentFactory.getDocument(type);
        Document document = (Document) dataSnapshot.getValue(objectClass);
        if(document.isRequired()){
            mCardAdapter.add(document);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
