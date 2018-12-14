package com.example.cidaasv2.Service.Repository.Tenant;

import android.content.Context;

import com.example.cidaasv2.Controller.Cidaas;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Service.CidaassdkService;
import com.example.cidaasv2.Service.Entity.TenantInfo.TenantInfoDataEntity;
import com.example.cidaasv2.Service.Entity.TenantInfo.TenantInfoEntity;
import com.example.cidaasv2.util.AuthenticationAPI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.CountDownLatch;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import timber.log.Timber;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;


@RunWith(RobolectricTestRunner.class)
public class TenantServiceTest {

    CidaassdkService service;
    Context context;
    TenantService tenantService;

    AuthenticationAPI mockAPI;

    @Mock
    CidaassdkService service1;
    private final CountDownLatch latch = new CountDownLatch(1);


    @Before
    public void setUp() throws Exception{
        context= RuntimeEnvironment.application;
        service=new CidaassdkService();
        MockitoAnnotations.initMocks(this);
        tenantService=new TenantService(context);
        mockAPI=new AuthenticationAPI();


    }

    @Test
    public void testGetShared() throws Exception {
        Context context=null;
        TenantService result = TenantService.getShared(context);
        Assert.assertThat(new TenantService(context), samePropertyValuesAs(result));
    }


    @Test
    public void testGetTenantInfo() throws Exception {

        tenantService.getTenantInfo("baseurl", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {
                Timber.e(result.getData().getTenant_name());
            }

            @Override
            public void failure(WebAuthError error) {
                Timber.e(error.ErrorMessage);
            }
        });
    }

    @Test
    public void testGetTenantInfoForEmptyBaseurl() throws Exception {
       TenantService sam=new TenantService(context);

        tenantService.getTenantInfo("", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {
                Timber.e(result.getData().getTenant_name());
            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e(error.ErrorMessage);
            }
        });
    }

    @Test
    public void testGetTenantInfoForEmptyurl() throws Exception {

      TenantService tenantService1=Mockito.mock(TenantService.class);
        /*    when(service.getInstance()).thenThrow(new NullPointerException());
       */

       // URLHelper.getShared()=Mockito.mock(URLHelper.class);

      //  when(URLHelper.getShared()).thenThrow(new NullPointerException());

        doThrow(new NullPointerException()).when(tenantService1).getTenantInfo("base", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {

            }

            @Override
            public void failure(WebAuthError error) {

            }
        });



        tenantService1.getTenantInfo("base", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {
                Timber.e(result.getData().getTenant_name());
            }

            @Override
            public void failure(WebAuthError error) {

                Timber.e(error.ErrorMessage);
            }
        });
    }


    @Test
    public void testGetTenantInfoForSuccess() throws Exception {
        TenantService tenantService123=Mockito.mock(TenantService.class);

        TenantInfoEntity tenantInfoEntity=new TenantInfoEntity();
        TenantInfoDataEntity tenantInfoDataEntity=new TenantInfoDataEntity();
        tenantInfoDataEntity.setTenant_name("TenantName");
        tenantInfoEntity.setData(tenantInfoDataEntity);
        tenantInfoEntity.setStatus(200);
        tenantInfoEntity.setSuccess(true);


        tenantService123.getTenantInfo("https://nightlybuild.cidaas.de", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {
             Assert.assertEquals("TenantName",result.getData().getTenant_name());
            }

            @Override
            public void failure(WebAuthError error) {

            }
        });
    }


    @Test
    public void testGetTenantInfoForFailure() throws Exception {
        try{
        final MockResponse response = new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("\"{\n" +
                        "    \"success\": true,\n" +
                        "    \"status\": 401,\n" +
                        "    \"error\": {\n" +
                        "        \"tenant_name\": \"Cidaas Developers\",\n" +
                        "\"errorMessage\":\"Mustnot be null\""+
                        "        \"allowLoginWith\": [\n" +
                        "            \"EMAIL\",\n" +
                        "            \"MOBILE\",\n" +
                        "            \"USER_NAME\"\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}\"");

        MockWebServer server = new MockWebServer();
            server.start(2716);
        String domainURL= server.url("").toString();


            server.shutdown();

        server.url("/public-srv/tenantinfo/basic");



        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                if (request.getPath().equals("/public-srv/tenantinfo/basic")){
                    return new MockResponse().setResponseCode(200).setBody("\"{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Cidaas Developers\",\n" +
                            "        \"allowLoginWith\": [\n" +
                            "            \"EMAIL\",\n" +
                            "            \"MOBILE\",\n" +
                            "            \"USER_NAME\"\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}\"");
                } else if (request.getPath().equals("v1/check/version/")){
                    return response;
                } else if (request.getPath().equals("/v1/profile/info")) {
                    return new MockResponse().setResponseCode(200).setBody("{\\\"info\\\":{\\\"name\":\"Lucas Albuquerque\",\"age\":\"21\",\"gender\":\"male\"}}");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);

        Cidaas.baseurl="https://"+server.getHostName()+":2716/";
        //server.start();


        //  RecordedRequest request=server.takeRequest();
        //       request.getBody();
        //  Assert.assertEquals("/public-srv/tenantinfo/basic",request.getRequestLine());

        tenantService.getTenantInfo("https://localhost:2716", new Result<TenantInfoEntity>() {
            @Override
            public void success(TenantInfoEntity result) {
                Assert.assertEquals("Cidaas developer",result.getData().getTenant_name());
            }

            @Override
            public void failure(WebAuthError error) {
                //   Assert.assertEquals("Cidaas developer",error.getErrorMessage());
            }
        });




        server.getHostName();

    }
       catch (Exception e)
    {
        Assert.assertEquals(e.getMessage(),true,true);
        Assert.assertFalse(e.getMessage(),true);
    }


}

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    @Test
    public void testWeb() throws Exception{
        try{



            mockAPI.getDomain();
            mockAPI.willReturnTenant();
            Cidaas.baseurl=removeLastChar(mockAPI.getDomain());

         //  tenantService.getTenantInfo();




        }
        catch (Exception e){
            Timber.e(e.getMessage());
        }
    }



    @Test
    public void testWebClients() throws  Exception{

        try {

            context=null;

            final MockResponse response = new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Cidaas Developers\",\n" +
                            "        \"allowLoginWith\": [\n" +
                            "            \"EMAIL\",\n" +
                            "            \"MOBILE\",\n" +
                            "            \"USER_NAME\"\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}");

            MockWebServer server = new MockWebServer();
            server.shutdown();
            server.start(40806);
            server.url("/public-srv/tenantinfo/basic");



            final Dispatcher dispatcher = new Dispatcher() {

                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                    if (request.getPath().equals("/public-srv/tenantinfo/basic")){
                        return response;
                    } else if (request.getPath().equals("v1/check/version/")){
                        return response;
                    } else if (request.getPath().equals("/v1/profile/info")) {
                        return new MockResponse().setResponseCode(200).setBody("{\\\"info\\\":{\\\"name\":\"Lucas Albuquerque\",\"age\":\"21\",\"gender\":\"male\"}}");
                    }
                    return new MockResponse().setResponseCode(404);
                }
            };
            server.setDispatcher(dispatcher);

            Cidaas.baseurl="https://"+server.getHostName()+":4006/";

            tenantService.getTenantInfo("localhost:4006", new Result<TenantInfoEntity>() {
                @Override
                public void success(TenantInfoEntity result) {
                    Assert.assertEquals("Raja Developer",result.getData().getTenant_name());
                }

                @Override
                public void failure(WebAuthError error) {
                    //   Assert.assertEquals("Cidaas developer",error.getErrorMessage());
                }
            });




            server.getHostName();

        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }




    @Test
    public void testWebClientWihLatch() throws  Exception{

        try {
            Timber.e("Success");

            final MockResponse response = new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody("{\n" +
                            "    \"success\": true,\n" +
                            "    \"status\": 200,\n" +
                            "    \"data\": {\n" +
                            "        \"tenant_name\": \"Cidaas Developers\",\n" +
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

           tenantService.getTenantInfo(removeLastChar(Cidaas.baseurl), new Result<TenantInfoEntity>() {
               @Override
               public void success(TenantInfoEntity result) {
                   latch.countDown();
                   Assert.assertEquals("Raja Developers",result.getData().getTenant_name());
               }

               @Override
               public void failure(WebAuthError error) {
                   latch.countDown();
                   Assert.assertEquals("Cidaas developer",error.getErrorMessage());
               }
           });
           // latch.await();
            Thread.sleep(3000);



            // final String responsebody=stringresponse(new OkHttpClient(),Cidaas.baseurl+"public-srv/tenantinfo/basic");



        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getMessage(),true,true);
            Assert.assertFalse(e.getMessage(),true);
        }

    }



}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
