package com.autollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.model.Registration;
import com.autollow.util.BindingUtils;
import com.autollow.vehicle.VehiclePresenter;
import com.autollow.vehicle.VehiclePresenterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by radsen on 7/27/17.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private Context mContext;
    private Map<String, Registration> mVehicleMap;
    private LayoutInflater inflater;

    private VehiclePresenter mPresenter;
    private List<String> mKeyList;
    private List<Registration> mRegistrationList;

    public VehicleAdapter(Context context, VehiclePresenter presenter) {
        mPresenter = presenter;
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Registration registration = mRegistrationList.get(position);
        holder.brandTextView.setText(registration.getBrand());
        holder.modelTextView.setText(registration.getModel());
        BindingUtils.loadImage(holder.pictureImageView, registration.getPicture());
        holder.licensePlateTextView.setText(registration.getLicensePlate());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mItemClickListener.onEditClicked(position);
                mPresenter.onEditVehicle(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onVehicleClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mVehicleMap == null){
            return 0;
        }
        return mVehicleMap.size();
    }

    public void clear() {
        mVehicleMap.clear();
        notifyDataSetChanged();
    }

    public void setAdapter(Map<String, Registration> map) {
        this.mVehicleMap = map;
        mKeyList = new ArrayList<String>(mVehicleMap.keySet());
        mRegistrationList = new ArrayList<Registration>(mVehicleMap.values());
        notifyDataSetChanged();
    }

    public String getVehicle(int position) {
        return mKeyList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture)
        ImageView pictureImageView;

        @BindView(R.id.license_plate)
        TextView licensePlateTextView;

        @BindView(R.id.brand)
        TextView brandTextView;

        @BindView(R.id.model)
        TextView modelTextView;

        @BindView(R.id.btn_edit)
        ImageButton editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
