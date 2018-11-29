package com.example.cidaasv2.Service.Entity.UserList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListDataEntity implements Serializable{

    public String _id;
    public String verificationType;
    public String verifierId;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }
}
