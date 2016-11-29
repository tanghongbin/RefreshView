package com.example.com.meimeng.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.gsonbean.ExploreListItem;
import com.example.com.meimeng.net.InternetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ExploreDateUtils {

    private static ArrayList<View> views;

    public static ArrayList<View> getView(Context context,List<ExploreListItem> list){
        View view=null;
        views = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            view= LayoutInflater.from(context).inflate(R.layout.exploregriditem_f, null);
            TextView text_name_explore = (TextView) view.findViewById(R.id.text_name_explore);
            TextView text_age_explore = (TextView) view.findViewById(R.id.text_age_explore);
            TextView text_height_explore = (TextView) view.findViewById(R.id.text_height_explore);
            TextView text_city_explore = (TextView) view.findViewById(R.id.text_city_explore);
            TextView text_like_explore = (TextView) view.findViewById(R.id.text_like_explore);
            ImageView imageview_explore = (ImageView) view.findViewById(R.id.imageview_explore);
            TextView text_lastlogintime_explore = (TextView) view.findViewById(R.id.text_lastlogintime_explore);
            ExploreListItem data = list.get(i);




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
                namestr.append(" ");
            }

            int sex = data.getSex();

            switch (sex){
                case 0://��ʿ
                    namestr.append("先生");
                    break;
                case 1://Ůʿ
                    namestr.append("女士");
                    break;
                default:
                    break;
            }

            text_name_explore.setText(Utils.setString(namestr.toString(), 3));
            text_age_explore.setText(String.valueOf(data.getAge())+"岁");//��String���ֶ�Ҫת�������ӿ��ĵ�
            text_height_explore.setText(String.valueOf(data.getHeight())+"cm");

            if(!TextUtils.isEmpty(data.getCity())) {
                text_city_explore.setText(data.getCity());
            }else{
                text_city_explore.setText("  未知  ");
            }

           text_like_explore.setText(String.valueOf(data.getPhotoVote()));

            if(!TextUtils.isEmpty(data.getLastLoginTime())) {
                text_lastlogintime_explore.setText(data.getLastLoginTime());
            }
            Log.i("msg", "num   ::  " + data.getHeadPic());
            InternetUtils.getPicIntoView(188, 170,imageview_explore, data.getHeadPic());//����ͼƬ

            views.add(view);
        }

        return  views;
    }
}
