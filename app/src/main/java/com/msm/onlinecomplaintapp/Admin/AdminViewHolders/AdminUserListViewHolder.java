package com.msm.onlinecomplaintapp.Admin.AdminViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msm.onlinecomplaintapp.Interfaces.ItemClickListener;
import com.msm.onlinecomplaintapp.Interfaces.OnClick;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUserListViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView userimage;
    private TextView email;
    private TextView uid;
    private TextView name;
    private TextView status;
    private View view;

    public AdminUserListViewHolder(@NonNull View itemView) {
        super(itemView);
        userimage=itemView.findViewById(R.id.aul_profile_image);
        email=itemView.findViewById(R.id.aul_profile_email);
        uid=itemView.findViewById(R.id.aul_profile_uid);
        name=itemView.findViewById(R.id.aul_profile_name);
        status=itemView.findViewById(R.id.aul_profile_enable);
        view=itemView;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public CircleImageView getUserimage() {
        return userimage;
    }

    public TextView getEmail() {
        return email;
    }

    public TextView getName() {
        return name;
    }

    public TextView getStatus() {
        return status;
    }

    public TextView getUid() {
        return uid;
    }

    public void setEmail(TextView email) {
        this.email = email;
    }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public void setUid(TextView uid) {
        this.uid = uid;
    }

    public void setUserimage(CircleImageView userimage) {
        this.userimage = userimage;
    }

    public void addListener(final Users users, final ItemClickListener<Users> listener){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(users);
            }
        });
    }
}
