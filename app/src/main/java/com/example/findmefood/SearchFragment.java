package com.example.findmefood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findmefood.models.Restaurant;
import com.example.findmefood.models.SearchRestaurantsResults;
import com.example.findmefood.utility.OkHttpHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    List<Restaurant> restaurants;
    private static final String TAG = SearchFragment.class.getName();
    private static final String URL = "URL";
    private static String FIND_BY_NAME_FLAG = "2";
    private static Gson gson;
    private static View view;
    private static OkHttpHandler okHttpHandler;
    private ImageView search_button;
    private EditText search_input,location_input;
    private TextView progress_text;
    private ProgressBar search_progress_bar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);
        gson = new Gson();
        search_button = view.findViewById(R.id.start_search);
        search_input = view.findViewById(R.id.search_input);
        location_input = view.findViewById(R.id.location_input);
        progress_text = view.findViewById(R.id.progress_text);
        search_progress_bar = view.findViewById(R.id.search_progress);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_progress_bar.setVisibility(View.VISIBLE);
                search_button.setVisibility(View.INVISIBLE);
                progress_text.setHint("Please wait a moment...");
                search();
            }
        });

        return view;
    }

    private void search(){
        hideKeyboard(getActivity());
        String search_term = (!(TextUtils.isEmpty(search_input.getText())))? search_input.getText().toString():"Restaurant";
        String loc = (!(TextUtils.isEmpty(location_input.getText()))) ? location_input.getText().toString():"San Francisco";
        search_input.setText(search_term);
        location_input.setText(loc);

        Log.d(TAG, "Search term : " + search_term + " Location : " + loc);

        okHttpHandler = new OkHttpHandler(new OkHttpHandler.OkHttpResponse() {
            @Override
            public void processFinished(Response output) {
                try{
                    if (output != null){
                        String body = output.body().string();
                        Log.d(TAG,"Response data: " + body);
                        if(!body.isEmpty()) {
                            SearchRestaurantsResults searchRestaurants = gson.fromJson(body, SearchRestaurantsResults.class);
                            restaurants = searchRestaurants.getBusinesses();
                            recyclerView=(RecyclerView) view.findViewById(R.id.search_recyclerview);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(new SearchAdapter(getActivity(), restaurants, new SearchAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Restaurant restaurant) {
                                    String url = restaurant.getUrl();
                                    Intent yelpSiteIntent = new Intent(getActivity().getBaseContext(), WebViewActivity.class);
                                    yelpSiteIntent.putExtra(URL,url);
                                    startActivity(yelpSiteIntent);
                                }
                            }));
                            search_progress_bar.setVisibility(View.INVISIBLE);
                            progress_text.setVisibility(View.INVISIBLE);
                            search_button.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Log.d(TAG,"Null body");
                    }
                }
                catch (IOException e){
                    Log.e(TAG,e.toString());
                }
            }
        });
        okHttpHandler.execute(FIND_BY_NAME_FLAG,search_term,loc);
    }

    //Function taken from stack overflow to hide keyboard.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(okHttpHandler != null && okHttpHandler.getStatus() == AsyncTask.Status.RUNNING) {
            okHttpHandler.cancel(true);
            Log.d(TAG, "Cancelled OkHTTPAsync");
        }

    }
}
