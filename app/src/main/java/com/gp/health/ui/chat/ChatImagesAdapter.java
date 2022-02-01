package com.gp.health.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.health.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatImagesAdapter extends RecyclerView.Adapter<ChatImagesAdapter.ChatImageViewHolder> {

    public List<Uri> imagesList;
    private final Context context;

    public ChatImagesAdapter(List<Uri> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new ChatImageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatImageViewHolder holder, final int pos) {

        Uri item = imagesList.get(pos);
        Glide.with(context).load(item).into(holder.chatImage);
        holder.remove.setOnClickListener(view -> removeItem(pos));
    }


    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    private void removeItem(int position) {
        imagesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, imagesList.size());
    }

    @SuppressLint("NonConstantResourceId")
    static class ChatImageViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.chatImage)
        ImageView chatImage;
        @BindView(R.id.remove)
        ImageView remove;

        ChatImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
