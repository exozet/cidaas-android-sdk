package com.example.cidaasv2.Service.Repository.Deduplication;

import android.content.Context;

import com.example.cidaasv2.Helper.CommonError.CommonError;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.URLHelper.URLHelper;
import com.example.cidaasv2.R;
import com.example.cidaasv2.Service.CidaassdkService;
import com.example.cidaasv2.Service.Entity.Deduplication.DeduplicationResponseEntity;
import com.example.cidaasv2.Service.Entity.Deduplication.RegisterDeduplication.RegisterDeduplicationEntity;
import com.example.cidaasv2.Service.HelperForService.Headers.Headers;
import com.example.cidaasv2.Service.ICidaasSDKService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DeduplicationService {

    CidaassdkService service;
    private ObjectMapper objectMapper=new ObjectMapper();
    //Local variables

    private Context context;

    public static DeduplicationService shared;

    public DeduplicationService(Context contextFromCidaas) {

        context=contextFromCidaas;


        if(service==null) {
            service=new CidaassdkService(context);
        }

        //Todo setValue for authenticationType

    }

    String codeVerifier, codeChallenge;


    public static DeduplicationService getShared(Context contextFromCidaas )
    {
        try {

            if (shared == null) {
                shared = new DeduplicationService(contextFromCidaas);

            }

        }
        catch (Exception e)
        {
            Timber.i(e.getMessage());
        }
        return shared;
    }


    //todo log
    //----------------------------------------------------Get Deduplication info--------------------------------------------------
    public void getDeduplicationList(String baseurl,String trackId, final Result<DeduplicationResponseEntity> callback)
    {
        //Local Variables
        String methodName = "DeduplicationService :getDeduplicationList()";
        try{

            if(baseurl!=null && !baseurl.equals(""))
            {
                //Construct URL For RequestId

                //Todo Chnage URL Global wise
               String DeduplicationUrl=baseurl+ URLHelper.getShared().getDeduplicationList()+trackId;

               //Header Generation
               Map<String, String> headers = Headers.getShared(context).getHeaders(null,false,null);

               //Service call For DeduplicationList
               serviceCallForDeduplicationList(callback, DeduplicationUrl, headers);

            }
            else
            {
              callback.failure( WebAuthError.getShared(context).propertyMissingException(context.getString(R.string.EMPTY_BASE_URL_SERVICE),"Error :"+methodName));
              return;
            }
        }
        catch (Exception e)
        {
           callback.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,e.getMessage()));
        }
    }

    private void serviceCallForDeduplicationList(final Result<DeduplicationResponseEntity> callback, String deduplicationUrl, Map<String, String> headers)
    {
     final String methodName="DeduplicationService :serviceCallForDeduplicationList()";
        try {
           //Call Service-getRequestId
           ICidaasSDKService cidaasSDKService = service.getInstance();
           cidaasSDKService.getDeduplicationList(deduplicationUrl, headers).enqueue(new Callback<DeduplicationResponseEntity>() {
               @Override
               public void onResponse(Call<DeduplicationResponseEntity> call, Response<DeduplicationResponseEntity> response) {
                   if (response.isSuccessful()) {
                       if (response.code() == 200) {
                           callback.success(response.body());
                       } else {
                           callback.failure(WebAuthError.getShared(context).emptyResponseException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                  response.code(), "Error :"+methodName));
                       }
                   } else {
                       callback.failure(CommonError.getShared(context).generateCommonErrorEntity(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE, response
                               , "Error :"));
                   }
               }

               @Override
               public void onFailure(Call<DeduplicationResponseEntity> call, Throwable t) {
                   callback.failure(WebAuthError.getShared(context).serviceCallFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                           t.getMessage(), "Error :"+methodName));

               }
           });
       }
       catch (Exception e)
       {
          callback.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,e.getMessage()));
       }
    }

    //--------------------------------------------------Register Deduplication info------------------------------------------------
    public void registerDeduplication(String baseurl,String trackId, final Result<RegisterDeduplicationEntity> callback)
    {
        //Local Variables
        String methodName = "DeduplicationService :registerDeduplication()";
        try{

            if(baseurl!=null && !baseurl.equals("")){
                //Construct URL For RequestId

                //Todo Chnage URL Global wise
                String registerDeduplicationUrl=baseurl+ URLHelper.getShared().getRegisterdeduplication()+trackId;

                //Header Generation
                Map<String, String> headers = Headers.getShared(context).getHeaders(null,false,URLHelper.contentTypeJson);

                //ServiceCall
                serviceCallForRegisterDeduplication(registerDeduplicationUrl, headers, callback);
            }
            else {
                callback.failure(WebAuthError.getShared(context).propertyMissingException(context.getString(R.string.EMPTY_BASE_URL_SERVICE),"Error :"+methodName));
                return;
            }
        }
        catch (Exception e)
        {
         callback.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,e.getMessage()));
        }
    }

    private void serviceCallForRegisterDeduplication(String registerDeduplicationUrl,Map<String, String> headers,final Result<RegisterDeduplicationEntity> callback)
    {
        final String methodName = "";
        try {
            //Call Service-getRequestId
            ICidaasSDKService cidaasSDKService = service.getInstance();
            cidaasSDKService.registerDeduplication(registerDeduplicationUrl, headers).enqueue(new Callback<RegisterDeduplicationEntity>() {
                @Override
                public void onResponse(Call<RegisterDeduplicationEntity> call, Response<RegisterDeduplicationEntity> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            callback.success(response.body());
                        } else {
                            callback.failure(WebAuthError.getShared(context).emptyResponseException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    response.code(), "Error :"+methodName));
                        }
                    } else {
                        callback.failure(CommonError.getShared(context).generateCommonErrorEntity(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                response, "Error :"+methodName));
                    }
                }

                @Override
                public void onFailure(Call<RegisterDeduplicationEntity> call, Throwable t) {
                    callback.failure(WebAuthError.getShared(context).serviceCallFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                            t.getMessage(), "Error :"+methodName));

                }
            });
        }
        catch (Exception e)
        {
         callback.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,e.getMessage()));
        }
    }

}
