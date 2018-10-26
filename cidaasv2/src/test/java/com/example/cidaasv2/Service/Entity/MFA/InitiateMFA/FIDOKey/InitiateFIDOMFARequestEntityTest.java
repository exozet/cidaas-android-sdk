package com.example.cidaasv2.Service.Entity.MFA.InitiateMFA.FIDOKey;

import com.example.cidaasv2.Helper.Entity.DeviceInfoEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InitiateFIDOMFARequestEntityTest {

    InitiateFIDOMFARequestEntity initiateFIDOMFARequestEntity;

    @Before
    public void setUp() {
        initiateFIDOMFARequestEntity=new InitiateFIDOMFARequestEntity();
    }



    @Test
    public void setPhysicalVerification()
    {
        initiateFIDOMFARequestEntity.setPhysicalVerificationId("physicalVerification");
        assertTrue(initiateFIDOMFARequestEntity.getPhysicalVerificationId().equals("physicalVerification"));
    }

    @Test
    public void getUsageType()
    {
        initiateFIDOMFARequestEntity.setUsageType("UsageType");
        assertTrue(initiateFIDOMFARequestEntity.getUsageType().equals("UsageType"));
    }

    @Test
    public void getVerificationType()
    {
        initiateFIDOMFARequestEntity.setVerificationType("VerificationType");
        assertTrue(initiateFIDOMFARequestEntity.getVerificationType().equals("VerificationType"));
    }

    @Test
    public void setSub()
    {
        initiateFIDOMFARequestEntity.setSub("Sub");
        assertTrue(initiateFIDOMFARequestEntity.getSub().equals("Sub"));
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

        initiateFIDOMFARequestEntity.setDeviceInfo(deviceInfoEntity);

        assertTrue(initiateFIDOMFARequestEntity.getDeviceInfo().getDeviceId().equals("deviceID"));
        assertTrue(initiateFIDOMFARequestEntity.getDeviceInfo().getDeviceMake().equals("deviceMake"));
        assertTrue(initiateFIDOMFARequestEntity.getDeviceInfo().getDeviceModel().equals("deviceModel"));
        assertTrue(initiateFIDOMFARequestEntity.getDeviceInfo().getDeviceVersion().equals("deviceVersion"));
        assertTrue(initiateFIDOMFARequestEntity.getDeviceInfo().getPushNotificationId().equals("push"));
    }

    @Test
    public void getUserDeviceId()
    {
        initiateFIDOMFARequestEntity.setUserDeviceId("UserDeveiceId");
        assertTrue(initiateFIDOMFARequestEntity.getUserDeviceId().equals("UserDeveiceId"));
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme