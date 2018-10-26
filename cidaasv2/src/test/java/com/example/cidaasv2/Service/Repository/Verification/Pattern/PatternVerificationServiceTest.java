package com.example.cidaasv2.Service.Repository.Verification.Pattern;

import android.content.Context;

import com.example.cidaasv2.Controller.Cidaas;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.Genral.DBHelper;
import com.example.cidaasv2.Service.Entity.MFA.AuthenticateMFA.Pattern.AuthenticatePatternRequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.AuthenticateMFA.Pattern.AuthenticatePatternResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.EnrollMFA.Pattern.EnrollPatternMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.EnrollMFA.Pattern.EnrollPatternMFAResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.InitiateMFA.Pattern.InitiatePatternMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.InitiateMFA.Pattern.InitiatePatternMFAResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.SetupMFA.Pattern.SetupPatternMFARequestEntity;
import com.example.cidaasv2.Service.Entity.MFA.SetupMFA.Pattern.SetupPatternMFAResponseEntity;
import com.example.cidaasv2.Service.Scanned.ScannedResponseEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Dictionary;
import java.util.Hashtable;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

@RunWith(RobolectricTestRunner.class)
public class PatternVerificationServiceTest {
    Context context;
    PatternVerificationService patternVerificationService;

    @Before
    public void setUp() {
        context= RuntimeEnvironment.application;
        patternVerificationService=new PatternVerificationService(context);
        DBHelper.setConfig(context);
        DBHelper.getShared().setFCMToken("FCM");
    }


    @Test
    public void testGetShared() throws Exception {
        PatternVerificationService result = PatternVerificationService.getShared(null);

        Assert.assertTrue(result instanceof PatternVerificationService);
    }


