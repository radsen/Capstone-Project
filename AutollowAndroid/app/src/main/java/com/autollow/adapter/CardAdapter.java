package com.autollow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autollow.R;
import com.autollow.model.Document;

import java.util.List;

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
        View view = null;

        switch (viewType){
            case 1:
                view = inflater.inflate(R.layout.item_insurance, parent, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_emission, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return docsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return 1;
            case 1:
                return 2;
            default:
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
}
