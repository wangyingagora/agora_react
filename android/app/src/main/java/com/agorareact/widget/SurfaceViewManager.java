package com.agorareact.widget;

import android.util.Log;
import android.view.SurfaceView;

import com.agorareact.AgoraPackage;
import com.agorareact.MainApplication;
import com.agorareact.modules.AgoraModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.lang.ref.WeakReference;
import java.util.Map;

import javax.annotation.Nullable;

import io.agora.rtc.RtcEngine;

/**
 * Created by eaglewangy on 11/01/2018.
 */

public class SurfaceViewManager extends SimpleViewManager<SurfaceView> {
    public static final String REACT_CLASS = "SurfaceView";

    public static final int COMMAND_SET_LOCAL_VIDEO = 1;

    private WeakReference<AgoraModule> mAgoraModule;

    public SurfaceViewManager(AgoraModule agoraModule) {
        mAgoraModule = new WeakReference<AgoraModule>(agoraModule);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected SurfaceView createViewInstance(ThemedReactContext reactContext) {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(reactContext);
        surfaceView.setZOrderOnTop(true);
        surfaceView.setZOrderMediaOverlay(true);
        return surfaceView;
    }

    /*
    @ReactProp(name = "width", defaultInt = 360)
    public void setWidth(SurfaceView view, int width) {
        view.getHolder().setFixedSize(width, 480);
    }

    @ReactProp(name = "height", defaultInt = 480)
    public void setHeight(SurfaceView view, int height) {
        view.getHolder().setFixedSize(360, height);
    }

    @ReactProp(name = "uniqueId")
    public void setViewUniqueId(SurfaceView view, String id) {
        view.setTag(id);
    }*/

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        Log.d("React"," View manager getCommandsMap:");
        return MapBuilder.of(
                "localVideo",
                COMMAND_SET_LOCAL_VIDEO);
    }

    @Override
    public void receiveCommand(SurfaceView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_SET_LOCAL_VIDEO: {
                //view.saveImage();
                mAgoraModule.get().setupLocalVideo(root,
                        args.getInt(0), args.getInt(1), args.getInt(2));
                return;
            }

            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandId,
                        getClass().getSimpleName()));
        }
    }
}
