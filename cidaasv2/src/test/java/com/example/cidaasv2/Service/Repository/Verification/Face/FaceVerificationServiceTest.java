package com.example.cidaasv2.Service.Repository.Verification.Face;

import android.content.Context;

import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Service.Entity.MFA.AuthenticateMFA.Face.AuthenticateFaceRequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.AuthenticateMFA.Face.AuthenticateFaceResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.EnrollMFA.Face.EnrollFaceMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.EnrollMFA.Face.EnrollFaceMFAResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.InitiateMFA.Face.InitiateFaceMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.InitiateMFA.Face.InitiateFaceMFAResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.SetupMFA.Face.SetupFaceMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.SetupMFA.Face.SetupFaceMFAResponseEntity;
import com.example.cidaasv2.Service.Scanned.ScannedResponseEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import okhttp3.RequestBody;
@RunWith(RobolectricTestRunner.class)
public class FaceVerificationServiceTest {
    Context context;
    FaceVerificationService faceVerificationService;

    @Before
    public void setUp() {
        context= RuntimeEnvironment.application;
      faceVerificationService=new FaceVerificationService(context);
    }

    @Test
    public void testGetShared() throws Exception {
        FaceVerificationService result = FaceVerificationService.getShared(null);

        Assert.assertTrue(result instanceof FaceVerificationService);
    }

    @Test
    public void testSetupFaceMFA() throws Exception {

        faceVerificationService.setupFaceMFA("baseurl", "accessToken", "codeChallenge", new SetupFaceMFARequestEntity(), null,new Result<SetupFaceMFAResponseEntity>() {
            @Override
            public void success(SetupFaceMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testSetupFaceMFAdd() throws Exception {

        faceVerificationService.setupFaceMFA("", "accessToken", "codeChallenge", new SetupFaceMFARequestEntity(), null,new Result<SetupFaceMFAResponseEntity>() {
            @Override
            public void success(SetupFaceMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }
    @Test
    public void testScannedFace() throws Exception {

        faceVerificationService.scannedFace("baseurl", "usagePass", "statusId", "AccessToken", null,new Result<ScannedResponseEntity>() {
            @Override
            public void success(ScannedResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testStringtoRequestBody() throws Exception {
        RequestBody result = faceVerificationService.StringtoRequestBody("value");
        Assert.assertEquals(result instanceof RequestBody, true);
    }

    @Test
    public void testEnrollFace() throws Exception {

        faceVerificationService.enrollFace("baseurl", "accessToken", new EnrollFaceMFARequestEntity(), null,new Result<EnrollFaceMFAResponseEntity>() {
            @Override
            public void success(EnrollFaceMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testInitiateFace() throws Exception {

        faceVerificationService.initiateFace("baseurl", "codeChallenge", new InitiateFaceMFARequestEntity(), null,new Result<InitiateFaceMFAResponseEntity>() {
            @Override
            public void success(InitiateFaceMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testAuthenticateFace() throws Exception {

        faceVerificationService.authenticateFace("baseurl", new AuthenticateFaceRequestEntity(), null,new Result<AuthenticateFaceResponseEntity>() {
            @Override
            public void success(AuthenticateFaceResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme