package com.example.findmefood;

import android.os.AsyncTask;
import android.util.Log;

import com.example.findmefood.models.ApiKey;

import java.io.Serializable;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<String,Void,String> {
    private static final String TAG = OkHttpHandler.class.getName();
    private static final String TOKEN = "V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx";
    private static final String AUTH_TYPE = "Bearer";
    public ApiKey apiKey = new ApiKey("V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx", "Bearer ");
    OkHttpClient clientTester = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();

    OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... params){
        HttpUrl url = new HttpUrl.Builder()
                .scheme(params[0])
                .host("api.yelp.com")
                .addPathSegment("v3")
                .addPathSegment("businesses")
                .addPathSegment("search")
                .addQueryParameter("term","restaurants")
                .addQueryParameter("latitude","37.72856")
                .addQueryParameter("longitude","-122.47688")
                .build();

//        HttpUrl testUrl = new HttpUrl.Builder()
//                .scheme(params[0])
//                .host("httpbin.org")
//                .addPathSegment("headers")
//                .build();

        Request request = new Request.Builder()
                //Authorization works for postman not working here. Why?
                .url(url)
                .get()
                .build();

        Log.d(TAG,"Request: " + request.toString());
        try{
            Response response = clientTester.newCall(request).execute();
            return response.body().string();
        }
        catch (Exception e){
            Log.d(TAG, "EXCEPTION ON RESPONSE");
        }

        return null;
    }
}
