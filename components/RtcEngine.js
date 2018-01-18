'use strict';

import AgoraModule from './android/agora'
import SurfaceView from './android/SurfaceView'

import {
    Alert,
    findNodeHandle,
    UIManager
  } from 'react-native';

var RtcEnine = {
    create(appId: string, callback: any) {
        AgoraModule.create(appId, (data) => {
            if (data.type.localeCompare('onJoinChannelSuccess') == 0) {
                callback[data.type](data.channel, data.uid, data.elapsed)
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

    callAPI(api: string, args: Array<any>) {
        AgoraModule.callAPI(api, args)
    },
};

module.exports = RtcEnine;