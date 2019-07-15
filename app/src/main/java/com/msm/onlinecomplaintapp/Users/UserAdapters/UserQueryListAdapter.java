package com.msm.onlinecomplaintapp.Users.UserAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.msm.onlinecomplaintapp.Common.DateFormatUtils;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserViewHolders.UserQueryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class UserQueryListAdapter extends RecyclerView.Adapter<UserQueryViewHolder> {

    private Context mcontext;
    private List<UserQuery> queryList=new ArrayList<>();

    public UserQueryListAdapter(Context context){
        mcontext=context;
    }

    @Override
    public UserQueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.user_query_view, parent, false);
        return new UserQueryViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(UserQueryViewHolder holder, int position) {
        holder.getName().setText(queryList.get(position).getDeptName());
        holder.getTime().setText(DateFormatUtils.getRelativeTimeSpanStringShort(mcontext,queryList.get(position).getTimestamp().toDate().getTime()));
        holder.getQuestion().setText(queryList.get(position).getQuestion());
        if(queryList.get(position).getReply()==null)
            holder.getReply().setText("REPLY: No Reply");
        else
            holder.getReply().setText("REPLY: "+queryList.get(position).getReply());
    }

    @Override
    public int getItemCount() {
        return queryList.size();
    }

    public void setList(List<UserQuery> list){
        this.queryList=list;
        notifyDataSetChanged();
    }
}
