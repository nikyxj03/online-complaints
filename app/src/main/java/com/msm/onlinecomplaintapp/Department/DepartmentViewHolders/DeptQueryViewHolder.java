package com.msm.onlinecomplaintapp.Department.DepartmentViewHolders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msm.onlinecomplaintapp.R;

public class DeptQueryViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView time;
    private TextView question;
    private RelativeLayout reply;

    public DeptQueryViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.dqv_user_name);
        time=itemView.findViewById(R.id.dqv_time);
        question=itemView.findViewById(R.id.dqv_question);
        reply=itemView.findViewById(R.id.dqv_reply);
    }

    public TextView getTime() {
        return time;
    }

    public TextView getQuestion() {
        return question;
    }

    public TextView getName() {
        return name;
    }

    public RelativeLayout getReply() {
        return reply;
    }
}
