package de.cidaas.sdk.android.Service.Scanned;

import junit.framework.Assert;

import org.junit.Test;

import de.cidaas.sdk.android.service.scanned.ScannedResponseDataEntity;


public class ScannedResponseDataEntityTest {

    @Test
    public void setUserDeviceId() {
        ScannedResponseDataEntity scannedResponseDataEntity = new ScannedResponseDataEntity();

        scannedResponseDataEntity.setUserDeviceId("TestData");
        Assert.assertEquals("TestData", scannedResponseDataEntity.getUserDeviceId());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme