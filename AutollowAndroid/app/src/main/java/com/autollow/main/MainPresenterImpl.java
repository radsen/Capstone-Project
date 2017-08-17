package com.autollow.main;

import com.autollow.base.BasePresenter;

/**
 * Created by radsen on 8/16/17.
 */

interface MainPresenterImpl<V extends MainView> extends BasePresenter<V> {

    void onVehiclesClick();

    void onProfileClick();

    void onNavCreated();

    void onViewInitialized();
}
