package com.example.findmefood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findmefood.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(Restaurant restaurant);
    }

    private final OnItemClickListener listener;
    private LayoutInflater inflater;
    private final String TAG = SearchAdapter.class.getName();
    List<Restaurant> restaurants;

    public SearchAdapter(Context context, List<Restaurant> restaurants, OnItemClickListener listener){
        inflater = LayoutInflater.from(context);
        this.restaurants = restaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = inflater.inflate(R.layout.row_search_item,viewGroup,false);
        SearchItem searchItem = new SearchItem(row);
        System.out.println("Rcv:ViewHolder");
        return searchItem;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //Populate fields.
        ((SearchItem)viewHolder).bind(restaurants.get(i),listener);
//        ((SearchItem)viewHolder).titleTextView.setText(restaurants.get(i).getName());
        System.out.println("OnBindViewHolder");

    }

    @Override
    public int getItemCount() {
        return (!restaurants.isEmpty()) ? restaurants.size():0;
    }

    public class SearchItem extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView restaurantImageView;
        public SearchItem(@NonNull View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.search_item_title);
            restaurantImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        public void bind(final Restaurant restaurant, final OnItemClickListener listener){
            titleTextView.setText(restaurant.getName());
            String image_url = restaurant.getImage_url();
            if(image_url != null && !(image_url.isEmpty())){
                Picasso.get().load(image_url).error(R.mipmap.ic_launcher).placeholder(R.drawable.cr_placeholder).fit().centerCrop().into(restaurantImageView);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(restaurant);
                }
            });
        }
    }

}
