package com.example.cidaasv2.Controller.Repository.ResetPassword;

import android.content.Context;

import com.example.cidaasv2.Helper.Enums.HttpStatusCode;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Enums.WebAuthErrorCode;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Helper.Logger.LogFile;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetNewPassword.ResetNewPasswordResponseEntity;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetNewPassword.ResetPasswordEntity;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetPasswordRequestEntity;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetPasswordResponseEntity;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetPasswordValidateCode.ResetPasswordValidateCodeRequestEntity;
import com.example.cidaasv2.Service.Entity.ResetPassword.ResetPasswordValidateCode.ResetPasswordValidateCodeResponseEntity;
import com.example.cidaasv2.Service.Repository.ResetPassword.ResetPasswordService;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class ResetPasswordController {


    private String sub;
    private String verificationType;
    private Context context;

    public static ResetPasswordController shared;

    public ResetPasswordController(Context contextFromCidaas) {
        sub="";
        verificationType="";
        context=contextFromCidaas;
        //Todo setValue for authenticationType

    }

    public static ResetPasswordController getShared(Context contextFromCidaas )
    {
        try {

            if (shared == null) {
                shared = new ResetPasswordController(contextFromCidaas);
            }
        }
        catch (Exception e)
        {
            Timber.i(e.getMessage());
        }
        return shared;
    }
    //Todo handle onSuccess and onError


    //initiateResetResetPasswordService
    public void initiateresetPasswordService(@NonNull String baseurl, @NonNull ResetPasswordRequestEntity resetPasswordRequestEntity,
                                             final Result<ResetPasswordResponseEntity> resetpasswordResult)
    {
        try {

            if(resetPasswordRequestEntity.getRequestId() != null && !resetPasswordRequestEntity.getRequestId().equals("")
                    && baseurl != null && !baseurl.equals("")){
                ResetPasswordService.getShared(context).initiateresetPassword(resetPasswordRequestEntity, baseurl,null,
                        resetpasswordResult);
            }
            else{

                resetpasswordResult.failure(WebAuthError.getShared(context).propertyMissingException("RequestId or Baseurl must not be null"));
            }
        }
        catch (Exception e)
        {
            LogFile.getShared(context).addRecordToLog(e.getMessage()+WebAuthErrorCode.INITIATE_RESET_PASSWORD_FAILURE);
            Timber.e(e.getMessage());
        }
    }


    //ResetResetPasswordValidateCodeService
    public void resetPasswordValidateCode(@NonNull String baseurl,@NonNull String verificationCode,@NonNull String rprq,
                                                  final Result<ResetPasswordValidateCodeResponseEntity> resetpasswordResult)
    {
        try {



            if( verificationCode != null && !verificationCode.equals("") && baseurl != null && !baseurl.equals("") && rprq != null && !rprq.equals("")){

                ResetPasswordValidateCodeRequestEntity resetPasswordValidateCodeRequestEntity=new ResetPasswordValidateCodeRequestEntity();
                resetPasswordValidateCodeRequestEntity.setResetRequestId(rprq);
                resetPasswordValidateCodeRequestEntity.setCode(verificationCode);

                ResetPasswordService.getShared(context).resetPasswordValidateCode(resetPasswordValidateCodeRequestEntity, baseurl,
                        new Result<ResetPasswordValidateCodeResponseEntity>() {

                            @Override
                            public void success(ResetPasswordValidateCodeResponseEntity serviceresult) {
                                resetpasswordResult.success(serviceresult);
                            }

                            @Override
                            public void failure(WebAuthError error) {

                                resetpasswordResult.failure(error);
                            }
                        });
            }
            else{

                resetpasswordResult.failure(WebAuthError.getShared(context).propertyMissingException("RPRQ or verification Code must not be null"));
            }
        }
        catch (Exception e)
        {
            LogFile.getShared(context).addRecordToLog(e.getMessage()+WebAuthErrorCode.RESET_PASSWORD_VALIDATE_CODE_FAILURE);
            resetpasswordResult.failure(WebAuthError.getShared(context).serviceException(WebAuthErrorCode.RESET_PASSWORD_VALIDATE_CODE_FAILURE));
        }
    }


    //resetNewPasswordService
    public void resetNewPassword(@NonNull String baseurl, ResetPasswordEntity resetPasswordEntity
            , final Result<ResetNewPasswordResponseEntity> resetpasswordResult)
    {
        try {


            if(resetPasswordEntity.getPassword() != null && !resetPasswordEntity.getPassword().equals("") &&
                    resetPasswordEntity.getConfirmPassword()!= null && !resetPasswordEntity.getConfirmPassword().equals("")
                    && baseurl != null && !baseurl.equals("")){

                ResetPasswordService.getShared(context).resetNewPassword(resetPasswordEntity, baseurl,
                        new Result<ResetNewPasswordResponseEntity>() {

                            @Override
                            public void success(ResetNewPasswordResponseEntity serviceresult) {
                                resetpasswordResult.success(serviceresult);
                            }

                            @Override
                            public void failure(WebAuthError error) {

                                resetpasswordResult.failure(error);
                            }
                        });
            }
            else{
                String errorMessage="ExchangeId or Exchange ID must not be empty";

                resetpasswordResult.failure(WebAuthError.getShared(context).customException(WebAuthErrorCode.PROPERTY_MISSING,
                        errorMessage, HttpStatusCode.EXPECTATION_FAILED));
            }
        }
        catch (Exception e)
        {
            LogFile.getShared(context).addRecordToLog(e.getMessage()+WebAuthErrorCode.RESET_PASSWORD_VALIDATE_CODE_FAILURE);
            resetpasswordResult.failure(WebAuthError.getShared(context).serviceException(WebAuthErrorCode.RESET_PASSWORD_VALIDATE_CODE_FAILURE));
            Timber.e(e.getMessage());
        }
    }

}
