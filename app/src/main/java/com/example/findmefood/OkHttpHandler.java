package com.example.findmefood;

import android.os.AsyncTask;
import android.util.Log;
import com.example.findmefood.models.ApiKey;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<String,Void,Response> {
    private static final String TAG = OkHttpHandler.class.getName();
    public ApiKey apiKey = new ApiKey("V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx", "Bearer ");
    public interface OkHttpResponse {
        public void processFinished(Response output);
    }
    public OkHttpResponse delegate = null;
    public OkHttpHandler(OkHttpResponse delegate){
        this.delegate = delegate;
    }
    OkHttpClient client;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        client = new OkHttpClient.Builder()
                .addInterceptor(new YelpLoggingInterceptor())
                .build();
    }

    @Override
    protected Response doInBackground(String... params){
        //TODO Maybe the alias to search.

        String flag = params[0], term, latitude, longitude, offset, destination_address, categories;
//        String term = params[0], latitude = params[1], longitude = params[2], offset = params[3], flag;

        HttpUrl url;
//        OkHttpClient client;

        /*For Category API call*/
        if(flag.contains("0")){
            term = params[1];
            latitude = params[2];
            longitude = params[3];
            offset = params[4];
            categories = "restaurants";

            url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.yelp.com")
                    .addPathSegment("v3")
                    .addPathSegment("businesses")
                    .addPathSegment("search")
                    .addQueryParameter("term",term)
                    .addQueryParameter("latitude",latitude)
                    .addQueryParameter("longitude",longitude)
                    .addQueryParameter("limit","50")
                    .addQueryParameter("sort_by", "distance")
                    .addQueryParameter("offset", offset)
                    .addQueryParameter("categories",categories)
                    .build();


        }
        /*For Restaurant API Call*/
        else{
            term = params[1];
            latitude = params[2];
            longitude = params[3];
            offset = params[4];
            categories = "restaurants," + params[5];

            url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.yelp.com")
                    .addPathSegment("v3")
                    .addPathSegment("businesses")
                    .addPathSegment("search")
                    .addQueryParameter("term",term)
                    .addQueryParameter("latitude",latitude)
                    .addQueryParameter("longitude",longitude)
                    .addQueryParameter("limit","50")
                    .addQueryParameter("sort_by", "distance")
                    .addQueryParameter("offset", offset)
                    .addQueryParameter("categories",categories)
                    .build();
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Log.d(TAG,"1st Request: " + request.toString());
        try{
            Response response = client.newCall(request).execute();

            Log.d(TAG,"Received response for " + response.request().url());
            return response;
        }
        catch (Exception e){
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response result) {
        delegate.processFinished(result);
    }

}
