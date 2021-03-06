/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { AppRegistry, Image, ReactNative, findNodeHandle} from 'react-native'
import SurfaceView from './components/android/SurfaceView'
import APPID from './components/android/Constants' 
import RtcEnine from './components/RtcEngine'

import {
  Platform,
  StyleSheet,
  Text,
  Button,
  View,
  Alert,
  UIManager,
  DeviceEventEmitter
} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

class Greeeting extends Component {
  render() {
    //let display = this.state.showText? this.props.text : ' ';
    return (
      <Text> {this.props.text} </Text>
    );
  }
}

export default class App extends Component<{}> {
  componentDidMount() {
  }

  componentWillUnmount() {
    //AgoraModule.removeView('1111');
  }

  _joinChannel() {
    var reactTag = findNodeHandle(this._surfaceView);
    //Alert.alert(reactTag + '')

    RtcEnine.setupLocalVideo(this._localView, 240, 320, 0)
    RtcEnine.callAPI('startPreview', [])
    //token, channelName, String optionalInfo, int optionalUid
    RtcEnine.callAPI('joinChannel', ['', 'AgoraChannel', '', 0])

    /*
    AgoraModule.setupLocalVideo('1111', 0);
    AgoraModule.startPreview();
    AgoraModule.joinChannel();
    */
  }

  render() {
    //AgoraModule.show('Call java method', AgoraModule.SHORT);

    var handler = {
      'onJoinChannelSuccess': (channel, uid, elapsed) => {
      },
      'onRejoinChannelSuccess': (channel, uid, elapsed) => {
      },
      'onError': (error) => {
      },
      'onUserJoined': (uid, elapsed) => {
        RtcEnine.setupRemoteVideo(this._remoteView1, 96, 96, uid)
      },
      'onUserOffline': (uid, reason) => {
      },
      'onFirstRemoteVideoDecoded': (uid, width, height, elapsed) => {
         //RtcEnine.setupRemoteVideo(this._remoteView1, 96, 96, uid)
      },
    };
    RtcEnine.create(APPID, handler);

    let pic = {
      uri: 'http://g.hiphotos.baidu.com/image/pic/item/241f95cad1c8a786c7dedcc46e09c93d71cf5007.jpg'
    };

    return (
      <View style = {styles.container} >
        <Greeeting text='Greeting from react world' />
        <SurfaceView 
          style = { {width: 240, height: 320}}
          ref={component => this._localView = component}
        />
        <View style = {styles.remote} >
          <SurfaceView 
            style = { {width: 96, height: 96}}
            ref={component => this._remoteView1 = component}
          />
        </View>
        <Button
          onPress={this._joinChannel.bind(this)}
          title="Join Channel"
          color="#841584"
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  remote: {
    width: 360,
    height: 96,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center'
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
