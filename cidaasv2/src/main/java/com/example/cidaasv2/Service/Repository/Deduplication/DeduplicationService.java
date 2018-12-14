package com.example.cidaasv2.Service.Repository.Deduplication;

import android.content.Context;

import com.example.cidaasv2.Helper.Entity.CommonErrorEntity;
import com.example.cidaasv2.Helper.Entity.ErrorEntity;
import com.example.cidaasv2.Helper.Enums.HttpStatusCode;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.URLHelper.URLHelper;
import com.example.cidaasv2.R;
import com.example.cidaasv2.Service.CidaassdkService;
import com.example.cidaasv2.Service.Entity.Deduplication.DeduplicationResponseEntity;
import com.example.cidaasv2.Service.Entity.Deduplication.RegisterDeduplication.RegisterDeduplicationEntity;
import com.example.cidaasv2.Service.ICidaasSDKService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Hashtable;
import java.util.LinkedHashMap;
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
            service=new CidaassdkService();
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


    //Get Deduplication info
    public void getDeduplicationList(String baseurl,String trackId, final Result<DeduplicationResponseEntity> callback)
    {
        //Local Variables
        String DeduplicationUrl = "";
        try{

            if(baseurl!=null && baseurl!=""){
                //Construct URL For RequestId

                //Todo Chnage URL Global wise
                DeduplicationUrl=baseurl+ URLHelper.getShared().getDeduplicationList()+trackId;
            }
            else {
                callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.PROPERTY_MISSING,
                        context.getString(R.string.PROPERTY_MISSING), 400,null,null));
                return;
            }

            //Call Service-getRequestId
            ICidaasSDKService cidaasSDKService = service.getInstance();
            cidaasSDKService.getDeduplicationList(DeduplicationUrl).enqueue(new Callback<DeduplicationResponseEntity>() {
                @Override
                public void onResponse(Call<DeduplicationResponseEntity> call, Response<DeduplicationResponseEntity> response) {
                    if (response.isSuccessful()) {
                        if(response.code()==200) {
                            callback.success(response.body());
                        }
                        else {
                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    "Service failure but successful response" , 400,null,null));
                        }
                    }
                    else {
                        assert response.errorBody() != null;
                        try {

                            //Todo Handle proper error message
                            String errorResponse=response.errorBody().source().readByteString().utf8();

                            CommonErrorEntity commonErrorEntity;
                            commonErrorEntity=objectMapper.readValue(errorResponse,CommonErrorEntity.class);


                            String errorMessage="";
                            ErrorEntity errorEntity=new ErrorEntity();
                            if(commonErrorEntity.getError()!=null && !commonErrorEntity.getError().toString().equals("")
                                    && commonErrorEntity.getError() instanceof  String) {
                                errorMessage=commonErrorEntity.getError().toString();
                            }
                            else
                            {
                                errorMessage = ((LinkedHashMap) commonErrorEntity.getError()).get("error").toString();
                                errorEntity.setCode(((LinkedHashMap) commonErrorEntity.getError()).get("code").toString());
                                errorEntity.setError( ((LinkedHashMap) commonErrorEntity.getError()).get("error").toString());
                                errorEntity.setMoreInfo( ((LinkedHashMap) commonErrorEntity.getError()).get("moreInfo").toString());
                                errorEntity.setReferenceNumber( ((LinkedHashMap) commonErrorEntity.getError()).get("referenceNumber").toString());
                                errorEntity.setStatus((Integer) ((LinkedHashMap) commonErrorEntity.getError()).get("status"));
                                errorEntity.setType( ((LinkedHashMap) commonErrorEntity.getError()).get("type").toString());
                            }

                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    errorMessage, commonErrorEntity.getStatus(),
                                    commonErrorEntity.getError(),errorEntity));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    "Deduplication Exception:"+ e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
                        }
                        Timber.e("response"+response.message());
                    }
                }

                @Override
                public void onFailure(Call<DeduplicationResponseEntity> call, Throwable t) {
                    Timber.e("Faliure in Request id service call"+t.getMessage());
                    callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                            t.getMessage(), 400,null,null));

                }
            });
        }
        catch (Exception e)
        {
            Timber.d(e.getMessage());
            callback.failure(WebAuthError.getShared(context).propertyMissingException());
        }
    }

    //Register Deduplication info
    public void registerDeduplication(String baseurl,String trackId, final Result<RegisterDeduplicationEntity> callback)
    {
        //Local Variables
        String registerDeduplicationUrl = "";
        try{

            if(baseurl!=null && baseurl!=""){
                //Construct URL For RequestId

                //Todo Chnage URL Global wise
                registerDeduplicationUrl=baseurl+ URLHelper.getShared().getRegisterdeduplication()+trackId;
            }
            else {
                callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.PROPERTY_MISSING,
                        context.getString(R.string.PROPERTY_MISSING), 400,null,null));
                return;
            }



            Map<String, String> headers = new Hashtable<>();

            headers.put("Content-Type", URLHelper.contentTypeJson);
            headers.put("user-agent", "cidaas-android");


            //Call Service-getRequestId
            ICidaasSDKService cidaasSDKService = service.getInstance();
            cidaasSDKService.registerDeduplication(registerDeduplicationUrl,headers).enqueue(new Callback<RegisterDeduplicationEntity>() {
                @Override
                public void onResponse(Call<RegisterDeduplicationEntity> call, Response<RegisterDeduplicationEntity> response) {
                    if (response.isSuccessful()) {
                        if(response.code()==200) {
                            callback.success(response.body());
                        }
                        else {
                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    "Service failure but successful response" , 400,null,null));
                        }
                    }
                    else {
                        assert response.errorBody() != null;
                        try {

                            //Todo Handle proper error message
                            String errorResponse=response.errorBody().source().readByteString().utf8();

                            CommonErrorEntity commonErrorEntity;
                            commonErrorEntity=objectMapper.readValue(errorResponse,CommonErrorEntity.class);


                            String errorMessage="";
                            ErrorEntity errorEntity=new ErrorEntity();
                            if(commonErrorEntity.getError()!=null && !commonErrorEntity.getError().toString().equals("")
                                    && commonErrorEntity.getError() instanceof  String) {
                                errorMessage=commonErrorEntity.getError().toString();
                            }
                            else
                            {
                                errorEntity.setCode( ((LinkedHashMap) commonErrorEntity.getError()).get("code").toString());
                                errorEntity.setError( ((LinkedHashMap) commonErrorEntity.getError()).get("error").toString());
                                errorEntity.setMoreInfo( ((LinkedHashMap) commonErrorEntity.getError()).get("moreInfo").toString());
                                errorEntity.setReferenceNumber( ((LinkedHashMap) commonErrorEntity.getError()).get("referenceNumber").toString());
                                errorEntity.setStatus((Integer) ((LinkedHashMap) commonErrorEntity.getError()).get("status"));
                                errorEntity.setType( ((LinkedHashMap) commonErrorEntity.getError()).get("type").toString());
                            }

                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    errorMessage, commonErrorEntity.getStatus(),
                                    commonErrorEntity.getError(),errorEntity));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    "Deduplication Exception:"+ e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
                        }
                        Timber.e("response"+response.message());
                    }
                }

                @Override
                public void onFailure(Call<RegisterDeduplicationEntity> call, Throwable t) {
                    Timber.e("Faliure in Request id service call"+t.getMessage());
                    callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                            t.getMessage(), 400,null,null));

                }
            });
        }
        catch (Exception e)
        {
            Timber.d(e.getMessage());
            callback.failure(WebAuthError.getShared(context).propertyMissingException());
        }
    }
















   /* //Login Deduplication
    public void loginDeduplication(final String baseurl, LoginEntity loginEntity, final Result<LoginCredentialsResponseEntity> callback)
    {

        String loginUrl = "";
        try{

            if(baseurl!=null || baseurl!=""){
                //Construct URL For RequestId
                loginUrl=baseurl+ URLHelper.getShared().getLoginWithCredentials();
            }
            else {
                callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.PROPERTY_MISSING,
                        context.getString(R.string.PROPERTY_MISSING), 400,null,null));
                return;
            }


            Map<String, String> headers = new Hashtable<>();
            // Get Device Information
            DeviceInfoEntity deviceInfoEntity = DBHelper.getShared().getDeviceInfo();

            //Todo - check Construct Headers pending,Null Checking Pending
            //Add headers
            headers.put("Content-Type", URLHelper.contentTypeJson);
            headers.put("user-agent", "cidaas-android");
            headers.put("deviceId", deviceInfoEntity.getDeviceId());
            headers.put("deviceMake", deviceInfoEntity.getDeviceMake());
            headers.put("deviceModel", deviceInfoEntity.getDeviceModel());
            headers.put("deviceVersion", deviceInfoEntity.getDeviceVersion());


            //Call Service-getRequestId
            final ICidaasSDKService cidaasSDKService = service.getInstance();

            cidaasSDKService.logindeDuplicatopm(loginUrl,headers, loginEntity).enqueue(new Callback<LoginCredentialsResponseEntity>() {
                @Override
                public void onResponse(Call<LoginCredentialsResponseEntity> call, Response<LoginCredentialsResponseEntity> response) {
                    if (response.isSuccessful()) {
                        if(response.code()==200) {
                            callback.success(response.body());
                        }
                        else {
                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.LOGINWITH_CREDENTIALS_FAILURE,
                                    "Service failure but successful response" , response.code(),null,null));
                        }
                    }
                    else {
                        assert response.errorBody() != null;
                        try {

                            String errorResponse=response.errorBody().source().readByteString().utf8();

                            CommonErrorEntity commonErrorEntity;
                            commonErrorEntity=objectMapper.readValue(errorResponse,CommonErrorEntity.class);


                            String errorMessage="";
                            if(commonErrorEntity.getError()!=null && !commonErrorEntity.getError().toString().equals("")
                                    && commonErrorEntity.getError() instanceof  String) {
                                errorMessage=commonErrorEntity.getError().toString();
                            }
                            else
                            {
                                errorMessage = ((LinkedHashMap) commonErrorEntity.getError()).get("error").toString();
                                errorEntity.setCode((Integer) ((LinkedHashMap) commonErrorEntity.getError()).get("code"));
                                errorEntity.setError( ((LinkedHashMap) commonErrorEntity.getError()).get("error").toString());
                                errorEntity.setMoreInfo( ((LinkedHashMap) commonErrorEntity.getError()).get("moreInfo").toString());
                                errorEntity.setReferenceNumber( ((LinkedHashMap) commonErrorEntity.getError()).get("referenceNumber").toString());
                                errorEntity.setStatus((Integer) ((LinkedHashMap) commonErrorEntity.getError()).get("status"));
                                errorEntity.setType( ((LinkedHashMap) commonErrorEntity.getError()).get("type").toString());
                            }

                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.DEDUPLICATION_LIST_FAILURE,
                                    errorMessage, commonErrorEntity.getStatus(),
                                    commonErrorEntity.getError(),errorEntity));

                        } catch (Exception e) {
                            callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.LOGINWITH_CREDENTIALS_FAILURE,e.getMessage(), 400,null,null));
                            Timber.e("response"+response.message()+e.getMessage());
                        }
                        Timber.e("response"+response.message());
                    }
                }

                @Override
                public void onFailure(Call<LoginCredentialsResponseEntity> call, Throwable t) {
                    Timber.e("Failure in Login with credentials service call"+t.getMessage());
                    callback.failure( WebAuthError.getShared(context).serviceFailureException(WebAuthErrorCode.LOGINWITH_CREDENTIALS_FAILURE,t.getMessage(), 400,null,null));
                }
            });
        }
        catch (Exception e)
        {
            LogFile.addRecordToLog("LoginDeduplicationResponseEntity Service exception"+e.getMessage());
            Timber.d(e.getMessage());
            callback.failure(WebAuthError.getShared(context).propertyMissingException());
        }
    }*/



    //Get deduplication details
    //Deduplication Login
    //Register deduplication
}
