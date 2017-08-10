package com.autollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.model.Document;
import com.autollow.model.Emission;
import com.autollow.model.Insurance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by radsen on 7/28/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private final List<Document> docsList;
    private final Context context;
    private final LayoutInflater inflater;

    public CardAdapter(Context context, List<Document> list) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        docsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view = null;

        switch (viewType){
            case Document.INSURANCE:
                view = inflater.inflate(R.layout.item_insurance, parent, false);
                holder = new InsuranceHolder(view);
                break;
            case Document.EMISSIONS:
                view = inflater.inflate(R.layout.item_emission, parent, false);
                holder = new EmissionHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof InsuranceHolder){
            InsuranceHolder insuranceHolder = (InsuranceHolder) holder;
            Insurance insurance = (Insurance) docsList.get(position);

            insuranceHolder.insuranceNameTextView.setText(insurance.getDesc().toUpperCase());
            insuranceHolder.insuranceNumberTextView.setText(insurance.getNumber());
            insuranceHolder.issuedDateTextView.setText(insurance.getIssued());
            insuranceHolder.startDateTextView.setText(insurance.getStarts());
            insuranceHolder.endDateTextView.setText(insurance.getExpires());

        } else if (holder instanceof  EmissionHolder){
            EmissionHolder eHolder = (EmissionHolder) holder;
            Emission emission = (Emission) docsList.get(position);

            eHolder.issuedDateTextView.setText(emission.getIssued());
            eHolder.expirationDateTextView.setText(emission.getExpiration());
            eHolder.emissionNumberTextView.setText(emission.getNumber());
            eHolder.emissionNameTextView.setText(emission.getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return docsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Document document = docsList.get(position);

        if(document.getType().equals("I")){
            return Document.INSURANCE;
        } else if (document.getType().equals("E")) {
            return Document.EMISSIONS;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void add(Document document) {
        docsList.add(document);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class InsuranceHolder extends ViewHolder {

        @BindView(R.id.tv_issued_date)
        TextView issuedDateTextView;

        @BindView(R.id.tv_start_date)
        TextView startDateTextView;

        @BindView(R.id.tv_end_date)
        TextView endDateTextView;

        @BindView(R.id.tv_insurance_number)
        TextView insuranceNumberTextView;

        @BindView(R.id.tv_insurance_name)
        TextView insuranceNameTextView;

        public InsuranceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class EmissionHolder extends ViewHolder {

        @BindView(R.id.tv_issued_date)
        TextView issuedDateTextView;

        @BindView(R.id.tv_expiration_date)
        TextView expirationDateTextView;

        @BindView(R.id.tv_emission_title)
        TextView emissionNameTextView;

        @BindView(R.id.tv_emissions_number)
        TextView emissionNumberTextView;

        public EmissionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
