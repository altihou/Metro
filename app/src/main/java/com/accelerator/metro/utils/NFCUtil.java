package com.accelerator.metro.utils;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

/**
 * Created by Nicholas on 2016/8/4.
 */
public class NFCUtil {

     NFCUtil() {
        throw new RuntimeException("Stub!");
    }

    public static boolean hasNFC(Context context) {
        boolean bRet = false;

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if (adapter != null) {
            bRet = true;
        }

        return bRet;
    }

    public static boolean isOpen(Context context) {
        boolean state = false;

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if (adapter != null && adapter.isEnabled()) {
            state = true;
        }

        return state;
    }


}
