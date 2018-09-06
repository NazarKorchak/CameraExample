package com.nkorchak.cameraexample;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {

    @SerializedName("img")
    private byte[] img;

    public byte[] getImg() {
        return img;
    }
}
