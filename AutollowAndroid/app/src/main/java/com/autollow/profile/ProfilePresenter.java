package com.autollow.profile;

import com.autollow.adapter.ProfileAdapter;

/**
 * Created by radsen on 8/23/17.
 */

public interface ProfilePresenter {
    int getProfileDocumentsCount();

    void onBindDocumentsRowViewAtPosition(int position, ProfileAdapter.ProfileViewHolder holder);
}
