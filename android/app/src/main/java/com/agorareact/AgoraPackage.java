package com.agorareact;

import android.view.SurfaceView;

import com.agorareact.modules.AgoraModule;
import com.agorareact.widget.SurfaceViewManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eaglewangy on 10/01/2018.
 */

public class AgoraPackage implements ReactPackage {
    private Map<String, SurfaceView> mSurfaceViews = new HashMap<>();

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        AgoraModule m = new AgoraModule(reactContext);
        m.setAgoraPackage(this);
        modules.add(m);
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        //return Collections.emptyList();
        return Arrays.<ViewManager>asList(
                new SurfaceViewManager(this)
        );
    }

    public void addSurface(String key, SurfaceView value) {
        mSurfaceViews.put(key, value);
    }

    public SurfaceView getSurfaceView(String key) {
        return mSurfaceViews.get(key);
    }

    public void removeSurfaceView(String key) {
        mSurfaceViews.remove(key);
    }
}
