package com.example.cidaasv2.VerificationV2.data.Entity.UpdateFCMToken;

import com.example.cidaasv2.VerificationV2.data.Entity.Setup.SetupResponseData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFCMTokenResponseEntity implements Serializable {

    boolean success;
    int status;
    boolean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}

