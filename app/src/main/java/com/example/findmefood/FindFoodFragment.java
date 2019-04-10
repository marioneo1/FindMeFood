package com.example.findmefood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

public class FindFoodFragment extends Fragment {
    private static final String TAG = OkHttpHandler.class.getName();
    private static final String TOKEN = "V1zoNqS9vcPyhFETp-mWNz49yuiRsJo9fBzpc1Ib2ONFsszZHIfT6-wG7gLS9Ok_ZrFT2sLiHDkILDW0Al-LKlo8O2fy_XfJFzVQPy8AxavUw-i-gEMeyjaN6BzMWnYx";
    String response;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findfood,container,false);

        Button start_ffood_button = view.findViewById(R.id.startffood);
        start_ffood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"FFoodFragment Start OKHTTP");
                String url = "https";

//                    new AsyncTask<String, Void, String>(){
//                        @Override
//                        protected String doInBackground(String... values){
//                            YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
//                            try {
//                                YelpFusionApi yelpFusionApi = apiFactory.createAPI(TOKEN);
//                                Map<String, String> params = new HashMap<>();
//                                Log.d(TAG, "ASYNC doInBackground");
//                                params.put("term", "indian food");
//                                params.put("latitude", "40.581140");
//                                params.put("longitude", "-111.914184");
//
//                                Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
//                                SearchResponse response = call.execute().body();
//                                int totalNumberOfResult = response.getTotal();  // 3
//
//                                ArrayList<Business> businesses = response.getBusinesses();
//                                String businessName = businesses.get(0).getName();  // "JapaCurry Truck"
//                                Double rating = businesses.get(0).getRating();  // 4.0
//                                Log.d(TAG, "Business name: " + businessName + " Rating: " + rating);
//
//                            }
//                            catch (IOException e)
//                            {
//                                Log.d(TAG, "IOException");
//                            }
//                            return null;
//                    }
//                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Hello World!");;



                OkHttpHandler okHttpHandler = new OkHttpHandler();
                try {
                    response = okHttpHandler.execute(url).get();
                }
                catch (InterruptedException e){

                }
                catch (ExecutionException e) {
                }
                Log.d(TAG,"Output response : " + response);
            }
        });
        return view;
//        return inflater.inflate(R.layout.fragment_findfood,null);
    }
}
