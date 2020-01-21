package com.noavaran.system.vira.baryab.utils.okhttp.request;

public class RequestUploadProfilePicture {
    private String pic;

    public RequestUploadProfilePicture() {}

    public RequestUploadProfilePicture(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
