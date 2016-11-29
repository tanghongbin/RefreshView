package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lx on 2015/8/10.
 */
public class ReportFragment extends Fragment {

    public interface ReportDialog{

        public void cancel() ;

        public void Report( int type ) ;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_dialog,container,false) ;

        ButterKnife.bind(this, view);

        MeiMengApplication.currentContext=getActivity();
        return view;
    }

    @OnClick(R.id.report_text1)
    public void onClickReportText1(){

        ((ReportDialog)getActivity()).Report(1);

    }

    @OnClick(R.id.report_text2)
    public void onClickReportText2(){

        ((ReportDialog)getActivity()).Report(2);

    }

    @OnClick(R.id.report_text3)
    public void onClickReportText3(){

        ((ReportDialog)getActivity()).Report(3);

    }

    @OnClick(R.id.report_text4)
    public void onClickReportText4(){

        ((ReportDialog)getActivity()).Report(4);

    }

    @OnClick(R.id.report_text5)
    public void onClickReportText5(){

        ((ReportDialog)getActivity()).Report(5);

    }
    @OnClick(R.id.report_text6)
    public void onClickReportText6(){

        ((ReportDialog)getActivity()).Report(6);

    }

    @OnClick(R.id.report_cancel)
    public void onClickReportCancel(){

        ((ReportDialog)getActivity()).cancel();
    }

}
