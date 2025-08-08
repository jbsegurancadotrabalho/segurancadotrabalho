package br.com.jbst.services;

import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class TestOkHttp {
    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }
}

