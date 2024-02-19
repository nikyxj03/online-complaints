package com.msm.onlinecomplaintapp.Department.DepartmentActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.msm.onlinecomplaintapp.Department.DepartmentActivity;
import com.msm.onlinecomplaintapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class depttotcompdec extends DepartmentActivity {

  private Toolbar toolbar;

  private LinearLayout tcdimglinear1;
  private TextView tcdheadingtext1;
  private TextView timeauthtext1;
  private TextView tcddesctext1;
  private TextView tcdsnt1;
  private ImageView tcdimg1;
  private TextView authtext1;
  private CircleImageView authimg1;
  private TextView statustext1;
  private LinearLayout tcdstatusmsglinear1;
  private TextView tcdstatusmsg1;

  private HashMap<String,Object> cuComplaintmap;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_depttotcompdec);

    toolbar=findViewById(R.id.d_tcd_toolbar);
    setSupportActionBar(toolbar);

    tcdimglinear1 = findViewById(R.id.tcdimglinear1);
    tcdimg1 = findViewById(R.id.tcdimg1);
    tcddesctext1 = findViewById(R.id.tcddesctext1);
    tcdheadingtext1 = findViewById(R.id.tcdheadingtext1);
    tcdsnt1 = findViewById(R.id.tcdsnt1);
    timeauthtext1 = findViewById(R.id.timeauthtext1);
    authimg1 = findViewById(R.id.authimage1);
    authtext1 = findViewById(R.id.authtext1);
    statustext1 = findViewById(R.id.statustext1);
    tcdstatusmsglinear1 = findViewById(R.id.tcdmsglinear1);
    tcdstatusmsg1 = findViewById(R.id.tcdmsg1);

    cuComplaintmap = (HashMap<String, Object>) getIntent().getSerializableExtra("cuComplaint");

    tcdheadingtext1.setText(cuComplaintmap.get("title").toString());
    tcdheadingtext1.setAllCaps(true);
    if (cuComplaintmap.get("ciuri") != null) {
      if (cuComplaintmap.get("ciuri").toString().length() != 0) {
        tcdimglinear1.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(cuComplaintmap.get("ciuri").toString()).into(tcdimg1);
      } else {
        tcdimg1.setVisibility(View.GONE);
        tcdimglinear1.setVisibility(View.GONE);
      }
    } else {
      tcdimg1.setVisibility(View.GONE);
      tcdimglinear1.setVisibility(View.GONE);
    }

    tcddesctext1.setText(cuComplaintmap.get("desc").toString());
    tcdsnt1.setText("Support:" + String.valueOf((int) cuComplaintmap.get("supportno")));
    SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy | HH:mm");
    Timestamp timestamp = (Timestamp) cuComplaintmap.get("time");
    timeauthtext1.setText(sfd.format(new Date(timestamp.getSeconds() * 1000L)));
    if (cuComplaintmap.get("amode").toString().equals("yes")) {
      authtext1.setText("Ananymous");
    } else {
      authtext1.setText(cuComplaintmap.get("userName").toString());
    }
    if (cuComplaintmap.get("acm").equals("0")) {
      statustext1.setText("Registered");
      statustext1.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_registered_grey_18dp), null, null, null);
    } else if (cuComplaintmap.get("acm").equals("1")) {
      statustext1.setText("Under Watch");
      statustext1.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_underwatch_18dp), null, null, null);
    } else if (cuComplaintmap.get("acm").equals("2")) {
      statustext1.setText("Resolved");
      statustext1.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_solved_18dp), null, null, null);
    } else {
      statustext1.setText("Ignored");
      statustext1.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.ic_ignored_18dp), null, null, null);
    }
    if (cuComplaintmap.get("statement") != null) {
      if (!cuComplaintmap.get("statement").toString().equals("")) {
        tcdstatusmsglinear1.setVisibility(View.VISIBLE);
        tcdstatusmsg1.setText(cuComplaintmap.get("statement").toString());
      }
    }


  }
}