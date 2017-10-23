package com.autollow.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.profile.ProfilePresenter;

import butterknife.BindView;
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

        @BindView(R.id.tv_driver_license_num)
        TextView tvDriverLicenseNum;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_issued_date)
        TextView tvIssueDate;

        @BindView(R.id.tv_expiration_date)
        TextView tvExpirationDate;

        @BindView(R.id.tv_category)
        TextView tvCategory;


        public ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setNumber(String number) {
            tvDriverLicenseNum.setText(number);
        }

        @Override
        public void setName(String name) {
            tvName.setText(name);
        }

        @Override
        public void setIssueDate(String issueDate) {
            tvIssueDate.setText(issueDate);
        }

        @Override
        public void setExpirationDate(String expirationDate) {
            tvExpirationDate.setText(expirationDate);
        }

        @Override
        public void setCategory(String category) {
            tvCategory.setText(category);
        }
    }
}
