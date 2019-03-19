package com.example.cidaasv2.Controller.Repository.MFASettings;

import android.content.Context;

import com.example.cidaasv2.Helper.Enums.HttpStatusCode;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.Genral.DBHelper;
import com.example.cidaasv2.Helper.Logger.LogFile;
import com.example.cidaasv2.Service.Entity.MFA.DeleteMFA.DeleteMFAResponseEntity;
import com.example.cidaasv2.Service.Entity.MFA.MFAList.MFAListResponseEntity;
import com.example.cidaasv2.Service.Entity.NotificationEntity.DenyNotification.DenyNotificationRequestEntity;
import com.example.cidaasv2.Service.Entity.NotificationEntity.DenyNotification.DenyNotificationResponseEntity;
import com.example.cidaasv2.Service.Entity.NotificationEntity.GetPendingNotification.NotificationEntity;
import com.example.cidaasv2.Service.Entity.UserList.ConfiguredMFAListEntity;
import com.example.cidaasv2.Service.Repository.Verification.Settings.VerificationSettingsService;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class VerificationSettingsController {



    private Context context;

    public static VerificationSettingsController shared;

    public VerificationSettingsController(Context contextFromCidaas) {

        context=contextFromCidaas;
        //Todo setValue for authenticationType

    }

    public static VerificationSettingsController getShared(Context contextFromCidaas )
    {
        try {

            if (shared == null) {
                shared = new VerificationSettingsController(contextFromCidaas);
            }
        }
        catch (Exception e)
        {
            Timber.i(e.getMessage());
        }
        return shared;
    }

    //Service call To Get MFA list
    public void getmfaList(@NonNull final String baseurl, @NonNull String sub, final Result<MFAListResponseEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && sub != null && !sub.equals("")) {
                // Change service call to private

               String userDeviceId=DBHelper.getShared().getUserDeviceId(baseurl);


                    VerificationSettingsService.getShared(context).getmfaList(baseurl, sub, userDeviceId,null, result);
                /*}
                else
                {
                    String errorMessae="UserDeviceID must not be empty ";

                    result.failure(WebAuthError.getShared(context).customException(417,errorMessae,417));
                }*/
            }
            else
            {
                result.failure(WebAuthError.getShared(context).propertyMissingException("BaseURL or Sub must not be null"));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).serviceException(WebAuthErrorCode.MFA_LIST_FAILURE));
            LogFile.getShared(context).addRecordToLog("Get MFA List"+e.getMessage()+WebAuthErrorCode.MFA_LIST_FAILURE);
            Timber.e(e.getMessage());
        }
    }



    //Service call To delete MFA list
    public void deleteMFA(@NonNull final String baseurl,@NonNull final String accessToken, @NonNull String userDeviceId, @NonNull String verificationType, final Result<DeleteMFAResponseEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && accessToken != null && !accessToken.equals("") && userDeviceId != null && !userDeviceId.equals("")
                    && verificationType != null && !verificationType.equals("")) {

                VerificationSettingsService.getShared(context).deleteMFA(baseurl, accessToken,userDeviceId,verificationType,null,
                        new Result<DeleteMFAResponseEntity>() {

                    @Override
                    public void success(DeleteMFAResponseEntity serviceresult) {
                        result.success(serviceresult);
                    }

                    @Override
                    public void failure(WebAuthError error) {

                        result.failure(error);
                    }
                });
            }
            else
            {
                String errorMessage="UserDeviceID or AccessToken or VerificationType or baseURL must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DELETE_MFA_FAILURE,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DELETE_MFA_FAILURE,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }

    //To delete MFA list
    public void deleteAllMFA(@NonNull final String baseurl, @NonNull final String accessToken, @NonNull String userDeviceId, final Result<DeleteMFAResponseEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && accessToken != null && !accessToken.equals("") && userDeviceId != null && !userDeviceId.equals("") ) {

                VerificationSettingsService.getShared(context).deleteAllMFA(baseurl,accessToken, userDeviceId,null, result);
            }
            else
            {
                String errorMessage="UserDeviceID or baseURL must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DELETE_MFA_FAILURE,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DELETE_MFA_FAILURE,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }


    //To get pending notification Service
    public void denyNotification(@NonNull final String baseurl, @NonNull final String accessToken, DenyNotificationRequestEntity denyNotificationRequestEntity,final Result<DenyNotificationResponseEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && accessToken != null && !accessToken.equals("")  ) {

                VerificationSettingsService.getShared(context).denyNotification(baseurl,accessToken,denyNotificationRequestEntity, null, result);
            }
            else
            {
                String errorMessage="AccessToken or baseURL must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DENY_NOTIFICATION,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.DENY_NOTIFICATION,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }


    //To deny notification Service
    public void getPendingNotification(@NonNull final String baseurl, @NonNull final String accessToken, @NonNull final String userDeviceId,final Result<NotificationEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && accessToken != null && !accessToken.equals("")&& userDeviceId != null && !userDeviceId.equals("")  ) {

                VerificationSettingsService.getShared(context).getPendingNotification(baseurl,accessToken,userDeviceId, null, result);
            }
            else
            {
                String errorMessage="AccessToken or baseURL or UserDeviceId must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }


    //To get Configured MFA List of a user
    public void getConfiguredMFAList(@NonNull final String baseurl, @NonNull final String sub, @NonNull final String userDeviceId,final Result<ConfiguredMFAListEntity> result){
        try{

            if (baseurl != null && !baseurl.equals("") && sub != null && !sub.equals("")&& userDeviceId != null && !userDeviceId.equals("")  ) {

                VerificationSettingsService.getShared(context).getConfiguredMFAList(baseurl,sub,userDeviceId, null, result);
            }
            else
            {
                String errorMessage="sub or baseURL or UserDeviceId must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }


    //update FCM TOken
    public void updateFCMToken(@NonNull final String baseurl, @NonNull final String AccessToken, @NonNull final String FCMToken,final Result<Object> result){
        try{

            if (baseurl != null && !baseurl.equals("") && AccessToken != null && !AccessToken.equals("")&& FCMToken != null && !FCMToken.equals("")  ) {

                VerificationSettingsService.getShared(context).updateFCMToken(baseurl,AccessToken,FCMToken, null, result);
            }
            else
            {
                String errorMessage="sub or baseURL or UserDeviceId must not be empty ";

                result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {

            result.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PENDING_NOTIFICATION_FAILURE,e.getMessage(), HttpStatusCode.EXPECTATION_FAILED));
            Timber.e(e.getMessage());
        }
    }

}
