package com.example.cidaasv2.Service.Repository.Login;

import android.content.Context;

import com.example.cidaasv2.Controller.Cidaas;
import com.example.cidaasv2.Helper.Entity.DeviceInfoEntity;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Service.CidaassdkService;
import com.example.cidaasv2.Service.Entity.LoginCredentialsEntity.LoginCredentialsRequestEntity;
import com.example.cidaasv2.Service.Entity.LoginCredentialsEntity.LoginCredentialsResponseEntity;
import com.example.cidaasv2.Service.Entity.LoginCredentialsEntity.ResumeLogin.ResumeLoginRequestEntity;
import com.example.cidaasv2.Service.Entity.LoginCredentialsEntity.ResumeLogin.ResumeLoginResponseEntity;
import com.example.cidaasv2.Service.Repository.RequestId.RequestIdService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

import static com.example.cidaasv2.Controller.HelperClass.removeLastChar;

public class LoginCredentialsServiceCall {

    Context context= Mockito.mock(Context.class);
    LoginService loginService=new LoginService(context);
    DeviceInfoEntity deviceInfoEntity=new DeviceInfoEntity();

    LoginCredentialsRequestEntity loginCredentialsRequestEntity=new LoginCredentialsRequestEntity();
    ResumeLoginRequestEntity resumeLoginRequestEntity=new ResumeLoginRequestEntity();


    CountDownLatch latch=new CountDownLatch(1);


    @Before
    public void setUp() throws Exception {

        deviceInfoEntity.setDeviceId("DeviceID");
        deviceInfoEntity.setDeviceVersion("DeviceVersion");
        deviceInfoEntity.setDeviceModel("DeviceModel");
        deviceInfoEntity.setDeviceMake("DeviceMake");
        deviceInfoEntity.setPushNotificationId("PushNotificationId");

        loginCredentialsRequestEntity.setRequestId("RequestId");
        loginCredentialsRequestEntity.setUsername_type("Username_type");
        loginCredentialsRequestEntity.setUsername("Username");
        loginCredentialsRequestEntity.setPassword("Password");


        resumeLoginRequestEntity.setTrack_id("TrackId");
        resumeLoginRequestEntity.setUserDeviceId("UserDeviceId");
        resumeLoginRequestEntity.setUsageType("UsageType");
        resumeLoginRequestEntity.setVerificationType("VerificationType");
        resumeLoginRequestEntity.setTrackingCode("TrackingCode");
        resumeLoginRequestEntity.setClient_id("ClientID");
        resumeLoginRequestEntity.setSub("Sub");
        resumeLoginRequestEntity.setRequestId("RequestID");

    }

    @Test
    public void testLoginWithCredentials() throws Exception {



        final MockResponse response = new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("{\n" +
                        "    \"success\": true,\n" +
                        "    \"status\": 200,\n" +
                        "    \"data\": {\n" +
                        "    \"requestId\": \"556b95f8-9326-4250-a4ee-0e0d887f7a7d\""+
                        "    }\n" +
                        "}");

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("login-srv/login/sdk");



        server.enqueue(response);
        Cidaas.baseurl=domainURL;

        LoginCredentialsRequestEntity loginCredentialsRequestEntity=new LoginCredentialsRequestEntity();
        loginCredentialsRequestEntity.setRequestId("RequestId");
        loginCredentialsRequestEntity.setUsername_type("Username_type");
        loginCredentialsRequestEntity.setUsername("Username");
        loginCredentialsRequestEntity.setPassword("Password");

        loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl), loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
            @Override
            public void success(LoginCredentialsResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }





