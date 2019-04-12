package com.example.findmefood;

import android.util.Log;

import com.example.findmefood.models.ApiKey;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class YelpLoggingInterceptor implements Interceptor {
    private String tokenType;
    private static final String TAG = YelpLoggingInterceptor.class.getName();
    private Logger logger;
    public ApiKey apiKey = new ApiKey("V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx", "Bearer ");

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", apiKey.getTokenType() + apiKey.getApiKey())
                .build();
        Log.d(TAG,"Sending request " +request.url() + " on :\n" + request.headers());

        return chain.proceed(request);
    }
}
