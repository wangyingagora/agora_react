package com.agorareact.modules;

import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.agorareact.AgoraPackage;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by eaglewangy on 10/01/2018.
 */

public class AgoraModule extends ReactContextBaseJavaModule {
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    private RtcEngine mRtcEngine;
    private IRtcEngineEventHandler mHandler;

    private WeakReference<AgoraPackage> mAgoraPackage;

    public AgoraModule(ReactApplicationContext reactContext) {
        super(reactContext);

        mHandler = new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);
            }

            @Override
            public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onRejoinChannelSuccess(channel, uid, elapsed);
            }

            @Override
            public void onWarning(int warn) {
                super.onWarning(warn);
            }

            @Override
            public void onError(int err) {
                super.onError(err);
            }

            @Override
            public void onCameraFocusAreaChanged(Rect rect) {
                super.onCameraFocusAreaChanged(rect);
            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                super.onLeaveChannel(stats);
            }

            @Override
            public void onRtcStats(RtcStats stats) {
                super.onRtcStats(stats);
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
            }

            @Override
            public void onUserOffline(int uid, int reason) {
                super.onUserOffline(uid, reason);
            }


            @Override
            public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
                super.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
            }
        };

        try {
            mRtcEngine = RtcEngine.create(reactContext, "aab8b8f5a8cd4469a63042fcfafe7063", mHandler);
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
    public void setupLocalVideo(int key, int uid) {
        if (mAgoraPackage.get() == null) return;

        SurfaceView view = mAgoraPackage.get().getSurfaceView(key);
        if (view == null) return;

        mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        view.setZOrderOnTop(true);
        view.setZOrderMediaOverlay(true);
    }

    @ReactMethod
    public void joinChannel() {
        mRtcEngine.joinChannel(null, "1234", "ARCore with RtcEngine", 0);
    }

    @ReactMethod
    public void startPreview() {
        mRtcEngine.startPreview();
    }
}
