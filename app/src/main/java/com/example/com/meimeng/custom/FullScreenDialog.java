package com.example.com.meimeng.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by Administrator on 2015/11/30.
 */
public class FullScreenDialog extends Dialog {

    public FullScreenDialog(Context context, int theme) {
        super(context, theme);
        setOwnerActivity((Activity) context);


    }
}
