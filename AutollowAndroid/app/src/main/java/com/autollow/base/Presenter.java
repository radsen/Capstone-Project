package com.autollow.base;

import com.autollow.main.MainView;

/**
 * Created by radsen on 8/16/17.
 */

public class Presenter<V extends BaseView> implements BasePresenter<V> {

    private V mView;

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {

    }

    public V getView() {
        return mView;
    }
}
