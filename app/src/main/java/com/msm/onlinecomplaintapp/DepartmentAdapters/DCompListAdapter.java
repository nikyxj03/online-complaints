package com.msm.onlinecomplaintapp.DepartmentAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.Dialogs.DeptIgnoreMenu;
import com.msm.onlinecomplaintapp.Dialogs.DeptRegisteredMenu;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Others.DateFormatUtils;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DCompListAdapter extends BaseAdapter {

    private int page;
    private Context mcontext;
    private int mresource;
    private int sm;
    private List<Complaint> complaintList=new ArrayList<>();
    private List<Complaint> objectsList=new ArrayList<>();
    private Activity activity;
    private List<Departments> departmentsList;

    public DCompListAdapter(Context context, int resource, int sm, int page) {
        mcontext=context;
        mresource=resource;
        this.sm=sm;
        this.page=page;
        GlobalApplication.databaseHelper.getDepartmentsList(new OnDataFetchListener<Departments>() {
            @Override
            public void onDataFetched(List<Departments> departments) {
                departmentsList=departments;
            }
        });
    }

    @Override
    public int getCount() {
        return complaintList.size();
    }

    @Override
    public Complaint getItem(int position) {
        return complaintList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(mresource,null);
        final TextView comptitletext1 = convertView.findViewById(R.id.comptitletext1);
        final TextView comptimetext1 = convertView.findViewById(R.id.comptimetext1);
        final TextView compdesctext1=convertView.findViewById(R.id.compdesctext1);
        final TextView supportnotext1=convertView.findViewById(R.id.supportnotext1);
        final LinearLayout complistmainlinear1=convertView.findViewById(R.id.complistmainlinear1);
        final ImageView complistimage1=convertView.findViewById(R.id.complistimage1);
        final CircleImageView authorimageview1=convertView.findViewById(R.id.authorimageview1);
        final TextView authortext1=convertView.findViewById(R.id.authortext1);
        final TextView statustextview1=convertView.findViewById(R.id.statustextview1);

        comptitletext1.setText(complaintList.get(position).getTitle());
        comptimetext1.setText(DateFormatUtils.getRelativeTimeSpanStringShort(mcontext,complaintList.get(position).getTime().toDate().getTime()));
        compdesctext1.setText(complaintList.get(position).getDesc());
        supportnotext1.setText("Support:"+complaintList.get(position).getSupportno());
        if(complaintList.get(position).getCiuri()!=""){
            Glide.with(mcontext.getApplicationContext()).load(complaintList.get(position).getCiuri()).into(complistimage1);
            Glide.with(mcontext.getApplicationContext()).load(complaintList.get(position).getCiuri()).into(complistimage1);
        }
        else {
            complistimage1.setVisibility(View.GONE);
        }

        if (page==((DepartmentActivity)mcontext).getPageRegisteredComplaintsD()){
            complistmainlinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeptRegisteredMenu deptRegisteredMenu=new DeptRegisteredMenu(mcontext,complaintList.get(position),comptimetext1,departmentsList);
                    View view=deptRegisteredMenu.getLayoutInflater().inflate(R.layout.dept_registered_menu,null);
                    deptRegisteredMenu.setContentView(view);
                    deptRegisteredMenu.show();
                }
            });
        }

        if(page==((DepartmentActivity)mcontext).getPageIgnoredComplaints()) {
            complistmainlinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeptIgnoreMenu deptIgnoreMenu = new DeptIgnoreMenu(mcontext, complaintList.get(position), comptitletext1);
                    View view = deptIgnoreMenu.getLayoutInflater().inflate(R.layout.dept_ignored_menu, null);
                    deptIgnoreMenu.setContentView(view);
                    deptIgnoreMenu.show();

                }
            });
        }
        return convertView;
    }

    public void setList(List<Complaint> list){
        complaintList=list;
        notifyDataSetChanged();
    }

}
