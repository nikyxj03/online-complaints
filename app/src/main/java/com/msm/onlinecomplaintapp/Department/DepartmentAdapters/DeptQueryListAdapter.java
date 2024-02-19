package com.msm.onlinecomplaintapp.Department.DepartmentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.msm.onlinecomplaintapp.Common.DateFormatUtils;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.DeptQueryActivity;
import com.msm.onlinecomplaintapp.Department.DepartmentViewHolders.DeptQueryViewHolder;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserViewHolders.UserQueryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DeptQueryListAdapter extends RecyclerView.Adapter<DeptQueryViewHolder> {

    private Context mcontext;
    private List<UserQuery> queryList=new ArrayList<>();

    public DeptQueryListAdapter(Context context){
        mcontext=context;
    }

    @Override
    public DeptQueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.dept_query_view, parent, false);
        return new DeptQueryViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(DeptQueryViewHolder holder, final int position) {
        holder.getName().setText(queryList.get(position).getUserName());
        holder.getQuestion().setText(queryList.get(position).getQuestion());
        holder.getTime().setText(DateFormatUtils.getRelativeTimeSpanStringShort(mcontext,queryList.get(position).getTimestamp().toDate().getTime()));
        holder.getReply().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mcontext,R.style.BottomSheetDialog);
                View view=bottomSheetDialog.getLayoutInflater().inflate(R.layout.status_message_layout,null);
                final TextView title=view.findViewById(R.id.smheading);
                final EditText reply=view.findViewById(R.id.smedit);
                final ImageButton sendbutton=view.findViewById(R.id.smsendbutton);
                bottomSheetDialog.setContentView(view);
                title.setText("REPLY");
                sendbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(reply.getText().toString().length()>5){
                            bottomSheetDialog.dismiss();
                            ((DeptQueryActivity)mcontext).showProgress("Sending Message..");
                            queryList.get(position).setReply(reply.getText().toString());
                            GlobalApplication.databaseHelper.updateQuery(queryList.get(position), new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    ((DeptQueryActivity)mcontext).hideProgress();
                                    if(success){
                                        Toast.makeText(mcontext,"Success",Toast.LENGTH_LONG).show();
                                        queryList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(mcontext,"Failed",Toast.LENGTH_LONG).show();
                                        queryList.get(position).setReply(null);
                                    }
                                }
                            });
                        }
                        else {
                            reply.setError("Reply is very small");
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });
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
