package com.example.com.meimeng.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;


/**
 * Created by wjw
 * 自定义的LinearLayout,用于单选的listView的item
 */
public class SingleSelectView extends LinearLayout implements Checkable {
    private TextView firstView;
    private TextView contentText;
    private CheckBox checkBox;

    public SingleSelectView(Context context) {
        super(context);

        initView(context);
    }
    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.gxzsitemlayout2, this, true);
        firstView = (TextView) v.findViewById(R.id.first_text);
        contentText = (TextView) v.findViewById(R.id.content_text);
        checkBox = (CheckBox) v.findViewById(R.id.check_box);

    }
    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void toggle() {
        checkBox.toggle();
    }

    public void setFirstText(String text) {
        firstView.setText(text);
    }

    public void setContentText(String text) {
        contentText.setText(text);
    }
}
