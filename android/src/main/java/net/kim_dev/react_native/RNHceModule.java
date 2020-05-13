package net.kim_dev.react_native;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class RNHceModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;

    public RNHceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addLifecycleEventListener(this);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        this.reactContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public String getName() {
        return "RNHce";
    }

    private WritableMap supportNFC() {
        NfcManager manager = (NfcManager) this.reactContext.getSystemService(this.reactContext.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        WritableMap map = Arguments.createMap();
        if (adapter != null) {
            map.putBoolean("supported", true);
            if (adapter.isEnabled()) {
                map.putBoolean("enabled", true);
            } else {
                map.putBoolean("enabled", false);
            }
        } else {
            map.putBoolean("supported", false);
            map.putBoolean("enabled", false);
        }
        return map;
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap payload) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, payload);
    }

    @ReactMethod
    public void setCardContent(String value) {
        RNStorage.setValue(this.reactContext, value, RNStorage.HCE_ID);
    }

    @ReactMethod
    public void setAidFilter(String value) {
        RNStorage.setValue(this.reactContext, value, RNStorage.HCE_AID);
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("supportNFC", supportNFC());
        return constants;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                        NfcAdapter.STATE_OFF);
                WritableMap payload = Arguments.createMap();
                switch (state) {
                    case NfcAdapter.STATE_OFF:
                        sendStatusToReactContext(payload, false);
                        break;
                    case NfcAdapter.STATE_ON:
                        sendStatusToReactContext(payload, true);
                        break;
                    case NfcAdapter.STATE_TURNING_OFF:
                    case NfcAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    private void sendStatusToReactContext(WritableMap map, Boolean status) {
        map.putBoolean("status", status);
        sendEvent(reactContext, "listenNFCStatus", map);
    }

    @Override
    public void onHostResume() {}

    @Override
    public void onHostPause() {}

    @Override
    public void onHostDestroy() {
        try {
            this.reactContext.unregisterReceiver(mReceiver);
        } catch (Exception ignore) {
            // ignore if already unregistered
        }
    }
}