package com.agorareact.modules;

import com.agorareact.utils.JSONUtils;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eaglewangy on 17/01/2018.
 */

public class RtcInvocationHandler implements InvocationHandler {
    private Object target;
    private WeakReference<Callback> mCallback;
    private final Map<String, Method> methods = new HashMap<>();

    public RtcInvocationHandler(Object target, Callback callback) {
        this.target = target;
        mCallback = new WeakReference<Callback>(callback);

        for(Method method: target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WritableNativeMap map = new WritableNativeMap();
        map.putString("type", method.getName());
        JSONArray array = new JSONArray();
        for (Object o : args) {
            array.put(o);
        }

        WritableArray jsArray = JSONUtils.convertJsonToArray(array);
        map.putArray("params", jsArray);
        if (mCallback == null && mCallback.get() == null) {
            mCallback.get().invoke(map);
        }

        //Object result = methods.get(method.getName()).invoke(target, args);
        return target;
    }
}
