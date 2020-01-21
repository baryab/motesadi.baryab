package com.noavaran.system.vira.baryab.utils.okhttp.request;

public class RateForLoadRequest {
    private String draftId;
    private float rate;

    public RateForLoadRequest() {}

    public RateForLoadRequest(String draftId, float rate) {
        this.draftId = draftId;
        this.rate = rate;
    }

    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(String draftId) {
        this.draftId = draftId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}