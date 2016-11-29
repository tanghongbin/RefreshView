package com.example.com.meimeng.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 010 on 2015/7/24.
 */
public class EditFragment extends Fragment {

    @Bind(R.id.edit_cancel_text)
    TextView cancelText;

    @Bind(R.id.edit_text)
    TextView editText;

    @Bind(R.id.edit_save_text)
    TextView saveText;

    @Bind(R.id.edit_name_edit)
    EditText nameEdit;

    @Bind(R.id.edit_content_edit)
    EditText contentEdit;

    private Context context;

    private String mTitle,mContent;

    public interface OnEditListener{
        public void cancelListener();

        public void saveListener(String name,String content);
    }

    public EditFragment(){

    }

    @SuppressLint("ValidFragment")
    public EditFragment(String tilte,String content){
        this.mTitle=tilte;
        this.mContent=content;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit,container,false);
        ButterKnife.bind(this, view);
        MeiMengApplication.currentContext=getActivity();
        if(mTitle!=null)
        nameEdit.setText(mTitle);
        if(mContent!=null)
        contentEdit.setText(mContent);
        context = getActivity();

        Bundle bundle = getArguments();
        if (bundle!=null){
            String type = bundle.getString("type");
            contentEdit.setVisibility(View.GONE);
            nameEdit.setText(null);
            if (type.equals("nickname")){
                nameEdit.setHint("请输入昵称");
            }else if (type.equals("email")){
                nameEdit.setHint("请输入电子邮件");
            }else if (type.equals("realname")){
                nameEdit.setHint("请输入真实姓名");
            }else if (type.equals("wxNo")){
                nameEdit.setHint("请输入微信号");
            }else if (type.equals("nationality")){
                nameEdit.setHint("请输入所属国籍");
            }else if (type.equals("carBrand")){
                nameEdit.setHint("请输入汽车品牌");
            }else if (type.equals("graduateCollege")){
                nameEdit.setHint("请输入毕业院校");
            }else if (type.equals("companyIndustry")){
                nameEdit.setHint("请输入公司行业");
            }else if (type.equals("companyName")){
                nameEdit.setHint("请输入公司名字");
            }else if (type.equals("profession")){
                nameEdit.setHint("请输入专业信息");
            }else if (type.equals("religion")){
                nameEdit.setHint("请输入宗教信仰");
            }
        }
        return view;
    }

    @OnClick(R.id.edit_cancel_text)
    void cancelListener(){
        ((OnEditListener)context).cancelListener();
    }

    @OnClick(R.id.edit_save_text)
    void saveListener(){
        ((OnEditListener)context).saveListener(nameEdit.getText().toString(),contentEdit.getText().toString());
        nameEdit.setText(null);
    }

}
