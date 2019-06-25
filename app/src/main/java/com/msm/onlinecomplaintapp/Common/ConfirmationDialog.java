package com.msm.onlinecomplaintapp.Common;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.R;

public class ConfirmationDialog extends Dialog {

    private String title="";
    private String desc="";

    private Button nobutton;
    private Button yesbutton;

    public ConfirmationDialog(Context context,String title) {
        super(context);
        this.title=title;
        this.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
    }

    public ConfirmationDialog(Context context,String title,String desc) {
        super(context);
        this.title=title;
        this.desc=desc;
        this.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {
        return false;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        yesbutton=view.findViewById(R.id.cdyesbutton);
        nobutton=view.findViewById(R.id.cdnobutton);
        TextView titletext=view.findViewById(R.id.cdtitletext);
        TextView desctext=view.findViewById(R.id.cddescriptiontext);
        titletext.setText(title);
        if(desc!=""){
            desctext.setText(desc);
        }
    }


    public void choicelistener(final CDOnClick cdOnClick){
        yesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdOnClick.OnButtonClicked(true);
            }
        });
        nobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdOnClick.OnButtonClicked(false);
            }
        });
    }


}
