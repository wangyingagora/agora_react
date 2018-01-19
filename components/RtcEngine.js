'use strict';

import AgoraModule from './android/AgoraAndroidModule'
import SurfaceView from './android/SurfaceView'

import {
    Alert,
    findNodeHandle,
    UIManager,
    DeviceEventEmitter
  } from 'react-native';

var RtcEnine = {
    create(appId: string, callback: any) {
        DeviceEventEmitter.addListener('onJoinChannelSuccess', (data) => {
            if (typeof callback['onJoinChannelSuccess'] === "undefined"){
                return
            }
            callback['onJoinChannelSuccess'](data.p0, data.p1, data.p2, data.p3)
        })
        DeviceEventEmitter.addListener('onUserJoined', (data) => {
            if (typeof callback['onUserJoined'] === "undefined"){
                return
            }
            callback['onUserJoined'](data.p0, data.p1)
        })

        /*
        for(var p in callback) {
            DeviceEventEmitter.addListener(p, (data) => {
                if (typeof callback[p] === "undefined"){
                    return
                }
                var n = callback[p].length
                if (n == 0) {
                    callback[p]()
                } else if (n == 1) {
                    callback[p](data.p0)
                } else if (n == 2) {
                    callback[p](data.p0, data.p1)
                } else if (n == 3) {
                    callback[p](data.p0, data.p1, data.p2)
                } else if (n == 4) {
                    callback[p](data.p0, data.p1, data.p2, data.p3)
                } else if (n == 5) {
                    callback[p](data.p0, data.p1, data.p2, data.p3, data.p4)
                } else if (n == 6) {
                    callback[p](data.p0, data.p1, data.p2, data.p3, data.p4, data.p5)
                } else if (n == 7) {
                    callback[p](data.p0, data.p1, data.p2, data.p3, data.p4, data.p5, data.p6)
                }
            })
         }
         */

        AgoraModule.create(appId, (data) => {
            //if (data.type.localeCompare('onJoinChannelSuccess') == 0) {
                //Alert.alert(callback[data.type].length + '')
                //callback[data.type](data.channel, data.uid, data.elapsed)
            //}
            if (typeof callback[data.type] === "undefined"){
                return
            }
        });
    },
    
    setupLocalVideo(view: SurfaceView, width: number, height: height, uid: number) {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(view),
            UIManager.SurfaceView.Commands.localVideo,
            [width, height, uid]
        );
    },

    setupRemoteVideo(view: SurfaceView, width: number, height: height, uid: number) {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(view),
            UIManager.SurfaceView.Commands.remoteVideo,
            [width, height, uid]
        );
    },

    callAPI(api: string, args: Array<any>) {
        AgoraModule.callAPI(api, args)
    },
};

module.exports = RtcEnine;