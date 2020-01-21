package com.noavaran.system.vira.baryab.utils.okhttp.response;

public class AcceptDriverResponse {
    private String DraftId;
    private String DriverId;

    public AcceptDriverResponse(String DraftId, String DriverId) {
        this.DraftId = DraftId;
        this.DriverId = DriverId;
    }

    public String getDraftId() {
        return DraftId;
    }

    public void setDraftId(String draftId) {
        DraftId = draftId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }
}