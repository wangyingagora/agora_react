package com.agorareact.widget;

import android.view.SurfaceView;

import com.agorareact.MainApplication;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import io.agora.rtc.RtcEngine;

/**
 * Created by eaglewangy on 11/01/2018.
 */

public class SurfaceViewManager extends SimpleViewManager<SurfaceView> {
    public static final String REACT_CLASS = "SurfaceView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected SurfaceView createViewInstance(ThemedReactContext reactContext) {
        //MainApplication app = (MainApplication)(reactContext.getApplicationContext());
        //SurfaceView surfaceView = RtcEngine.CreateRendererView(reactContext);
        return RtcEngine.CreateRendererView(reactContext);
    }

    @ReactProp(name = "width", defaultInt = 360)
    public void setWidth(SurfaceView view, int width) {
        view.getHolder().setFixedSize(width, view.getHeight());
    }

    @ReactProp(name = "height", defaultInt = 480)
    public void setHeight(SurfaceView view, int height) {
        view.getHolder().setFixedSize(view.getWidth(), height);
    }
}
