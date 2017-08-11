package com.autollow.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.autollow.R;

/**
 * Created by radsen on 8/10/17.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final String[] list;
    private final LayoutInflater inflater;
    private final int layout;

    public SpinnerAdapter(Context context, int layout, String[] vehicleTypes) {
        super(context, layout);
        list = vehicleTypes;
        this.layout = layout;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getRowView(position, convertView);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getRowView(position, convertView);
    }

    private View getRowView(int position, View convertView) {
        String rowItem = list[position];
        ViewHolder holder ;
        View rowView = convertView;
        if (rowView==null) {

            holder = new ViewHolder();
            rowView = inflater.inflate(layout, null, false);

            holder.txtTitle = (TextView) rowView.findViewById(R.id.title);
            rowView.setTag(holder);
        }else{
            holder = (ViewHolder) rowView.getTag();
        }

        holder.txtTitle.setText(rowItem);

        return rowView;
    }

    private class ViewHolder{
        TextView txtTitle;
    }
}
