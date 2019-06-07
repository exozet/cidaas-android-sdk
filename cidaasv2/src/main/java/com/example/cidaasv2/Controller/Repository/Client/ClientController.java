package com.example.cidaasv2.Controller.Repository.Client;

import android.content.Context;

import com.example.cidaasv2.Controller.Cidaas;
import com.example.cidaasv2.Controller.Repository.RequestId.RequestIdController;
import com.example.cidaasv2.Helper.CidaasProperties.CidaasProperties;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.Genral.CidaasHelper;
import com.example.cidaasv2.Helper.Logger.LogFile;
import com.example.cidaasv2.Helper.pkce.OAuthChallengeGenerator;
import com.example.cidaasv2.Service.Entity.AuthRequest.AuthRequestResponseEntity;
import com.example.cidaasv2.Service.Entity.ClientInfo.ClientInfoEntity;
import com.example.cidaasv2.Service.Repository.Client.ClientService;

import java.util.Dictionary;
import java.util.HashMap;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class ClientController {

    private Context context;

    public static ClientController shared;

    public ClientController(Context contextFromCidaas) {
        context=contextFromCidaas;

    }

    String codeVerifier, codeChallenge;
    // Generate Code Challenge and Code verifier
    public void generateChallenge(){
        OAuthChallengeGenerator generator = new OAuthChallengeGenerator();

        codeVerifier=generator.getCodeVerifier();
        codeChallenge= generator.getCodeChallenge(codeVerifier);

    }

    public static ClientController getShared(Context contextFromCidaas )
    {
        try {

            if (shared == null) {
                shared = new ClientController(contextFromCidaas);
            }
        }
        catch (Exception e)
        {  LogFile.getShared(contextFromCidaas).addFailureLog("ClientController instance Creation Exception:-"+e.getMessage());
        }
        return shared;
    }


    //-----------------------------------------------------Service call To Get Client Info without requestId----------------------------------------
    public void getClientInfo(final Result<ClientInfoEntity> clientInfoEntityResult,final HashMap<String,String>... extraParams)
    {
       final String methodName="Client Controller :getClientInfo() without requestId";
        try{
            if(CidaasHelper.baseurl!=null && !CidaasHelper.baseurl.equals("")) {

                CidaasProperties.getShared(context).checkCidaasProperties(new Result<Dictionary<String, String>>() {
                    @Override
                    public void success(Dictionary<String, String> loginPropertiesResult) {
                        requestIdGeneration(loginPropertiesResult,clientInfoEntityResult,extraParams);
                    }


                    @Override
                    public void failure(WebAuthError error) {
                        clientInfoEntityResult.failure(error);
                    }
                });
            }
            else
            {
                clientInfoEntityResult.failure(WebAuthError.getShared(context).propertyMissingException("DomainURL or RequestId Must not be empty"
                        ,"Error :"+methodName));
            }

        }
        catch (Exception e)
        {
            clientInfoEntityResult.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.CLIENT_INFO_FAILURE,
                    e.getMessage()));
        }
    }


    //RequestId Generation
    private void requestIdGeneration(Dictionary<String, String> loginPropertiesResult,final Result<ClientInfoEntity> clientInfoEntityResult,
                                     final HashMap<String,String>... extraParams)
    {
        final String methodName="Client Controller :requestIdGeneration()";
       try
       {
            RequestIdController.getShared(context).getRequestId(loginPropertiesResult, new Result<AuthRequestResponseEntity>() {
                @Override
                public void success(AuthRequestResponseEntity result) {
                    LogFile.getShared(context).addSuccessLog("Success" + methodName, " Success requestId:-" + result.getData().getRequestId());
                    getClientInfo(result.getData().getRequestId(), clientInfoEntityResult);
                }

                @Override
                public void failure(WebAuthError error) {
                    clientInfoEntityResult.failure(error);
                }
            }, extraParams);
        }
        catch (Exception e)
        {
            clientInfoEntityResult.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName, WebAuthErrorCode.CLIENT_INFO_FAILURE,
                    e.getMessage()));
        }
    }


    //Service call to Get Client info
    public void getClientInfo(@NonNull final String requestId, final Result<ClientInfoEntity> clientInfoEntityResult)
    {
        final String methodName="Client Controller :getClientInfo()";
        try{
            LogFile.getShared(context).addInfoLog("Info "+methodName, " Info requestId:-"+requestId);
            if(CidaasHelper.baseurl!=null && !CidaasHelper.baseurl.equals("") && requestId != null && !requestId.equals("")) {

                CidaasProperties.getShared(context).checkCidaasProperties(new Result<Dictionary<String, String>>() {
                    @Override
                    public void success(Dictionary<String, String> stringresult) {
                        ClientService.getShared(context).getClientInfo(requestId,stringresult.get("DomainURL"),clientInfoEntityResult);
                    }

                    @Override
                    public void failure(WebAuthError error) {
                        clientInfoEntityResult.failure(error);
                    }
                });
            }
            else
            {
                clientInfoEntityResult.failure(WebAuthError.getShared(context).CidaaspropertyMissingException("", "Error :"+methodName));
            }

        }
        catch (Exception e)
        {
         clientInfoEntityResult.failure(WebAuthError.getShared(context).methodException("Exception :"+methodName,WebAuthErrorCode.CLIENT_INFO_FAILURE,
                 e.getMessage()));
        }
    }


}
