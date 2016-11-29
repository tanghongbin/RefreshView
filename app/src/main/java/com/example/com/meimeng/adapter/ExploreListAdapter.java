package com.example.com.meimeng.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.gsonbean.ExploreListItem;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.List;

/**
 * 发现页list的适配器
 */

public class ExploreListAdapter extends BaseAdapter {

    private Context context;
    private List<ExploreListItem> datalist;

    public ExploreListAdapter(Context context, List<ExploreListItem> objects) {
        this.context = context;
        this.datalist = objects;
    }

    //更新数据
    public void refresh(List<ExploreListItem> objects) {
        this.datalist = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (datalist == null)
            return 0;
        return datalist.size();
    }

    @Override
    public ExploreListItem getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.exploregriditem, null);
            viewHolder = new ViewHolder();
            //获取ID
            viewHolder.text_name_explore = (TextView) convertView.findViewById(R.id.text_name_explore);
//            viewHolder.text_sex_explore = (TextView) convertView.findViewById(R.id.text_sex_explore);
            viewHolder.text_age_explore = (TextView) convertView.findViewById(R.id.text_age_explore);
            viewHolder.text_height_explore = (TextView) convertView.findViewById(R.id.text_height_explore);
            viewHolder.text_city_explore = (TextView) convertView.findViewById(R.id.text_city_explore);
            viewHolder.text_like_explore = (TextView) convertView.findViewById(R.id.text_like_explore);
            viewHolder.imageview_explore = (ImageView) convertView.findViewById(R.id.imageview_explore);
           // viewHolder.text_lastlogintime_explore = (TextView) convertView.findViewById(R.id.text_lastlogintime_explore);

    /*    //认证信息
        ImageView shenfen = (ImageView) convertView.findViewById(R.id.imageview_shenfen_explore);
        ImageView money = (ImageView) convertView.findViewById(R.id.imageview_money_explore);
        ImageView marry = (ImageView) convertView.findViewById(R.id.imageview_marry_explore);
        ImageView education = (ImageView) convertView.findViewById(R.id.imageview_education_explore);
        ImageView work = (ImageView) convertView.findViewById(R.id.imageview_work_explore);*/

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }
        ExploreListItem data = datalist.get(position);


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

        //认证信息
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

        //填充数据
        StringBuffer namestr = new StringBuffer();

        if(!TextUtils.isEmpty(data.getFristName())) {
//            if(name.matches("^([A-Za-z]+)$")){
//                viewHolder.text_name_explore.setText(Utils.setString(name, 7));
//            }else {
//                viewHolder.text_name_explore.setText(Utils.setString(name, 3));
//            }
            namestr.append(data.getFristName());
        }else {
            namestr.append(" ");
        }

        int sex = data.getSex();

        switch (sex){
            case 0://男士
            namestr.append("先生");
                break;
            case 1://女士
            namestr.append("女士");
                break;
            default:
                break;
        }

        viewHolder.text_name_explore.setText(Utils.setString(namestr.toString(), 5));
        viewHolder.text_age_explore.setText(String.valueOf(data.getAge())+"岁");//非String的字段要转换，看接口文档
        viewHolder.text_height_explore.setText(data.getHeight()==0?"未知":data.getHeight()+"CM");

        if(!TextUtils.isEmpty(data.getCity())) {
            viewHolder.text_city_explore.setText(data.getCity());
        }else{
            viewHolder.text_city_explore.setText("  未知  ");
        }

        viewHolder.text_like_explore.setText(String.valueOf(data.getPhotoVote()));

       /* if(!TextUtils.isEmpty(data.getLastLoginTime())) {
            viewHolder.text_lastlogintime_explore.setText(data.getLastLoginTime());
        }*/
        Log.i("msg","num   ::  "+data.getHeadPic());
        InternetUtils.getPicIntoView(188, 170, viewHolder.imageview_explore, data.getHeadPic());//加载图片

        return convertView;

    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.text_name_explore.setText(null);
//        viewHolder.text_sex_explore.setText(null);
        viewHolder.text_age_explore.setText(null);//非String的字段要转换，看接口文档
        viewHolder.text_height_explore.setText(null);
        viewHolder.text_city_explore.setText(null);
        viewHolder.text_like_explore.setText(null);
        viewHolder.imageview_explore.setImageBitmap(null);
        //viewHolder.text_lastlogintime_explore.setText(null);
    }

    private class ViewHolder {
        private TextView text_name_explore;
//        private TextView text_sex_explore;
        private TextView text_age_explore;
        private TextView text_height_explore;
        private TextView text_city_explore;
        private TextView text_like_explore;
        private ImageView imageview_explore;
        private TextView text_lastlogintime_explore;
    }

}

