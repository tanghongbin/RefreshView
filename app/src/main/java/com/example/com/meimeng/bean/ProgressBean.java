package com.example.com.meimeng.bean;

import android.app.Dialog;
import android.widget.TextView;

/**
 * Created by lx on 2015/10/23.
 */
public class ProgressBean {

    private TextView textView;

    private Dialog dialog;

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
