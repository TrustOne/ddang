package com.example.administrator.secondlogin.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.administrator.secondlogin.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.List;

public class MyRecyclerAdapter extends  RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private final List<CardItem> mDataList;
    public static RequestManager mGlideRequestManager;
    public MyRecyclerAdapter(List<CardItem> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sub2,viewGroup,false);
        mGlideRequestManager = Glide.with(viewGroup.getContext());

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardItem item = mDataList.get(i);
        viewHolder.comment.setText(item.getComment());
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(item.getTime());

        mGlideRequestManager
                .using(new FirebaseImageLoader())
                .load(mDataList.get(i).getImg())
                .into(viewHolder.imgview);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView comment,time,name;
        ImageView imgview;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.tv_comment_sub2);
            time = itemView.findViewById(R.id.tv_time_comment);
            name = itemView.findViewById(R.id.tv_name_sub);
            imgview = itemView.findViewById(R.id.imageView_sub2);
            ratingBar = itemView.findViewById(R.id.ratingBar_sub);


        }
    }
}
