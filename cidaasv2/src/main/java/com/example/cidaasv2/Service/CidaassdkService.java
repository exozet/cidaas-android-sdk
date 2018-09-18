package com.example.cidaasv2.Service;

import com.example.cidaasv2.Controller.Cidaas;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by widasrnarayanan on 17/1/18.
 */

public class CidaassdkService {

    public ICidaasSDKService getInstance()
    {

        String baseurl= Cidaas.baseurl;

        if(baseurl==null || baseurl.equals(""))
        {
            baseurl="https://www.google.com";
        }



        ICidaasSDKService iCidaasSDKService=null;
        OkHttpClient okHttpClient=null;

        // HttpClient
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit=new Retrofit.Builder()
               // .baseUrl(DBHelper.getShared().getLoginProperties().get("DomainURL"))
                .baseUrl(baseurl)//done Get Base URL
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();
          iCidaasSDKService=retrofit.create(ICidaasSDKService.class);
        return iCidaasSDKService;
    }

}
