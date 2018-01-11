/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import { AppRegistry, Image} from 'react-native'
import AgoraModule from './components/android/agora'
import SurfaceView from './components/android/SurfaceView'

import {
  Platform,
  StyleSheet,
  Text,
  Button,
  View,
  Alert
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
  _joinChannel() {
    //Alert.alert('You tapped the button!')
    AgoraModule.startPreview();
    AgoraModule.joinChannel();
  }

  render() {
    AgoraModule.show('Call java method', AgoraModule.SHORT);

    let pic = {
      uri: 'http://g.hiphotos.baidu.com/image/pic/item/241f95cad1c8a786c7dedcc46e09c93d71cf5007.jpg'
    };

    return (
      <View style = {styles.container} >
        <Greeeting text='Greeting from react world' />
        <SurfaceView style = { {width: 360, height: 480}} />
        <Button
          onPress={this._joinChannel}
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
