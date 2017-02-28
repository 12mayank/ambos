package co.jlabs.famb.boon.apps.logic.presenter;

import android.support.annotation.Nullable;

import co.jlabs.famb.boon.apps.logic.presenter_view.MainView;


public final class MainPresenter {

    @Nullable
    private MainView view;

    public MainPresenter(@Nullable MainView view) {
        this.view = view;
    }



    public void addTextView() {
        if (null != getView()) {
            getView().prepareTextView();
        }
    }

    public void addCalendarView() {
        if (null != getView()) {
            getView().prepareCalendarView();
        }
    }

    public void animate() {
        if (null != getView()) {
            getView().animateViews();
        }
    }

    public void detachView() {
        this.view = null;
    }

    @Nullable
    public MainView getView() {
        return view;
    }
}
