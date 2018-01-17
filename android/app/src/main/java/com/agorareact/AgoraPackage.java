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
    private AgoraModule mModule;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        if (mModule == null) {
            mModule = new AgoraModule(reactContext);
            mModule.setAgoraPackage(this);
        }

        modules.add(mModule);
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        //return Collections.emptyList();
        //mViewManagers.add(new SurfaceViewManager(this));
        //return mViewManagers;
        if (mModule == null) {
            mModule = new AgoraModule(reactContext);
            mModule.setAgoraPackage(this);
        }
        return Arrays.<ViewManager>asList(
                new SurfaceViewManager(mModule)
        );
    }

    /*public SurfaceViewManager getSurfaceViewManager() {
        return (SurfaceViewManager)mViewManagers.get(0);
    }

    public void setLocalVideo(SurfaceView view, int uid) {
        ((AgoraModule)(mModules.get(0))).setupLocalVideo(view, uid);
    }

    public void addSurface(String key, SurfaceView value) {
        mSurfaceViews.put(key, value);
    }

    public SurfaceView getSurfaceView(String key) {
        return mSurfaceViews.get(key);
    }

    public void removeSurfaceView(String key) {
        mSurfaceViews.remove(key);
    }*/
}
