package de.cidaas.sdk.android.cidaasverification.data.entity.push.pushallow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushAllowResponse implements Serializable {
    boolean success;
    int status;
    PushAllowResponseDataEntity data;

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

    public PushAllowResponseDataEntity getData() {
        return data;
    }

    public void setData(PushAllowResponseDataEntity data) {
        this.data = data;
    }
}