    @Test
    public void testLoginWithCredentialsError() throws Exception {



        final MockResponse response = new MockResponse().setResponseCode(417)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("  {\n" +
                        "        \"success\": false,\n" +
                        "            \"status\": 417,\n" +
                        "            \"error\": {\n" +
                        "        \"code\": 507,\n" +
                        "                \"moreInfo\": \"\",\n" +
                        "                \"type\": \"LoginException\",\n" +
                        "                \"status\": 417,\n" +
                        "                \"referenceNumber\": \"1537426534732\",\n" +
                        "                \"error\": \"requestId or username or password in body must not be empty\"\n" +
                        "    }\n" +
                        "    }");

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("login-srv/login/sdk");



        server.enqueue(response);
        Cidaas.baseurl=domainURL;

        loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl), new LoginCredentialsRequestEntity(),deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
            @Override
            public void success(LoginCredentialsResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testWebClient() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "    \"access_token\": \"Sample_Access_Token\""+
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;



            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {

                    Assert.assertEquals("Sample_Access_Token",result.getData().getAccess_token());

                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Cidaas developer",error.getErrorMessage());
                    latch.countDown();
                }
            });
            latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }



    @Test
    public void testWebClientFor202() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(202)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Raja Developers\",\n" +
                            "        \"allowLoginWith\": [\n" +
                            "            \"EMAIL\",\n" +
                            "            \"MOBILE\",\n" +
                            "            \"USER_NAME\"\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {
                    Assert.assertEquals("get Access Token",result.getData().getAccess_token());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Service failure but successful response",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testWebClientFor401() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"referenceNumber\": \"1537337364806\",\n" +
                            "                        \"error\": \"Invalid tenant\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getAccess_token());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }

    @Test
    public void testWebClientFaliureError() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"referenceNumber\": \"1537337364806\",\n" +
                            "                        \"error\": \"Invalid Login id\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getAccess_token());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testWebClientFaliureException() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant \n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getAccess_token());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testWebClientFaliureExcn() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant\"\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.loginWithCredentials(removeLastChar(Cidaas.baseurl),loginCredentialsRequestEntity,deviceInfoEntity, new Result<LoginCredentialsResponseEntity>() {
                @Override
                public void success(LoginCredentialsResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getAccess_token());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }



    @Test
    public void testContinueMFA() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "    \"code\": \"Code\""+
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;



            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {
                   Assert.assertEquals("Code",result.getData().getCode());
                    latch.countDown();
                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Error",error.getErrorMessage());
                    latch.countDown();
                }
            });
            latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }



    @Test
    public void testContinueFor202() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(202)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Raja Developers\",\n" +
                            "        \"allowLoginWith\": [\n" +
                            "            \"EMAIL\",\n" +
                            "            \"MOBILE\",\n" +
                            "            \"USER_NAME\"\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {
                    Assert.assertEquals("Code",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Service failure but successful response",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinueFor401() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"referenceNumber\": \"1537337364806\",\n" +
                            "                        \"error\": \"Invalid tenant\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }

    @Test
    public void testContinueError() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"referenceNumber\": \"1537337364806\",\n" +
                            "                        \"error\": \"Invalid Login id\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinueMFAException() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant \n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }

    @Test
    public void testContinueMFAExcep() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant\" \n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continueMFA(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinuePasswordless() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "    \"code\": \"Code\""+
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;



            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {
                    Assert.assertEquals("Code",result.getData().getCode());
                    latch.countDown();
                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Error",error.getErrorMessage());
                    latch.countDown();
                }
            });
            latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }



    @Test
    public void testContinueForPasswordless202() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(202)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Raja Developers\",\n" +
                            "        \"allowLoginWith\": [\n" +
                            "            \"EMAIL\",\n" +
                            "            \"MOBILE\",\n" +
                            "            \"USER_NAME\"\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {
                    Assert.assertEquals("Code",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Service failure but successful response",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinueForPasswordless401() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"referenceNumber\": \"1537337364806\",\n" +
                            "                        \"error\": \"Invalid tenant\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }

    @Test
    public void testContinueForPasswordlessError() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "                        \"error_description\": \"Error\",\n" +
                            "   \"error\": {\n" +
                            "                \"code\": 24008,\n" +
                            "                        \"moreInfo\": \"\",\n" +
                            "                        \"type\": \"LoginException\",\n" +
                            "                        \"status\": 400,\n" +
                            "                        \"error\": \"Invalid Login id\"\n" +
                            "            }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinueForPasswordlessException() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant \n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


    @Test
    public void testContinueForPasswordlessE() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(401)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": false,\n" +
                            "    \"status\": 401,\n" +
                            "     \"error\": \"Invalid tenant\" \n" +
                            "}");

            MockWebServer server = new MockWebServer();
            String domainURL= server.url("").toString();
            server.url("/public-srv/tenantinfo/basic");



            server.enqueue(response);
            Cidaas.baseurl=domainURL;

            loginService.continuePasswordless(removeLastChar(Cidaas.baseurl), resumeLoginRequestEntity, deviceInfoEntity, new Result<ResumeLoginResponseEntity>() {
                @Override
                public void success(ResumeLoginResponseEntity result) {

                    Assert.assertEquals("Service failure but successful response",result.getData().getCode());
                    latch.countDown();

                }

                @Override
                public void failure(WebAuthError error) {
                    Assert.assertEquals("Invalid tenant",error.getErrorMessage());
                    latch.countDown();
                }
            });
            //latch.await();
            //Thread.sleep(3000);
            Timber.e("Success");

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }


}