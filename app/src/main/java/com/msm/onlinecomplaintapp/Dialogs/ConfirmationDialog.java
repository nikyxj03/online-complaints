package com.msm.onlinecomplaintapp.Dialogs;

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

    public ConfirmationDialog(Context context,String title,int resid) {
        super(context);
        this.title=title;
        setContentView(resid);
    }

    public ConfirmationDialog(Context context,String title,String desc,int resid) {
        super(context);
        this.title=title;
        this.desc=desc;
        setContentView(resid);
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
        return super.onTouchEvent(event);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view=getLayoutInflater().inflate(layoutResID,null );
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
