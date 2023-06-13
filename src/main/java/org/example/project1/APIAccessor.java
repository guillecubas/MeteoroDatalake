package org.example;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class APIAccessor {
    private static final String URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWlsbGVybW8uY3ViYXMuZ3JhbmFkb0BnbWFpbC5jb20iLCJqdGkiOiJhNDEwMTBhYi03ZDliLTQwYzEtYTlkMy00ZmIwMDA4YzY4MzQiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY3OTA1MzA4MCwidXNlcklkIjoiYTQxMDEwYWItN2Q5Yi00MGMxLWE5ZDMtNGZiMDAwOGM2ODM0Iiwicm9sZSI6IiJ9._8E2_47XvkZ8iH4dnkqi1og-tFtV7lQGFPe6FAs_V5w";
    private static final String ACCEPT_HEADER = "application/json";
    private static final String API_KEY_HEADER = "api_key";

    private final String response;
    private static String data;

    public APIAccessor() {
        try {
            Connection.Response resp = Jsoup.connect(URL)
                    .followRedirects(false)
                    .timeout(6000)
                    .ignoreContentType(true)
                    .header(ACCEPT_HEADER, ACCEPT_HEADER)
                    .header(API_KEY_HEADER, API_KEY)
                    .method(Connection.Method.GET)
                    .maxBodySize(0)
                    .execute();
            this.response = resp.body();
            datosUrl datosUrl = new Gson().fromJson(response, datosUrl.class);
            this.data = Jsoup.connect(datosUrl.getdatosUrl())
                    .followRedirects(false)
                    .timeout(6000)
                    .ignoreContentType(true)
                    .header(ACCEPT_HEADER, ACCEPT_HEADER)
                    .header(API_KEY_HEADER, API_KEY)
                    .method(Connection.Method.GET)
                    .maxBodySize(0)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getData() {
        return data;
    }

    private static class datosUrl {
        private String datos;

        public String getdatosUrl() {
            return datos;
        }
    }
}
