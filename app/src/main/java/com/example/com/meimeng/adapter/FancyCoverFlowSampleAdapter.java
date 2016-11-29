/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.com.meimeng.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.fancycoverflow.FancyCoverFlow;
import com.example.com.meimeng.fancycoverflow.FancyCoverFlowAdapter;
import com.example.com.meimeng.gson.gsonbean.ExploreListItem;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.List;

public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {

    private Context context;
    private List<ExploreListItem> datalist;

    private DisplayMetrics dm;
    public FancyCoverFlowSampleAdapter(Context context, List<ExploreListItem> objects) {
        this.context = context;
        this.datalist = objects;
        dm=new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    //��������
    public void refresh(List<ExploreListItem> objects) {
        this.datalist = objects;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public ExploreListItem getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (reuseableView == null) {
            reuseableView = LayoutInflater.from(context).inflate(R.layout.exploregriditem_f, null);
            FancyCoverFlow.LayoutParams params = new FancyCoverFlow.LayoutParams((dm.widthPixels*17/24), (dm.heightPixels*3/4));//屏幕适配
            reuseableView.setLayoutParams(params);
            viewHolder = new ViewHolder();
            viewHolder.common_name__explore_f=(TextView) reuseableView.findViewById(R.id.common_name__explore_f);
            viewHolder.common_age__explore_f=(TextView) reuseableView.findViewById(R.id.common_age__explore_f);
            viewHolder.common_height__explore_f=(TextView) reuseableView.findViewById(R.id.common_height__explore_f);//��String���ֶ�Ҫת�������ӿ��ĵ�
            viewHolder.common_location__explore_f=(TextView) reuseableView.findViewById(R.id.common_location__explore_f);
            viewHolder.common_vip_lvl_text__explore_f=(TextView) reuseableView.findViewById(R.id.common_vip_lvl_text__explore_f);
            viewHolder.common_follow_num_text__explore_f=(TextView) reuseableView.findViewById(R.id.common_follow_num_text__explore_f);
            //viewHolder.text_lastlogintime__explore_f=(TextView) reuseableView.findViewById(R.id.text_lastlogintime__explore_f);
            viewHolder.imageview_explore_f=(ImageView) reuseableView.findViewById(R.id.imageview_explore_f);
            viewHolder.common_vip_lvl_image__explore_f=(ImageView) reuseableView.findViewById(R.id.common_vip_lvl_image__explore_f);
            viewHolder.common_follow_num_image__explore_f=(ImageView) reuseableView.findViewById(R.id.common_follow_num_image__explore_f);
            reuseableView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) reuseableView.getTag();
            resetViewHolder(viewHolder);
        }
        ExploreListItem data = datalist.get(i);


       /* if (data.getIdeStatus() == 1) {
            shenfen.setImageResource(R.drawable.user_ico);
        }
        if (data.getProStatus() == 1) {
            money.setImageResource(R.drawable.money_ico);
        }
        if (data.getMarStatus() == 1) {
            marry.setImageResource(R.drawable.marriage_ico);
        }
        if (data.getMarStatus() == 1) {
            education.setImageResource(R.drawable.education_ico);
        }
        if (data.getMarStatus() == 1) {
            work.setImageResource(R.drawable.work_ico);
        }*/

        //��֤��Ϣ
       /* LinearLayout marryLyaout = (LinearLayout) convertView.findViewById(R.id.imageview_marry_explore_l);
        if (data.getMarStatus() == 1) {
            marryLyaout.setVisibility(View.VISIBLE);
        }
        LinearLayout moneyLyaout = (LinearLayout) convertView.findViewById(R.id.imageview_money_explore_l);
        if (data.getProStatus() == 1) {
            moneyLyaout.setVisibility(View.VISIBLE);
        }
        LinearLayout shengfenLyaout = (LinearLayout) convertView.findViewById(R.id.imageview_shengfen_explore_l);
        if (data.getIdeStatus() == 1) {
            shengfenLyaout.setVisibility(View.VISIBLE);
        }
        LinearLayout vollegeLyaout = (LinearLayout) convertView.findViewById(R.id.imageview_vollege_explore_l);
        if (data.getEduStatus() == 1) {
            vollegeLyaout.setVisibility(View.VISIBLE);
        }
        LinearLayout workLyaout = (LinearLayout) convertView.findViewById(R.id.imageview_work_explore_l);
        if (data.getJobStatus() == 1) {
            workLyaout.setVisibility(View.VISIBLE);
        }*/

        //�������
        StringBuffer namestr = new StringBuffer();

        if(!TextUtils.isEmpty(data.getFristName())) {
//            if(name.matches("^([A-Za-z]+)$")){
//                viewHolder.text_name_explore.setText(Utils.setString(name, 7));
//            }else {
//                viewHolder.text_name_explore.setText(Utils.setString(name, 3));
//            }
            namestr.append(data.getFristName());
        }else {
            namestr.append("");
        }

        int sex = data.getSex();

        switch (sex){
            case 0:
                namestr.append("先生");
                break;
            case 1://Ůʿ
                namestr.append("女士");
                break;
            default:
                break;
        }

        viewHolder.common_name__explore_f.setText("<  "+Utils.setString(namestr.toString(), 7)+"  >");
        viewHolder.common_age__explore_f.setText(String.valueOf(data.getAge())+"岁");//��String���ֶ�Ҫת�������ӿ��ĵ�
        viewHolder.common_height__explore_f.setText(String.valueOf(data.getHeight())+"CM");
        viewHolder.common_follow_num_text__explore_f.setText(String.valueOf(data.getFollowerNum()));
        switch (data.getVipLevel()){
            case 0:
                viewHolder.common_vip_lvl_text__explore_f.setText("普通会籍");
                viewHolder.common_vip_lvl_image__explore_f.setImageDrawable(context.getResources().getDrawable(R.drawable.vip_0));
                break;
            case 1:
                viewHolder.common_vip_lvl_text__explore_f.setText("银牌会籍");
                viewHolder.common_vip_lvl_image__explore_f.setImageDrawable(context.getResources().getDrawable(R.drawable.vip_1));
                break;
            case 2:
                viewHolder.common_vip_lvl_text__explore_f.setText("金牌会籍");
                viewHolder.common_vip_lvl_image__explore_f.setImageDrawable(context.getResources().getDrawable(R.drawable.vip_2));
                break;
            case 3:
                viewHolder.common_vip_lvl_text__explore_f.setText("黑牌会籍");
                viewHolder.common_vip_lvl_image__explore_f.setImageDrawable(context.getResources().getDrawable(R.drawable.vip_3));
                break;
            default:
                break;
        }
        if(!TextUtils.isEmpty(data.getCity())) {
            viewHolder.common_location__explore_f.setText(data.getCity());
        }else{
            viewHolder.common_location__explore_f.setText("  未知  ");
        }
       /* if(!TextUtils.isEmpty(data.getLastLoginTime())) {
            viewHolder.text_lastlogintime__explore_f.setText(data.getLastLoginTime());
        }*/
        Log.i("msg", "num   ::  " + data.getHeadPic());
        InternetUtils.getPicIntoView(188, 170, viewHolder.imageview_explore_f, data.getHeadPic());//����ͼƬ

        return reuseableView;

    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.common_name__explore_f.setText(null);
        viewHolder.common_age__explore_f.setText(null);
        viewHolder.common_height__explore_f.setText(null);//��String���ֶ�Ҫת�������ӿ��ĵ�
        viewHolder.common_location__explore_f.setText(null);
        viewHolder.common_vip_lvl_text__explore_f.setText(null);
        viewHolder.common_follow_num_text__explore_f.setText(null);
        viewHolder.text_lastlogintime__explore_f.setText(null);
        viewHolder.imageview_explore_f.setImageBitmap(null);
        viewHolder.common_vip_lvl_image__explore_f.setImageBitmap(null);
        viewHolder.common_follow_num_image__explore_f.setImageBitmap(null);
    }

    private class ViewHolder {
        private ImageView imageview_explore_f;
        private ImageView common_vip_lvl_image__explore_f;
        private ImageView common_follow_num_image__explore_f;
        private TextView common_name__explore_f;
        private TextView common_age__explore_f;
        private TextView common_height__explore_f;
        private TextView common_location__explore_f;
        private TextView common_vip_lvl_text__explore_f;
        private TextView common_follow_num_text__explore_f;
        private TextView text_lastlogintime__explore_f;
    }
}
