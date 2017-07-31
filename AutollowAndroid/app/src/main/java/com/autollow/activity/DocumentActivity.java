package com.autollow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.autollow.R;
import com.autollow.common.IConstants;
import com.autollow.fragment.CardFragment;
import com.autollow.model.Registration;

/**
 * Created by radsen on 7/28/17.
 */

public class DocumentActivity extends BaseActivity implements IConstants {

    private CardFragment cardFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            cardFragment = CardFragment.newInstance(bundle);
            attachToActivity(R.id.master, cardFragment, CardFragment.TAG);
        }
    }
}
