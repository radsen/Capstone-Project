package com.autollow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.R;
import com.autollow.profile.ProfilePresenter;

import butterknife.ButterKnife;

/**
 * Created by radsen on 8/23/17.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private static final int LICENSE = 0;
    private static final int TICKET = 1;

    private final ProfilePresenter presenter;

    public ProfileAdapter(ProfilePresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = 0;
        switch (viewType){
            case LICENSE:
                rowLayout = R.layout.item_driver_license;
                break;
            case TICKET:
                rowLayout = R.layout.item_ticket_summary;
                break;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        presenter.onBindDocumentsRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getProfileDocumentsCount();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder implements ILicenseView {



        public ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
