package com.noavaran.system.vira.baryab.utils.okhttp.request;

public class RequestCancelShipmentRequest {
    private String draftId;
    private String description;

    public RequestCancelShipmentRequest() {}

    public RequestCancelShipmentRequest(String draftId, String description) {
        this.draftId = draftId;
        this.description = description;
    }

    public String getDraftId() {
        return draftId;
    }

    public void setDraftId(String draftId) {
        this.draftId = draftId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}