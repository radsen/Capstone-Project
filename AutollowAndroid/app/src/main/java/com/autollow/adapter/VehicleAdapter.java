package com.autollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.model.Registration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by radsen on 7/27/17.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Registration> mVehicleList;
    private final LayoutInflater inflater;

    private VehicleClickListener mItemClickListener;

    public interface VehicleClickListener {
        void onClick(Registration registration, int position);
    }

    public VehicleAdapter(Context context, List<Registration> vehicleList){
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVehicleList = vehicleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Registration registration = mVehicleList.get(position);
        holder.brandTextView.setText(registration.getBrand());
        holder.modelTextView.setText(registration.getModel());
        holder.pictureImageView.setImageResource(R.drawable.ic_default);
        holder.licensePlateTextView.setText(registration.getLicensePlate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onClick(registration, position);
            }
        });
    }

    public void setItemClickListener(VehicleClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mVehicleList.size();
    }

    public void add(Registration registration) {
        mVehicleList.add(registration);
        notifyDataSetChanged();
    }

    public void clear() {
        mVehicleList.clear();
        notifyDataSetChanged();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
