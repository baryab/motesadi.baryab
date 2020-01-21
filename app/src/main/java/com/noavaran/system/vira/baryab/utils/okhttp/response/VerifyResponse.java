package com.noavaran.system.vira.baryab.utils.okhttp.response;

public class VerifyResponse {
    private int VerifyCode;
    private String PhoneNumber;
    private boolean CodeType;
    private String userId;
    private String registrationId;

    public VerifyResponse() {}

    public VerifyResponse(int VerifyCode, String PhoneNumber, boolean CodeType, String userId, String registrationId) {
        this.VerifyCode = VerifyCode;
        this.PhoneNumber = PhoneNumber;
        this.CodeType = CodeType;
        this.userId = userId;
        this.registrationId = registrationId;
    }

    public int getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        VerifyCode = verifyCode;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public boolean getCodeType() {
        return CodeType;
    }

    public void setCodeType(boolean codeType) {
        CodeType = codeType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