    @Test
    public void testSetupPatternMFA() throws Exception {

        patternVerificationService.setupPattern("baseurl", "accessToken","code",new SetupPatternMFARequestEntity(),null, new Result<SetupPatternMFAResponseEntity>() {
            @Override
            public void success(SetupPatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testSetupPatternNullMFA() throws Exception {

        patternVerificationService.setupPattern("", "accessToken","code",new SetupPatternMFARequestEntity(),null, new Result<SetupPatternMFAResponseEntity>() {
            @Override
            public void success(SetupPatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testSetupPatternFAILMFA() throws Exception {

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("/public-srv/Clientinfo/basic");
        server.enqueue(new MockResponse());


        Cidaas.baseurl=domainURL;


        Dictionary<String,String> loginproperties=new Hashtable<>();
        loginproperties.put("DomainURL","localhost:234235");
        loginproperties.put("ClientId","ClientId");
        loginproperties.put("RedirectURL","RedirectURL");


        patternVerificationService.setupPattern("localhost:234235", "accessToken","code",new SetupPatternMFARequestEntity(),null, new Result<SetupPatternMFAResponseEntity>() {
            @Override
            public void success(SetupPatternMFAResponseEntity result) {


            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e("Success");
            }
        });

    }

    @Test
    public void testScannedPatternMFA() throws Exception {

        patternVerificationService.scannedPattern("baseurl", "Usa","Status","Access",null,new Result<ScannedResponseEntity>() {
            @Override
            public void success(ScannedResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }
    @Test
    public void testScannedPatternNUllMFA() throws Exception {

        patternVerificationService.scannedPattern("", "Usa","Status","accessToken",null,new Result<ScannedResponseEntity>() {
            @Override
            public void success(ScannedResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testScannedPatternFAILMFA() throws Exception {

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("/public-srv/Clientinfo/basic");
        server.enqueue(new MockResponse());


        Cidaas.baseurl=domainURL;


        Dictionary<String,String> loginproperties=new Hashtable<>();
        loginproperties.put("DomainURL","localhost:234235");
        loginproperties.put("ClientId","ClientId");
        loginproperties.put("RedirectURL","RedirectURL");



        patternVerificationService.scannedPattern("localhost:234235", "Usa","StatusId","accessToken",null, new Result<ScannedResponseEntity>() {
            @Override
            public void success(ScannedResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e("Success");
            }
        });

    }


    @Test
    public void testEnrollPatternMFA() throws Exception {

        patternVerificationService.enrollPattern("baseurl", "accessToken", new EnrollPatternMFARequestEntity(), null,new Result<EnrollPatternMFAResponseEntity>() {
            @Override
            public void success(EnrollPatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }
    @Test
    public void testEnrollPatternNULLMFA() throws Exception {

        patternVerificationService.enrollPattern("", "accessToken", new EnrollPatternMFARequestEntity(), null,new Result<EnrollPatternMFAResponseEntity>() {
            @Override
            public void success(EnrollPatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testEnrollPatternFAILMFA() throws Exception {

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("/public-srv/Clientinfo/basic");
        server.enqueue(new MockResponse());


        Cidaas.baseurl=domainURL;


        Dictionary<String,String> loginproperties=new Hashtable<>();
        loginproperties.put("DomainURL","localhost:234235");
        loginproperties.put("ClientId","ClientId");
        loginproperties.put("RedirectURL","RedirectURL");



        patternVerificationService.enrollPattern("localhost:234235", "accessToken", new EnrollPatternMFARequestEntity(),null, new Result<EnrollPatternMFAResponseEntity>() {
            @Override
            public void success(EnrollPatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e("Success");
            }
        });

    }

    @Test
    public void testInitiatePattern() throws Exception {

        patternVerificationService.initiatePattern("baseurl", "sd",new InitiatePatternMFARequestEntity(),null, new Result<InitiatePatternMFAResponseEntity>() {
            @Override
            public void success(InitiatePatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testInitiatePatternNULLMFA() throws Exception {

        patternVerificationService.initiatePattern("", "sd",new InitiatePatternMFARequestEntity(),null, new Result<InitiatePatternMFAResponseEntity>() {
            @Override
            public void success(InitiatePatternMFAResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testInitiatePatternFAILMFA() throws Exception {

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("/public-srv/Clientinfo/basic");
        server.enqueue(new MockResponse());


        Cidaas.baseurl=domainURL;


        Dictionary<String,String> loginproperties=new Hashtable<>();
        loginproperties.put("DomainURL","localhost:234235");
        loginproperties.put("ClientId","ClientId");
        loginproperties.put("RedirectURL","RedirectURL");



        patternVerificationService.initiatePattern("localhost:234235", "sd",new InitiatePatternMFARequestEntity(),null, new Result<InitiatePatternMFAResponseEntity>() {
            @Override
            public void success(InitiatePatternMFAResponseEntity result) {


            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e("Success");
            }
        });

    }

    @Test
    public void testAuthenticatePattern() throws Exception {

        patternVerificationService.authenticatePattern("baseurl", new AuthenticatePatternRequestEntity(),null, new Result<AuthenticatePatternResponseEntity>() {
            @Override
            public void success(AuthenticatePatternResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }

    @Test
    public void testAuthenticatePatternNULLMFA() throws Exception {

        patternVerificationService.authenticatePattern("", new AuthenticatePatternRequestEntity(),null, new Result<AuthenticatePatternResponseEntity>() {
            @Override
            public void success(AuthenticatePatternResponseEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }


    @Test
    public void testAuthenticatePatternFAILMFA() throws Exception {

        MockWebServer server = new MockWebServer();
        String domainURL= server.url("").toString();
        server.url("/public-srv/Clientinfo/basic");
        server.enqueue(new MockResponse());


        Cidaas.baseurl=domainURL;


        Dictionary<String,String> loginproperties=new Hashtable<>();
        loginproperties.put("DomainURL","localhost:234235");
        loginproperties.put("ClientId","ClientId");
        loginproperties.put("RedirectURL","RedirectURL");



        patternVerificationService.authenticatePattern("localhost:234235", new AuthenticatePatternRequestEntity(),null, new Result<AuthenticatePatternResponseEntity>() {
            @Override
            public void success(AuthenticatePatternResponseEntity result) {


            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e("Success");
            }
        });

    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme