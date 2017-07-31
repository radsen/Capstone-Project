package com.autollow.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.autollow.R;
import com.autollow.model.Registration;

/**
 * Created by radsen on 7/25/17.
 */

public class AddVehicleDialogFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener, DialogInterface.OnShowListener, View.OnClickListener {

    public static final String TAG = AddVehicleDialogFragment.class.getSimpleName();
    private static final String DIALOG_TITLE_KEY = "title";
    private static final int NO_SELECTION = 0;

    private Spinner mIdTypeSpinner;
    private TextView mLicensePlateEditText;
    private TextView mIdNumberEditText;
    private int selectedType;

    private TextInputLayout mPlateLayout;
    private TextInputLayout mTypeLayout;
    private TextInputLayout mNumberLayout;

    public void setAddVehicleListener(AddVehicleDialogListener listener) {
        this.listener = listener;
    }

    private AddVehicleDialogListener listener;

    @Override
    public void onShow(DialogInterface dialog) {
        Button btnOk = ((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE);
        btnOk.setTag(Dialog.BUTTON_POSITIVE);
        btnOk.setOnClickListener(this);
        Button btnCancel = ((AlertDialog) dialog).getButton(Dialog.BUTTON_NEGATIVE);
        btnCancel.setTag(Dialog.BUTTON_NEGATIVE);
        btnCancel.setOnClickListener(this);
    }

    public interface AddVehicleDialogListener{
        void onRegistered(Registration registration);
    }

    public static AddVehicleDialogFragment newInstance(String title) {
        AddVehicleDialogFragment frag = new AddVehicleDialogFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_KEY, title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(DIALOG_TITLE_KEY);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_vehicle, null);

        mPlateLayout = (TextInputLayout) view.findViewById(R.id.plateLayout);
        mTypeLayout = (TextInputLayout) view.findViewById(R.id.typeLayout);
        mNumberLayout = (TextInputLayout) view.findViewById(R.id.numberLayout);

        mLicensePlateEditText = (EditText) view.findViewById(R.id.lPlate);
        mIdNumberEditText = (EditText) view.findViewById(R.id.idNum);
        mIdTypeSpinner = (Spinner) view.findViewById(R.id.idType);

        ArrayAdapter<String> idTypeArrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.id_type_options));
        mIdTypeSpinner.setAdapter(idTypeArrayAdapter);
        mIdTypeSpinner.setOnItemSelectedListener(this);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok), null);
        alertDialogBuilder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }

    private boolean validateForm() {
        String lPlate = mLicensePlateEditText.getText().toString();
        String idNumber = mIdNumberEditText.getText().toString();

        boolean valid = true;

        if(TextUtils.isEmpty(lPlate)){
            mPlateLayout.setError(getString(R.string.error_plate_required));
            valid = false;
        }

        if(selectedType == NO_SELECTION){
            mTypeLayout.setError(getString(R.string.error_type_required));
            valid = false;
        }

        if(TextUtils.isEmpty(idNumber)){
            mNumberLayout.setError(getString(R.string.error_number_required));
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int id = (int) v.getTag();
        switch (id){
            case Dialog.BUTTON_POSITIVE:

                if(validateForm()){
                    Registration registration = new Registration();
                    registration.setLicensePlate(mLicensePlateEditText.getText().toString());
                    registration.setIdType(getResources().getStringArray(R.array.id_type_values)[selectedType]);
                    registration.setIdNumber(mIdNumberEditText.getText().toString());
                    listener.onRegistered(registration);
                    dismiss();
                }

                break;
            case DialogInterface.BUTTON_NEGATIVE:

                dismiss();

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedType = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
