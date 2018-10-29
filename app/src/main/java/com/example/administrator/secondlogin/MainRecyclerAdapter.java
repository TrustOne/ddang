package com.example.administrator.secondlogin;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ItemViewHolder>{
    List<MainRecyclerItem> items;


    public MainRecyclerAdapter(List<MainRecyclerItem>items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MainRecyclerAdapter.ItemViewHolder holder, int position) {
        String imgaePath = "imagePath";
        String[] imageUrls = {"imagePath1","imagePath2"};
        ImageViewPagerAdapter imageViewPagerAdapter = new ImageViewPagerAdapter(holder.profile.getContext(),imageUrls);

        Picasso.get()
                .load(Uri.parse(imgaePath))
                .into(holder.profile);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                } else
                    view.setVisibility(View.VISIBLE);
            }
        });
        //comment 눌렀을때 comment리스트 볼수있게 액티비티 넘어가기
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                } else
                    view.setVisibility(View.VISIBLE);
            }
        });

        holder.nickName.setText("닉네임");
        holder.likeText.setText("누구누구 라이크 눌렀습니다");
        holder.desc.setText("#농산물 #쌀 #사과 #꿀사과");
        holder.comments.setText("아이디 "+ "코멘트");
        holder.contentTime.setText("작성 시간");
        holder.images.setAdapter(imageViewPagerAdapter);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView profile, like, comment;
        TextView nickName, likeText, desc, comments, contentTime;
        ViewPager images;

        public ItemViewHolder(View itemView){
            super(itemView);
            profile = itemView.findViewById(R.id.profile_photo);
            like = itemView.findViewById(R.id.image_heart_red);
            comment = itemView.findViewById(R.id.speech_bubble);

            nickName = itemView.findViewById(R.id.username);
            likeText = itemView.findViewById(R.id.like_num);
            desc = itemView.findViewById(R.id.descript);
            comments = itemView.findViewById(R.id.image_comments_link);
            contentTime = itemView.findViewById(R.id.image_time_posted);

            images = itemView.findViewById(R.id.item_imgs);

        }
    }
}
