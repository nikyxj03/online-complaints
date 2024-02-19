package com.msm.onlinecomplaintapp.Users.UserAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msm.onlinecomplaintapp.Department.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.Interfaces.OnPosClicked;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.R;

import java.util.List;

public class UDeptListAdapter extends BaseAdapter {
  private int curpos;
  private List<Departments> deptslist;
  private OnPosClicked onPosClicked;
  private Context mContext;

  public UDeptListAdapter(List<Departments> deptslist,int curpos,Context context) {
    this.deptslist=deptslist;
    this.curpos=curpos;
    this.mContext=context;
  }

  @Override
  public int getCount() {
    return deptslist.size();
  }

  @Override
  public Departments getItem(int position) {
    return deptslist.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view=layoutInflater.inflate(R.layout.depspinnerlayout,null);
    final TextView deptnametext=view.findViewById(R.id.deptnametext);
    deptnametext.setText(deptslist.get(position).getName()+"("+deptslist.get(position).getDid()+")");
    if(position==curpos)
      deptnametext.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
    deptnametext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onPosClicked.onSelected(position);
      }
    });

    return view;
  }

  public void dissmiss(OnPosClicked onPosClicked){
    this.onPosClicked=onPosClicked;
  }
}