package com.agorareact.modules;

import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.agorareact.AgoraPackage;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by eaglewangy on 10/01/2018.
 */

public class AgoraModule extends ReactContextBaseJavaModule {
    private final static String TAG = AgoraModule.class.getSimpleName();

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    private ReactApplicationContext mReactContext;
    private RtcEngine mRtcEngine;
    private IRtcEngineEventHandler mHandler;

    private ConcurrentHashMap<String, Method> mMethods = new ConcurrentHashMap<>();
    private Map<String, Boolean> mInternalMethods = new HashMap<>();

    private WeakReference<AgoraPackage> mAgoraPackage;
    private WeakReference<Callback> mCallback;

    public AgoraModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    public void setAgoraPackage(AgoraPackage agoraPackage) {
        mAgoraPackage = new WeakReference<AgoraPackage>(agoraPackage);
    }

    @Override
    public String getName() {
        return "AgoraModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public void create(String appId, Callback callback) {
        mCallback = new WeakReference<Callback>(callback);

        mHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onJoinChannelSuccess");
                    map.putString("p0", channel);
                    map.putInt("p1", uid);
                    map.putInt("p2", elapsed);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onRejoinChannelSuccess");
                    map.putString("p0", channel);
                    map.putInt("p1", uid);
                    map.putInt("p2", elapsed);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onWarning(int warn) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onWarning");
                    map.putInt("p0", warn);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onError(int err) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onError");
                    map.putInt("p0", err);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onCameraFocusAreaChanged(Rect rect) {
                if (canCallack()) {
                    //mCallback.get().invoke(rect);
                }
            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                if (canCallack()) {
                    //mCallback.get().invoke(stats);
                }
            }

            @Override
            public void onRtcStats(RtcStats stats) {
                if (canCallack()) {
                    //mCallback.get().invoke(stats);
                }
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onUserJoined");
                    map.putInt("p0", uid);
                    map.putInt("p1", elapsed);
                    mCallback.get().invoke(map);
                }
            }

            @Override
            public void onUserOffline(int uid, int reason) {
                if (canCallack()) {
                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onUserOffline");
                    map.putInt("p0", uid);
                    map.putInt("p1", reason);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
                if (canCallack()) {
                    if (mAgoraPackage.get() == null) return;

                    WritableMap map = Arguments.createMap();
                    map.putString("type", "onFirstRemoteVideoDecoded");
                    map.putInt("p0", uid);
                    map.putInt("p1", width);
                    map.putInt("p2", height);
                    map.putInt("p3", elapsed);
                    //mCallback.get().invoke(map);
                }
            }

            @Override
            public void onApiCallExecuted(int error, String api, String result) {
                if (canCallack()) {
                }
            }
        };

        try {
            mRtcEngine = RtcEngine.create(mReactContext, appId, mHandler);

            initPublicAPI();
            initInternalAPI();

            mRtcEngine.setParameters("{\"rtc.log_filter\": 65535}");
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.enableDualStreamMode(true);

            mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_480P, false);
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);

            //mSource = new AgoraVideoSource();
            //mRender = new AgoraVideoRender(0, true);
            //mRtcEngine.setVideoSource(mSource);
            //mRtcEngine.setLocalVideoRenderer(mRender);

            //mRtcEngine.startPreview();

        } catch (Exception ex) {
            Log.e("RCTNative", ex.toString());
            mRtcEngine = null;
        }
    }

    @ReactMethod
    public void destroy() {
        mMethods.clear();
        mInternalMethods.clear();

        RtcEngine.destroy();
    }

    @ReactMethod
    public void callAPI(String api, ReadableArray args) {
        Object[] parameters = new Object[args.size()];
        for (int i = 0; i < args.size(); ++i) {
            switch (args.getType(i)) {
                case Null:
                    parameters[i] = null;
                    continue;
                case Boolean:
                    parameters[i] = args.getBoolean(i);
                    continue;
                case Number:
                    parameters[i] = args.getDouble(i);
                    continue;
                case String:
                    parameters[i] = args.getString(i);
                    continue;
                case Map:
                    Log.e(TAG, "No support for array or map parameters at moment");
                    return;
                case Array:
                    Log.e(TAG, "No support for array or map parameters at moment");
                    return;
            }
        }

        if (mInternalMethods.containsKey(api)) {
            if (api.equals("setupLocalVideo")) {
                //setupLocalVideo((String)parameters[0], ((Double)parameters[1]).intValue());
            }
        } else {
            callAPI(api, parameters);
        }
    }

    //@ReactMethod
    /*private void setupLocalVideo(String key, int uid) {
        if (mAgoraPackage.get() == null) return;

        SurfaceView view = mAgoraPackage.get().getSurfaceView(key);
        if (view == null) return;

        mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        view.setZOrderOnTop(true);
        view.setZOrderMediaOverlay(true);
    }*/

    public void setupLocalVideo(SurfaceView view, int width, int height, int uid) {
        if (mAgoraPackage.get() == null) return;

        view.getHolder().setFixedSize(width, height);
        mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    public void setupRemoteVideo(SurfaceView view, int width, int height, int uid) {
        if (mAgoraPackage.get() == null) return;

        view.getHolder().setFixedSize(width, height);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    //@ReactMethod
    private void joinChannel() {
        mRtcEngine.joinChannel(null, "1234", "ARCore with RtcEngine", 0);
    }

    @ReactMethod
    public void startPreview() {
        mRtcEngine.startPreview();
    }

    @ReactMethod
    public void removeView(String viewId) {
        if (mAgoraPackage.get() == null) return;

        //mAgoraPackage.get().removeSurfaceView(viewId);
    }

    private void initPublicAPI() {
        Method[] methods = mRtcEngine.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("native")) {
                continue;
            }

            mMethods.put(name, m);
        }
    }

    private void initInternalAPI() {
        mInternalMethods.put("setupLocalVideo", true);
    }

    private boolean canCallack() {
        if (mCallback == null || mCallback.get() == null) return false;
        return true;
    }

    private void callAPI(String api, Object[] args) {
        try {
            Method m = mMethods.get(api);
            int n = args.length;

            Class<?>[] cls = m.getParameterTypes();
            for (int i = 0; i < n; ++i) {
                if (cls[i].equals(Integer.TYPE)) {
                    args[i] = ((Double)args[i]).intValue();
                }
            }
            switch (n) {
                case 0:
                    callAPI0(m, args);
                    break;
                case 1:
                    callAPI1(m, args);
                    break;
                case 2:
                    callAPI2(m, args);
                    break;
                case 3:
                    callAPI3(m, args);
                    break;
                case 4:
                    callAPI4(m, args);
                    break;
                case 5:
                    callAPI5(m, args);
                    break;
                case 6:
                    callAPI6(m, args);
                    break;
                case 7:
                    callAPI7(m, args);
                    break;
                default:
                    Log.e(TAG, "Need add more callAPI methods");
                    break;
            }
        } catch (IllegalAccessException ex) {
            Log.e(TAG, ex.toString());
        } catch (InvocationTargetException ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private void callAPI0(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine);
    }

    private void callAPI1(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0]);
    }

    private void callAPI2(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1]);
    }

    private void callAPI3(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1], args[2]);
    }

    private void callAPI4(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1], args[2] ,args[3]);
    }

    private void callAPI5(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1], args[2], args[3], args[4]);
    }

    private void callAPI6(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
    }

    private void callAPI7(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(mRtcEngine, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
    }
}
