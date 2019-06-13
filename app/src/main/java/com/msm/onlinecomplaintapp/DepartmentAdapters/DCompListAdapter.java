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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.Dialogs.DeptArchivemenu;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Others.DateFormatUtils;
import com.msm.onlinecomplaintapp.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DCompListAdapter extends BaseAdapter {

    private int page;
    private Context mcontext;
    private int mresource;
    private int sm;
    private List<Complaint> complaintList=new ArrayList<>();
    private List<Complaint> objectsList=new ArrayList<>();
    private Activity activity;

    public DCompListAdapter(Context context, int resource, int sm, int page) {
        mcontext=context;
        mresource=resource;
        this.sm=sm;
        this.page=page;
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

        if(page==((DepartmentActivity)mcontext).getPageIgnoredComplaints()) {
            complistmainlinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeptArchivemenu deptArchivemenu = new DeptArchivemenu(mcontext, complaintList.get(position), comptitletext1);
                    View view = deptArchivemenu.getLayoutInflater().inflate(R.layout.adminarchivemenu, null);
                    deptArchivemenu.setContentView(view);
                    deptArchivemenu.show();

                }
            });
        }

        return convertView;
    }


    public void sort(List<Complaint> object,int sm){
        Calendar comptime=Calendar.getInstance();
        ArrayList<HashMap<String,Object>> mtdlist =new ArrayList<>();
        HashMap<String,Object> tempmap1=new HashMap<>();
        complaintList.clear();
        if(sm==0){
            Toast.makeText(mcontext,"0",Toast.LENGTH_LONG).show();
            for(int i7=0;i7<object.size();i7++) {
                String comptimes = object.get(i7).getTime().toDate().toString();
               /* comptime.set(Calendar.HOUR, (int) (Double.parseDouble(comptimes.substring((int) (0), (int) (2)))));
                comptime.set(Calendar.MINUTE, (int) (Double.parseDouble(comptimes.substring((int) (3), (int) (5)))));
                comptime.set(Calendar.SECOND, (int) (00));
                comptime.set(Calendar.DAY_OF_MONTH, (int) (Double.parseDouble(comptimes.substring((int) (6), (int) (8)))));
                comptime.set(Calendar.MONTH, (int) (Double.parseDouble(comptimes.substring((int) (9), (int) (11))) - 1));   mnnmnlmkl 8u hu
                comptime.set(Calendar.YEAR, (int) (Double.parseDouble(comptimes.substring((int) (12), (int) (16)))));
                int ttd=(Double.valueOf(cutime.getTimeInMillis()-comptime.getTimeInMillis()).intValue());*/
                int ttd=0;
                tempmap1=new HashMap<>();
                tempmap1.put("pos",i7);
                tempmap1.put("val",ttd);
                mtdlist.add(tempmap1);
            }
            for(int i7=0;i7<object.size();i7++){
                int min=(int)Double.parseDouble(mtdlist.get(0).get("val").toString());
                int temppos=(int)Double.parseDouble(mtdlist.get(0).get("pos").toString());
                int temppos1=0;
                for(int j7=0;j7<mtdlist.size();j7++){
                    if((int)Double.parseDouble(mtdlist.get(j7).get("val").toString())<min){
                        min=(int)Double.parseDouble(mtdlist.get(j7).get("val").toString());
                        temppos=(int)Double.parseDouble(mtdlist.get(j7).get("pos").toString());
                        temppos1=j7;
                    }
                }
                complaintList.add(object.get(temppos));
                mtdlist.remove(temppos1);
            }

        }
        else{
            Toast.makeText(mcontext,"1",Toast.LENGTH_LONG).show();
            for (int i7=0;i7<object.size();i7++){
                tempmap1=new HashMap<>();
                tempmap1.put("pos",i7);
                tempmap1.put("val",object.get(i7).getSupportno());
                mtdlist.add(tempmap1);
            }

            for(int i7=0;i7<object.size();i7++){
                int max=0;
                int temppos=(int)Double.parseDouble(mtdlist.get(0).get("pos").toString());
                int temppos1=0;
                for (int j7=0;j7<mtdlist.size();j7++){
                    if(max<(int)Double.parseDouble(mtdlist.get(j7).get("val").toString())){
                        max=(int)Double.parseDouble(mtdlist.get(j7).get("val").toString());
                        temppos=(int)Double.parseDouble(mtdlist.get(j7).get("pos").toString());
                        temppos1=j7;
                    }
                }
                complaintList.add(object.get(temppos));
                mtdlist.remove(temppos1);
            }

        }
    }

    public void changesm(int sm){
        this.sm=sm;
        sort(objectsList,sm);
        notifyDataSetChanged();
    }

    public void setList(List<Complaint> list){
        objectsList=list;
        sort(objectsList,sm);
        notifyDataSetChanged();
    }

    public void addSMListener(){
    }

}
