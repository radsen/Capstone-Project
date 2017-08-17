package com.autollow.base;

/**
 * Created by radsen on 8/16/17.
 */

public interface BasePresenter<V extends BaseView> {
    void onAttach(V BaseView);

    void onDetach();
}
