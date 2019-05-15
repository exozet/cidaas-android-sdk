package com.example.cidaasv2.VerificationV2.data.Entity.Enroll;

import com.example.cidaasv2.Helper.Entity.FingerPrintEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrollEntity implements Serializable {

    private String exchange_id="";
    private String device_id="";
    private String client_id="";
    private String push_id="";
    private String pass_code="";
    private String verificationType="";

    private FingerPrintEntity fingerPrintEntity;

    //Constructors
    public EnrollEntity() {
    }

    public EnrollEntity(String exchange_id, String client_id, String verificationType) {
        this.exchange_id = exchange_id;
        this.client_id = client_id;
        this.verificationType = verificationType;
    }



    public EnrollEntity(String exchange_id, String device_id, String client_id, String push_id, String pass_code, String verificationType) {
        this.exchange_id = exchange_id;
        this.device_id = device_id;
        this.client_id = client_id;
        this.push_id = push_id;
        this.pass_code = pass_code;
        this.verificationType = verificationType;
    }

    public EnrollEntity(String exchange_id, String client_id, String pass_code, String verificationType) {
        this.exchange_id = exchange_id;
        this.client_id = client_id;
        this.pass_code = pass_code;
        this.verificationType = verificationType;
    }

    public EnrollEntity(String exchange_id, String client_id, String verificationType, FingerPrintEntity fingerPrintEntity) {
        this.exchange_id = exchange_id;
        this.client_id = client_id;
        this.verificationType = verificationType;
        this.fingerPrintEntity = fingerPrintEntity;
    }


    public EnrollEntity(String exchange_id, String device_id, String client_id, String push_id, String verificationType, FingerPrintEntity fingerPrintEntity) {
        this.exchange_id = exchange_id;
        this.device_id = device_id;
        this.client_id = client_id;
        this.push_id = push_id;
        this.verificationType = verificationType;
        this.fingerPrintEntity = fingerPrintEntity;
    }

    public FingerPrintEntity getFingerPrintEntity() {
        return fingerPrintEntity;
    }

    public void setFingerPrintEntity(FingerPrintEntity fingerPrintEntity) {
        this.fingerPrintEntity = fingerPrintEntity;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public String getExchange_id() {
        return exchange_id;
    }

    public void setExchange_id(String exchange_id) {
        this.exchange_id = exchange_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getPass_code() {
        return pass_code;
    }

    public void setPass_code(String pass_code) {
        this.pass_code = pass_code;
    }
}
