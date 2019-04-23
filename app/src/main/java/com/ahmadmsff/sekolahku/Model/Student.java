package com.ahmadmsff.sekolahku.Model;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("email")
    private String email;
    @SerializedName("jenis_kelamin")
    private String jenis_kelamin;
    @SerializedName("tanggal_lahir")
    private String tanggal_lahir;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("nomor_handphone")
    private String nomor_handphone;
    @SerializedName("image")
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomor_handphone() {
        return nomor_handphone;
    }

    public void setNomor_handphone(String nomor_handphone) {
        this.nomor_handphone = nomor_handphone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
