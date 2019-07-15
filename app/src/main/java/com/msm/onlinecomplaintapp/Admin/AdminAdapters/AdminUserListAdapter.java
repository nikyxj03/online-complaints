package com.msm.onlinecomplaintapp.Admin.AdminAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.msm.onlinecomplaintapp.Admin.AdminViewHolders.AdminUserListViewHolder;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.Interfaces.ItemClickListener;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListAdapter extends RecyclerView.Adapter<AdminUserListViewHolder> {

    private Context mcontext;
    private List<Users> usersList=new ArrayList<>();
    private ItemClickListener<Users> usersItemClickListener;

    public AdminUserListAdapter(Context context, ItemClickListener<Users> itemClickListener){
        mcontext=context;
        usersItemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public AdminUserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.a_users_list_item, parent, false);
        contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        AdminUserListViewHolder viewHolder = new AdminUserListViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminUserListViewHolder holder, int position) {
        holder.getEmail().setText(usersList.get(position).getEmail());
        holder.getName().setText(usersList.get(position).getFullname());
        holder.getUid().setText(usersList.get(position).getUid());
        if(usersList.get(position).getUenable().equals("1")){
            holder.getStatus().setVisibility(View.GONE);
        }
        else {
            holder.getStatus().setVisibility(View.VISIBLE);
            holder.getStatus().setText("Blocked");
        }
        Glide.with(mcontext).asBitmap().load(usersList.get(position).getProfilePhoto()).placeholder(R.mipmap.ananymous_person_round).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.getUserimage().setImageBitmap(ImageConverter.getRoundedCornerBitmap(resource,100));
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        holder.addListener(usersList.get(position),usersItemClickListener);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setList(List<Users> list){
        usersList=list;
        notifyDataSetChanged();
    }

}
