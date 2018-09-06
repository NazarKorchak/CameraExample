package com.nkorchak.cameraexample;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface CameraApi {

    @Multipart
    @POST("/net/inference")
    Call<ResponseModel> changeImage(@Query("mode") String mode,
                                    @Part MultipartBody.Part img);
}
