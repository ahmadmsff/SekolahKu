package com.ahmadmsff.sekolahku.APIHelper;

import com.ahmadmsff.sekolahku.Model.ListStudent;
import com.ahmadmsff.sekolahku.Model.Student;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {

    //GET ALL DATA
    @GET("get.php")
    Call<ListStudent> getSiswa();

    //GET ALL DATA
    @GET("get.php")
    Call<ListStudent> getNextSiswa(@Query("page") int page);

    //GET DATA BY ID
    @GET("get.php")
    Call<ListStudent> getSiswaByID(@Query("id") String id);

    //SEARCH DATA
    @GET("get.php")
    Call<ListStudent> searchSiswa(@Query("key") String key);

    //POST DATA
    @FormUrlEncoded
    @POST("post.php")
    Call<ListStudent> setSiswa(@Field("nama") String nama,
                                @Field("email") String email,
                                @Field("jenis_kelamin") String jenis_kelamin,
                                @Field("tanggal_lahir") String tanggal_lahir,
                                @Field("alamat") String alamat,
                                @Field("nomor_handphone") String nomor_handphone);

    @FormUrlEncoded
    @POST("put.php")
    Call<ListStudent> editSiswa(@Field("id") Integer id,
                                 @Field("nama") String nama,
                                 @Field("email") String email,
                                 @Field("jenis_kelamin") String jenis_kelamin,
                                 @Field("tanggal_lahir") String tanggal_lahir,
                                 @Field("alamat") String alamat,
                                 @Field("nomor_handphone") String nomor_handphone);
    @GET("delete.php")
    Call<ListStudent> deleteSiswa(@Query("id") Integer id);

}
