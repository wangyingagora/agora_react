package com.agorareact.widget;

import android.util.Log;
import android.view.SurfaceView;

import com.agorareact.AgoraPackage;
import com.agorareact.MainApplication;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.lang.ref.WeakReference;

import io.agora.rtc.RtcEngine;

/**
 * Created by eaglewangy on 11/01/2018.
 */

public class SurfaceViewManager extends SimpleViewManager<SurfaceView> {
    public static final String REACT_CLASS = "SurfaceView";
    private WeakReference<AgoraPackage> mAgoraPackage;

    public SurfaceViewManager(AgoraPackage agoraPackage) {
        mAgoraPackage = new WeakReference<AgoraPackage>(agoraPackage);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected SurfaceView createViewInstance(ThemedReactContext reactContext) {
        //MainApplication app = (MainApplication)(reactContext.getApplicationContext());
        //SurfaceView surfaceView = RtcEngine.CreateRendererView(reactContext);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(reactContext);
        return surfaceView;
    }

    @ReactProp(name = "width", defaultInt = 360)
    public void setWidth(SurfaceView view, int width) {
        view.getHolder().setFixedSize(width, view.getHeight());
    }

    @ReactProp(name = "height", defaultInt = 480)
    public void setHeight(SurfaceView view, int height) {
        view.getHolder().setFixedSize(view.getWidth(), height);
    }

    @ReactProp(name = "uniqueId")
    public void setViewUniqueId(SurfaceView view, String id) {
        view.setTag(id);
        if (mAgoraPackage.get() != null) {
            mAgoraPackage.get().addSurface(id, view);
        }
    }
}
