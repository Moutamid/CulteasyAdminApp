package com.moutamid.servicebuyingadminapp.Notifications;

import java.io.IOException;

import okhttp3.Interceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(String url){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


   /* private static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+SharedPreference.gettoken(mcontext))
                        .build();

                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        };
    }*/

}
