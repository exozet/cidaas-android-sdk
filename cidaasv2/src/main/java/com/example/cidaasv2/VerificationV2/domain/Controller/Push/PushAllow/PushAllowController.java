package com.example.cidaasv2.VerificationV2.domain.Controller.Push.PushAllow;

import android.content.Context;

import com.example.cidaasv2.Helper.CidaasProperties.CidaasProperties;
import com.example.cidaasv2.Helper.Entity.DeviceInfoEntity;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.Genral.DBHelper;
import com.example.cidaasv2.Helper.Logger.LogFile;
import com.example.cidaasv2.Helper.URLHelper.URLHelper;
import com.example.cidaasv2.Service.HelperForService.Headers.Headers;
import com.example.cidaasv2.VerificationV2.data.Entity.Push.PushAllow.PushAllowEntity;
import com.example.cidaasv2.VerificationV2.data.Entity.Push.PushAllow.PushAllowResponse;
import com.example.cidaasv2.VerificationV2.data.Service.Helper.VerificationURLHelper;
import com.example.cidaasv2.VerificationV2.domain.Service.Push.PushAllow.PushAllowService;

import java.util.Dictionary;
import java.util.Map;

public class PushAllowController {
    //Local Variables
    private Context context;


    public static PushAllowController shared;

    public PushAllowController(Context contextFromCidaas) {
        context = contextFromCidaas;
    }


    public static PushAllowController getShared(Context contextFromCidaas) {
        try {

            if (shared == null) {
                shared = new PushAllowController(contextFromCidaas);
            }
        } catch (Exception e) {
            LogFile.getShared(contextFromCidaas).addFailureLog("PushAllowController instance Creation Exception:-" + e.getMessage());
        }
        return shared;
    }


    //--------------------------------------------PushAllow--------------------------------------------------------------
    public void pushAllowVerification(final PushAllowEntity pushAllowEntity, final Result<PushAllowResponse> pushAllowResult)
    {
        checkPushAllowEntity(pushAllowEntity,pushAllowResult);
    }


    //-------------------------------------checkPushAllowEntity-----------------------------------------------------------
    private void checkPushAllowEntity(final PushAllowEntity pushAllowEntity, final Result<PushAllowResponse> pushAllowResult)
    {
        String methodName = "PushAllowController:-checkPushAllowEntity()";
        try {
            if (pushAllowEntity.getVerificationType() != null && !pushAllowEntity.getVerificationType().equals(""))
            {
                if(pushAllowEntity.getClient_id() != null && !pushAllowEntity.getClient_id().equals("") &&
                        pushAllowEntity.getExchange_id() != null && !pushAllowEntity.getExchange_id().equals(""))
                {
                    // Todo Check For Face and Voice
                    addProperties(pushAllowEntity,pushAllowResult);
                }
                else
                {
                    pushAllowResult.failure(WebAuthError.getShared(context).propertyMissingException("ClientId or ExchangeId must not be null",
                            "Error:"+methodName));
                    return;
                }

            }
            else
            {
                pushAllowResult.failure(WebAuthError.getShared(context).propertyMissingException("Verification type must not be null", "Error:"+methodName));
                return;
            }

        }
        catch (Exception e) {
            pushAllowResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName,
                    WebAuthErrorCode.PUSH_ALLOW_FAILURE, e.getMessage()));
        }
    }


    //-------------------------------------Add Device info and pushnotificationId-------------------------------------------------------
    private void addProperties(final PushAllowEntity pushAllowEntity, final Result<PushAllowResponse> pushAllowResult)
    {
        String methodName = "PushAllowController:-addProperties()";
        try {
            //App properties
            DeviceInfoEntity deviceInfoEntity = DBHelper.getShared().getDeviceInfo();
            pushAllowEntity.setDevice_id(deviceInfoEntity.getDeviceId());
            pushAllowEntity.setPush_id(deviceInfoEntity.getPushNotificationId());

            //call pushAllow call
            callPushAllow(pushAllowEntity,pushAllowResult);
        }
        catch (Exception e) {
            pushAllowResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName,
                    WebAuthErrorCode.PUSH_ALLOW_FAILURE, e.getMessage()));
        }
    }

    //-------------------------------------------Call pushAllow Service-----------------------------------------------------------
    private void callPushAllow(final PushAllowEntity pushAllowEntity, final Result<PushAllowResponse> pushAllowResult)
    {
        String methodName = "PushAllowController:-pushAllow()";
        try
        {
            CidaasProperties.getShared(context).checkCidaasProperties(new Result<Dictionary<String, String>>() {
                @Override
                public void success(Dictionary<String, String> loginPropertiesResult) {
                    final String baseurl = loginPropertiesResult.get("DomainURL");

                    String pushAllowUrl= VerificationURLHelper.getShared().getPushAllowURL(baseurl,pushAllowEntity.getVerificationType());

                    //headers Generation
                    Map<String,String> headers= Headers.getShared(context).getHeaders(null,false, URLHelper.contentTypeJson);

                    //PushAllow Service call
                    PushAllowService.getShared(context).callPushAllowService(pushAllowUrl,headers,pushAllowEntity,pushAllowResult);
                }
                @Override
                public void failure(WebAuthError error) {
                    pushAllowResult.failure(error);
                }
            });
        }
        catch (Exception e) {
            pushAllowResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName,
                    WebAuthErrorCode.PUSH_ALLOW_FAILURE, e.getMessage()));
        }
    }
}
