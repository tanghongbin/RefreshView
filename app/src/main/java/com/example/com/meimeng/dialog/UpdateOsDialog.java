package com.example.com.meimeng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.util.ActivityCollector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/15.
 */
public class UpdateOsDialog extends Dialog {

    private Context mContext;
    private  Handler mHandler;

    @Bind(R.id.prompt_text)
    TextView promptText;

    @Bind(R.id.bottom_text)
    TextView bottomText;

    @Bind(R.id.cancle_text)
    TextView cancleText;



    public UpdateOsDialog(Context context){
        super(context);
        mContext= context;
    }

    public UpdateOsDialog(Context context,Handler mHandler,int theme){
        super(context, theme);
        this.mContext = context;
        this.mHandler = mHandler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_prompt_f);
        ButterKnife.bind(this);

        promptText.setText("已检测到新版本");
        bottomText.setText("立即更新");
        setCancelable(false);
        cancleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mHandler.sendEmptyMessage(555);
            }
        });

    }

    @OnClick(R.id.prompt_btn)
    protected void updateListener(){
        dismiss();
        ActivityCollector.finishAll();
        String mAddress = "market://details?id=" + mContext.getPackageName();
        Intent marketIntent = new Intent("android.intent.action.VIEW");
        marketIntent.setData(Uri.parse(mAddress));
        mContext.startActivity(marketIntent);
    }

}
