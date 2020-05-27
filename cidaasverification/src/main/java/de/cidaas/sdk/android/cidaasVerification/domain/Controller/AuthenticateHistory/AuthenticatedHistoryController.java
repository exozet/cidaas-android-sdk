package de.cidaas.sdk.android.cidaasVerification.domain.Controller.AuthenticateHistory;

import android.content.Context;

import java.util.Dictionary;
import java.util.Map;

import de.cidaas.sdk.android.cidaasVerification.data.Entity.AuthenticatedHistory.AuthenticatedHistoryEntity;
import de.cidaas.sdk.android.cidaasVerification.data.Entity.AuthenticatedHistory.AuthenticatedHistoryResponse;
import de.cidaas.sdk.android.cidaasVerification.data.Service.Helper.VerificationURLHelper;
import de.cidaas.sdk.android.cidaasVerification.domain.Service.AuthenticatedHistory.AuthenticatedHistoryService;
import de.cidaas.sdk.android.entities.DeviceInfoEntity;
import de.cidaas.sdk.android.helper.enums.EventResult;
import de.cidaas.sdk.android.helper.enums.WebAuthErrorCode;
import de.cidaas.sdk.android.helper.extension.WebAuthError;
import de.cidaas.sdk.android.helper.general.DBHelper;
import de.cidaas.sdk.android.helper.logger.LogFile;
import de.cidaas.sdk.android.helper.urlhelper.URLHelper;
import de.cidaas.sdk.android.properties.CidaasProperties;
import de.cidaas.sdk.android.service.helperforservice.Headers.Headers;


public class AuthenticatedHistoryController {
    //Local Variables
    private Context context;


    public static AuthenticatedHistoryController shared;

    public AuthenticatedHistoryController(Context contextFromCidaas) {
        context = contextFromCidaas;
    }


    public static AuthenticatedHistoryController getShared(Context contextFromCidaas) {
        try {

            if (shared == null) {
                shared = new AuthenticatedHistoryController(contextFromCidaas);
            }
        } catch (Exception e) {
            LogFile.getShared(contextFromCidaas).addFailureLog("AuthenticatedHistoryController instance Creation Exception:-" + e.getMessage());
        }
        return shared;
    }

    //--------------------------------------------AuthenticatedHistory--------------------------------------------------------------
    public void getauthenticatedHistoryList(final AuthenticatedHistoryEntity authenticatedHistoryEntity, final EventResult<AuthenticatedHistoryResponse> authenticatedHistoryResult) {
        checkAuthenticatedHistoryEntity(authenticatedHistoryEntity, authenticatedHistoryResult);
    }


    //-------------------------------------checkAuthenticatedHistoryEntity-----------------------------------------------------------
    private void checkAuthenticatedHistoryEntity(final AuthenticatedHistoryEntity authenticatedHistoryEntity, final EventResult<AuthenticatedHistoryResponse> authenticatedHistoryResult) {
        String methodName = "AuthenticatedHistoryController:-checkAuthenticatedHistoryEntity()";
        try {
            if (authenticatedHistoryEntity.getVerification_type() != null && !authenticatedHistoryEntity.getVerification_type().equals("") &&
                    authenticatedHistoryEntity.getSub() != null && !authenticatedHistoryEntity.getSub().equals("")
            ) {
                if (authenticatedHistoryEntity.getStart_time() != null && !authenticatedHistoryEntity.getStart_time().equals("") &&
                        authenticatedHistoryEntity.getEnd_time() != null && !authenticatedHistoryEntity.getEnd_time().equals("")) {

                    LogFile.getShared(context).addInfoLog(methodName, "Verification Type:" + authenticatedHistoryEntity.getVerification_type() +
                            "Sub:" + authenticatedHistoryEntity.getSub());
                    addProperties(authenticatedHistoryEntity, authenticatedHistoryResult);
                } else {
                    authenticatedHistoryResult.failure(WebAuthError.getShared(context).propertyMissingException("StartDate or EndDate  must not be null",
                            "Error:" + methodName));
                    return;
                }
            } else {
                authenticatedHistoryResult.failure(WebAuthError.getShared(context).propertyMissingException("VerificationType or Sub  must not be null",
                        "Error:" + methodName));
                return;
            }
        } catch (Exception e) {
            authenticatedHistoryResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName, WebAuthErrorCode.SCANNED_VERIFICATION_FAILURE,
                    e.getMessage()));
        }
    }


    //-------------------------------------Add Device info and pushnotificationId-------------------------------------------------------
    private void addProperties(final AuthenticatedHistoryEntity authenticatedHistoryEntity, final EventResult<AuthenticatedHistoryResponse> authenticatedHistoryResult) {
        String methodName = "AuthenticatedHistoryController:-addProperties()";
        try {

            CidaasProperties.getShared(context).checkCidaasProperties(new EventResult<Dictionary<String, String>>() {
                @Override
                public void success(Dictionary<String, String> loginPropertiesResult) {
                    final String baseurl = loginPropertiesResult.get("DomainURL");
                    final String clientId = loginPropertiesResult.get("ClientId");


                    //App properties
                    DeviceInfoEntity deviceInfoEntity = DBHelper.getShared().getDeviceInfo();
                    authenticatedHistoryEntity.setDevice_id(deviceInfoEntity.getDeviceId());
                    authenticatedHistoryEntity.setPush_id(deviceInfoEntity.getPushNotificationId());
                    authenticatedHistoryEntity.setClient_id(clientId);

                    //call authenticatedHistory call
                    callAuthenticatedHistory(baseurl, authenticatedHistoryEntity, authenticatedHistoryResult);
                }

                @Override
                public void failure(WebAuthError error) {
                    authenticatedHistoryResult.failure(error);
                }
            });

        } catch (Exception e) {
            authenticatedHistoryResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName, WebAuthErrorCode.SCANNED_VERIFICATION_FAILURE,
                    e.getMessage()));
        }
    }

    //-------------------------------------------Call authenticatedHistory Service-----------------------------------------------------------
    private void callAuthenticatedHistory(String baseurl, final AuthenticatedHistoryEntity authenticatedHistoryEntity, final EventResult<AuthenticatedHistoryResponse> authenticatedHistoryResult) {
        String methodName = "AuthenticatedHistoryController:-authenticatedHistory()";
        try {
            String authenticatedHistoryUrl = VerificationURLHelper.getShared().getAuthentictedHistoryURL(baseurl);

            //headers Generation
            Map<String, String> headers = Headers.getShared(context).getHeaders(null, false, URLHelper.contentTypeJson);

            //AuthenticatedHistory Service call
            AuthenticatedHistoryService.getShared(context).callAuthenticatedHistoryService(authenticatedHistoryUrl, headers, authenticatedHistoryEntity, authenticatedHistoryResult);
        } catch (Exception e) {
            authenticatedHistoryResult.failure(WebAuthError.getShared(context).methodException("Exception:-" + methodName, WebAuthErrorCode.SCANNED_VERIFICATION_FAILURE,
                    e.getMessage()));
        }
    }

}
