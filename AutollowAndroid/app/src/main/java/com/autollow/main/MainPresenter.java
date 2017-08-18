package com.autollow.main;

import com.autollow.base.Presenter;

/**
 * Created by radsen on 8/16/17.
 */

public class MainPresenter<V extends MainView> extends Presenter<V> implements MainPresenterImpl<V> {

    @Override
    public void onVehiclesClick() {
        getView().closeNavMenu();
        getView().showVehicles();
    }

    @Override
    public void onProfileClick() {
        getView().closeNavMenu();
        getView().showProfile();
    }

    @Override
    public void onNavCreated() {

    }

    @Override
    public void onViewInitialized() {

    }
}
