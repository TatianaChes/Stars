package com.example.starview;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.ViewHolder>{

    interface OnStarClickListener{
        void onStarClick(Star star, int position);
    }
    private  OnStarClickListener onClickListener;
    private LayoutInflater inflater;
    private  List<Star> stars;

    StarAdapter(Context context, List<Star> stars, OnStarClickListener onClickListener)  {
        this.onClickListener = onClickListener;
        this.stars = stars;
        this.inflater = LayoutInflater.from(context);
    }

    public void filterList(ArrayList<Star> filterdNames) {
        this.stars = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public StarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StarAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Star star = stars.get(position);
        Picasso.get().load(star.getPhoto()).into(holder.photoView);
        holder.nameView.setText(star.getName());
        holder.categoryView.setText(star.getCategory());
        holder.ageView.setText(star.getAge());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onStarClick(star, position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return stars.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView photoView;
        final TextView nameView;
        final TextView categoryView;
        final TextView ageView;

        ViewHolder(View view){
            super(view);
            photoView = view.findViewById(R.id.photo);
            nameView = view.findViewById(R.id.name);
            categoryView = view.findViewById(R.id.category);
            ageView = view.findViewById(R.id.age);


        }
    }

}