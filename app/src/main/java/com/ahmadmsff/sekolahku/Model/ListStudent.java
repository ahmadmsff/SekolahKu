package com.ahmadmsff.sekolahku.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListStudent {
    @SerializedName("error")
    private Boolean error;
    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    private List<Student> student;
    @SerializedName("page")
    private Integer page;
    @SerializedName("total_data")
    private Integer total_data;
    @SerializedName("pages")
    private Integer pages;

    public Boolean isError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_data() {
        return total_data;
    }

    public void setTotal_data(Integer total_data) {
        this.total_data = total_data;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
