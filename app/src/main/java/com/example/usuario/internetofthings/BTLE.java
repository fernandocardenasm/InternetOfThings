package com.example.usuario.internetofthings;

/**
 * Created by usuario on 05/06/2015.
 */
public class BTLE {
    private String mDeviceAddress;
    private String mFriendlyName;

    public BTLE() {
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        mDeviceAddress = deviceAddress;
    }

    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        mFriendlyName = friendlyName;
    }
}
