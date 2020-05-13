package net.kim_dev.react_native;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class CardService extends HostApduService {

    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final String UNKNOWN_CMD_SW = "8888";


    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        String data = RNStorage.getValue(this.getApplicationContext(), RNStorage.HCE_ID);
        byte[] accountBytes = data.getBytes();
        return accountBytes;
//        byte[] selectedApdu = this.getSelectedApdu();
//        if (Arrays.equals(selectedApdu, commandApdu)) {
//            String data = RNStorage.getValue(this.getApplicationContext(), RNStorage.HCE_ID);
//            byte[] accountBytes = data.getBytes();
//            return accountBytes;
//        } else {
//            return UNKNOWN_CMD_SW.getBytes();
//        }
    }

    @Override
    public void onDeactivated(int reason) {}


    public static byte[] hexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] buildSelectApdu(String aid) {
        return hexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }

    private String getAIDValue() {
        return RNStorage.getValue(this.getApplicationContext(), RNStorage.HCE_AID);
    }

    private byte[] getSelectedApdu() {
        return this.buildSelectApdu(this.getAIDValue());
    }
}
