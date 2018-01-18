//import React from 'react';
import PropTypes from 'prop-types'
import {requireNativeComponent, ViewPropTypes} from 'react-native';

var iface = {
    name: 'SurfaceView',
    propTypes:  {
        //src: PropTypes.array,
        //borderRadius: PropTypes.number,
        //resizeMode: PropTypes.oneOf(['cover', 'contain', 'stretch']),
        width: PropTypes.number,
        height: PropTypes.number,
        //uniqueId: PropTypes.string,
        ...ViewPropTypes, // include the default view properties
    },
};

module.exports = requireNativeComponent('SurfaceView', iface);