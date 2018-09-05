package com.example.cidaasv2.Service.Entity.MFA.AuthenticateMFA.Face;

import com.example.cidaasv2.Helper.Entity.DeviceInfoEntity;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertTrue;


public class AuthenticateFaceRequestEntityTest {

    File imagetoSend;

    DeviceInfoEntity deviceInfo;

    AuthenticateFaceRequestEntity authenticateFaceRequestEntity;

    @Before
    public void setUp() {
      authenticateFaceRequestEntity=new AuthenticateFaceRequestEntity();
    }

    @Test
    public void getStatusID()
    {
        authenticateFaceRequestEntity.setStatusId("Status_ID");
        assertTrue(authenticateFaceRequestEntity.getStatusId().equals("Status_ID"));
    }

    @Test
    public void getImagetoSend()
    {
        File imgFile=new File("");
        authenticateFaceRequestEntity.setImagetoSend(imgFile);
        assertTrue(authenticateFaceRequestEntity.getImagetoSend().equals(imgFile));
    }


    @Test
    public void getDeviceInfoEntity()
    {
        DeviceInfoEntity deviceInfoEntity=new DeviceInfoEntity();
        deviceInfoEntity.setPushNotificationId("push");
        deviceInfoEntity.setDeviceId("deviceID");
        deviceInfoEntity.setDeviceMake("deviceMake");
        deviceInfoEntity.setDeviceModel("deviceModel");
        deviceInfoEntity.setDeviceVersion("deviceVersion");

        authenticateFaceRequestEntity.setDeviceInfo(deviceInfo);

/*        assertTrue(authenticateFaceRequestEntity.getDeviceInfo().getDeviceId().equals("deviceID"));
        assertTrue(authenticateFaceRequestEntity.getDeviceInfo().getDeviceMake().equals("deviceMake"));
        assertTrue(authenticateFaceRequestEntity.getDeviceInfo().getDeviceModel().equals("deviceModel"));
        assertTrue(authenticateFaceRequestEntity.getDeviceInfo().getDeviceVersion().equals("deviceVersion"));
        assertTrue(authenticateFaceRequestEntity.getDeviceInfo().getPushNotificationId().equals("push"));*/
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme