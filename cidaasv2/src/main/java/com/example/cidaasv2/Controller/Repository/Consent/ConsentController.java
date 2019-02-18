package com.example.cidaasv2.Controller.Repository.Consent;

import android.content.Context;

import com.example.cidaasv2.Controller.Repository.AccessToken.AccessTokenController;
import com.example.cidaasv2.Helper.Enums.Result;
import com.example.cidaasv2.Helper.Extension.WebAuthError;
import com.example.cidaasv2.Service.Entity.AccessTokenEntity;
import com.example.cidaasv2.Service.Entity.ConsentManagement.ConsentDetailsResultEntity;
import com.example.cidaasv2.Service.Entity.ConsentManagement.ConsentManagementAcceptResponseEntity;
import com.example.cidaasv2.Service.Entity.ConsentManagement.ConsentManagementAcceptedRequestEntity;
import com.example.cidaasv2.Service.Entity.ConsentManagement.ResumeConsent.ResumeConsentRequestEntity;
import com.example.cidaasv2.Service.Entity.ConsentManagement.ResumeConsent.ResumeConsentResponseEntity;
import com.example.cidaasv2.Service.Entity.LoginCredentialsEntity.LoginCredentialsResponseEntity;
import com.example.cidaasv2.Service.Repository.Consent.ConsentService;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class ConsentController {

    private Context context;
    private String ConsentName;
    private String ConsentVersion;

    public static ConsentController shared;

    public ConsentController(Context contextFromCidaas) {

        context = contextFromCidaas;
        //Todo setValue for authenticationType

    }

    public static ConsentController getShared(Context contextFromCidaas) {
        try {

            if (shared == null) {
                shared = new ConsentController(contextFromCidaas);
            }
        } catch (Exception e) {
            Timber.i(e.getMessage());
        }
        return shared;
    }


    //Get Consent URL
    public void getConsentURL(@NonNull String consentName, @NonNull String consentVersion, @NonNull final Result<String> result) {

    }

    //Service call For ConsentManagementService
    public void getConsentDetails(@NonNull final String baseurl, @NonNull final String consentName,
                                  @NonNull final Result<ConsentDetailsResultEntity> consentresult) {
        ConsentName = consentName;

        try {
            if (consentName != null && !consentName.equals("") && baseurl != null && !baseurl.equals("")) {



                        //Call getString Details
                        ConsentService.getShared(context).getConsentDetails(baseurl, consentName, new Result<ConsentDetailsResultEntity>() {
                            @Override
                            public void success(ConsentDetailsResultEntity result) {
                                //result.getData().setConsentURL(urlResult);
                                consentresult.success(result);
                            }

                            @Override
                            public void failure(WebAuthError error) {
                                consentresult.failure(error);
                            }
                        });

            } else {
                consentresult.failure(WebAuthError.getShared(context).propertyMissingException());
            }
        } catch (Exception e) {
            Timber.e("Get Consent URL Service" + e.getMessage());

        }
    }


    //Service call To AcceptConsent
    public void acceptConsent(@NonNull final String baseurl, @NonNull final ConsentManagementAcceptedRequestEntity consentManagementAcceptedRequestEntity,
                              final Result<LoginCredentialsResponseEntity> loginresult) {
        try {

        //    consentManagementAcceptedRequestEntity.setName(ConsentName);

            if (consentManagementAcceptedRequestEntity.getClient_id() != null && consentManagementAcceptedRequestEntity.getClient_id() != "" &&

                    consentManagementAcceptedRequestEntity.getSub() != null && consentManagementAcceptedRequestEntity.getSub() != "" &&
                    consentManagementAcceptedRequestEntity.getName() != null && consentManagementAcceptedRequestEntity.getName() != "" &&
                    consentManagementAcceptedRequestEntity.getVersion() != null && consentManagementAcceptedRequestEntity.getVersion() != ""
                    && baseurl != null && !baseurl.equals("")) {

                //Todo Service call
                ConsentService.getShared(context).acceptConsent(baseurl, consentManagementAcceptedRequestEntity,null,
                        new Result<ConsentManagementAcceptResponseEntity>() {
                    @Override
                    public void success(ConsentManagementAcceptResponseEntity serviceresult) {

                        ResumeConsentRequestEntity resumeConsentRequestEntity = new ResumeConsentRequestEntity();
                        resumeConsentRequestEntity.setTrack_id(consentManagementAcceptedRequestEntity.getTrackId());
                        resumeConsentRequestEntity.setSub(consentManagementAcceptedRequestEntity.getSub());
                        resumeConsentRequestEntity.setName(consentManagementAcceptedRequestEntity.getName());
                        resumeConsentRequestEntity.setVersion(consentManagementAcceptedRequestEntity.getVersion());
                        resumeConsentRequestEntity.setClient_id(consentManagementAcceptedRequestEntity.getClient_id());
                        ConsentService.getShared(context).resumeConsent(baseurl, resumeConsentRequestEntity, null,new Result<ResumeConsentResponseEntity>() {
                            @Override
                            public void success(ResumeConsentResponseEntity result) {

                                AccessTokenController.getShared(context).getAccessTokenByCode(result.getData().getCode(), new Result<AccessTokenEntity>() {
                                    @Override
                                    public void success(AccessTokenEntity result) {
                                        LoginCredentialsResponseEntity loginCredentialsResponseEntity=new LoginCredentialsResponseEntity();
                                        loginCredentialsResponseEntity.setSuccess(true);
                                        loginCredentialsResponseEntity.setStatus(200);
                                        loginCredentialsResponseEntity.setData(result);
                                        loginresult.success(loginCredentialsResponseEntity);
                                    }

                                    @Override
                                    public void failure(WebAuthError error) {
                                        loginresult.failure(error);
                                    }
                                });

                            }

                            @Override
                            public void failure(WebAuthError error) {
                                loginresult.failure(error);
                            }
                        });
                    }

                    @Override
                    public void failure(WebAuthError error) {
                        loginresult.failure(error);
                    }
                });
            } else {

                loginresult.failure(WebAuthError.getShared(context).propertyMissingException());
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

}
