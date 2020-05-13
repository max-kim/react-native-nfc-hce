# react-native-nfc-hce

Only Android supported !!!

## Requirements

- Android (API 19+)
- Android SDK 28
- Android Build Tools v28.0.2

## Getting started

- add next row to package.json 

    ```javascript
    {
      ...
      "dependencies": {
        ...
        "react-native-nfc-hce": "git+https://github.com/max-kim/react-native-nfc-hce.git",
      }
    }
    ```
    
- run command

    `$ npm install`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`

    - Add `import net.kim_dev.react_native.RNHcePackage;` to the imports at the top of the file
    - Add `new RNHcePackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:
   ```
   include ':react-native-nfc-hce'
   project(':react-native-nfc-hce').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-nfc-hce/android')
   ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
   ```
    dependencies {
        ...
        implementation project(':react-native-nfc-hce')
    }
   ```
4. Add the following lines to `android/app/src/main/java/[...]/MainApplication.java`:
   ```
    import net.kim_dev.react_native.RNHcePackage;
   
    ...
   
    public class MainApplication extends NavigationApplication implements ShareApplication, ReactApplication {
    
    ...
   
        protected List<ReactPackage> getPackages() {
            // Add additional packages you require here
            // No need to add RnnPackage and MainReactPackage
            return Arrays.<ReactPackage>asList(
                // eg. new VectorIconsPackage()
                ...
                new RNHcePackage()
            );
        }
    }
   ```

## Setup

1. Open up `android/app/src/main/AndroidManifest.xml`

- Add `<uses-permission android:name="android.permission.NFC" />`
- Add
  ```
  <service
     android:name="net.kim_dev.react_native.CardService"
     android:exported="true"
     android:permission="android.permission.BIND_NFC_SERVICE">
     <intent-filter>
         <action android:name="android.nfc.cardemulation.action.HOST_APDU_SERVICE" />
     </intent-filter>
     <meta-data
         android:name="android.nfc.cardemulation.host_apdu_service"
         android:resource="@xml/aid_list" />
    </service>
  ```

2. Create `aid_list.xml` in `android/app/src/main/res/xml/`

    - Add code in `aid_list.xml`

    ```xml
    <host-apdu-service xmlns:android="http://schemas.android.com/apk/res/android"
        android:description="@string/service_name"
        android:requireDeviceUnlock="true">
        <aid-group
            android:category="other"
            android:description="@string/card_title">
            <aid-filter android:name="F04B694D4D4D4D" />
        </aid-group>
    </host-apdu-service>
    ```


## Usage

```javascript
import HCE from "react-native-nfc-hce";

componentDidMount = () => {
  const { supported, enabled } = HCE.supportNFC();
  // it's value should be equal "<aid-filter />" from aid_list.xml
  const NFC_HCE_AID = "F04B694D4D4D4D";  

  supported && HCE.setAidFilter(NFC_HCE_AID);

  HCE.listenNFCStatus(enabled => {
    console.log("NFC enabled: ", enabled);
  });
};

onChangeText = text => {
  if (text.length > 0) {
    HCE.setCardContent(text);
  }
};
```

## API

Get NFC supported and enabled

```supportNFC(): { enabled: boolean, enabled: boolean }```

Listen NFC status changing

```listenNFCStatus(callback(enabled: boolean)):```

Write CardEmulation content

```setCardContent(content: string): void```

Set AID value

```setAidFilter(aid: string): void```
