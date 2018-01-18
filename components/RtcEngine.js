'use strict';

import AgoraModule from './android/agora'
import SurfaceView from './android/SurfaceView'

import {
    findNodeHandle,
    UIManager
  } from 'react-native';

var RtcEnine = {
    create(appId: string, callback: (channel: string) => any) {
        AgoraModule.create(appId, callback);
    },
    
    setupLocalVideo(view: SurfaceView, width: number, height: height, uid: number) {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(view),
            UIManager.SurfaceView.Commands.localVideo,
            [width, height, uid]
        );
    },

    callAPI(api: string, ...args: Array<any>) {
        AgoraModule.callAPI(api, args)
    },
};

module.exports = RtcEnine;