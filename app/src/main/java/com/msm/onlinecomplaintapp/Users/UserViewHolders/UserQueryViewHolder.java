package com.msm.onlinecomplaintapp.Users.UserViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msm.onlinecomplaintapp.Common.DateFormatUtils;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.R;

public class UserQueryViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView time;
    private TextView question;
    private TextView reply;

    public UserQueryViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.uqv_dept_name);
        time=itemView.findViewById(R.id.uqv_time);
        question=itemView.findViewById(R.id.uqv_question);
        reply=itemView.findViewById(R.id.uqv_reply);
    }

    public TextView getName() {
        return name;
    }

    public TextView getQuestion() {
        return question;
    }

    public TextView getReply() {
        return reply;
    }

    public TextView getTime() {
        return time;
    }
}
