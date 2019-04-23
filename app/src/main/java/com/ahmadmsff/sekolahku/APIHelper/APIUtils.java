package com.ahmadmsff.sekolahku.APIHelper;

public class APIUtils {
    public static final String BASE_API_URL = "https://ahmadmsff.000webhostapp.com/api/smk/";
    public static APIServices getAPIServices() {
        return APIClient.getClient(BASE_API_URL).create(APIServices.class);
    }
}
