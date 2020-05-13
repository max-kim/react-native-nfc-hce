import { NativeModules, DeviceEventEmitter, Platform } from "react-native";

const { RNHce } = NativeModules;

const isAndroid = Platform.OS === 'android';

export default {
  supportNFC: function() {
    return (isAndroid ? RNHce.supportNFC : () => ({ support: false, enabled: false }));
  },

  listenNFCStatus: function(callback) {
    isAndroid && DeviceEventEmitter.addListener("listenNFCStatus", resp => {
      callback(resp.status);
    });
  },

  setCardContent: function(value) {
    isAndroid && RNHce.setCardContent(value);
  },

  setAidFilter: function(value) {
    isAndroid && RNHce.setAidFilter(value);
  }
};
