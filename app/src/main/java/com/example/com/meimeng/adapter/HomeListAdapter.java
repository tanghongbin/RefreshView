package com.example.com.meimeng.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.activity.OthersSelfActivity;
import com.example.com.meimeng.constants.CommonConstants;
import com.example.com.meimeng.custom.CircleImageView;
import com.example.com.meimeng.gson.gsonbean.HomeListItem;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.List;

/**
 *首页展示的适配器
 */
public class HomeListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<HomeListItem> datalist;
    private Context context;
    private static final int NEWUSER = 0;
    private static final int SYSTEMRECOMMAN = 1;
    private static final int HONGNIANGRECOMMAND = 2;

    public HomeListAdapter(Context context, List<HomeListItem> objects) {

        this.context = context;
        this.datalist = objects;
    }

    public void refresh(List<HomeListItem> objects) {
        this.datalist = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (datalist == null) {
            return 0;
        }
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeListItem data = datalist.get(position);
        ViewHolder viewholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.homelistitem, null);
            viewholder = new ViewHolder();
            viewholder.roundImageView = (CircleImageView) convertView.findViewById(R.id.imageview_round_home);
            viewholder.text_view_name = (TextView) convertView.findViewById(R.id.text_name_home);
            viewholder.textView_story = (TextView) convertView.findViewById(R.id.text_story_content_home);
            viewholder.textView_location = (TextView) convertView.findViewById(R.id.homedetail_textview_location);
            viewholder.pictureImageView = (ImageView) convertView.findViewById(R.id.home_list_big_image_view);
            viewholder.textView_vote_num = (TextView) convertView.findViewById(R.id.text_votenum_home);
            viewholder.recommandImageView = (TextView) convertView.findViewById(R.id.iamgeview_recommend);
            viewholder.vote_num_ImageView = (ImageView) convertView.findViewById(R.id.vote_star_imageView);
            viewholder.city_ImageView = (ImageView) convertView.findViewById(R.id.homedetail_image_location);
            viewholder.vote_tableRow = (TableRow) convertView.findViewById(R.id.like_story_tableRow);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
            reserviewholder(viewholder);
        }

        viewholder.roundImageView.setOnClickListener(this);
        viewholder.text_view_name.setOnClickListener(this);
        viewholder.text_view_name.setText(data.getNickname());
        viewholder.textView_story.setText(data.getStoryBrief());
        viewholder.textView_vote_num.setText(String.valueOf(data.getVoteNum()));
        viewholder.textView_location.setText(data.getCity());
        viewholder.roundImageView.setTag(position);
        viewholder.text_view_name.setTag(position);
        viewholder.pictureImageView.setTag(position);
        switch (data.getType()) {
            case SYSTEMRECOMMAN:

                viewholder.recommandImageView.setText("系统推荐");
                viewholder.recommandImageView.setBackgroundColor(context.getResources().getColor(R.color.gold_textcolor));

                break;
            case HONGNIANGRECOMMAND:

                viewholder.recommandImageView.setText("红娘推荐");
                viewholder.recommandImageView.setBackgroundColor(context.getResources().getColor(R.color.matchmaker));
                break;
            case NEWUSER:

                viewholder.recommandImageView.setText("最新用户");
                viewholder.recommandImageView.setBackgroundColor(context.getResources().getColor(R.color.gold_textcolor));
                break;
            default:

                viewholder.recommandImageView.setVisibility(View.GONE);
                break;

        }

        if (data.getStoryPic()!=0){
            InternetUtils.getPicIntoView(375, 375, viewholder.pictureImageView, data.getStoryPic(), position,1);
        }
        if (data.getHeadPic()!=0){
            InternetUtils.getPicIntoView(200, 200, viewholder.roundImageView, data.getHeadPic(), position, true);
        }
        return convertView;

    }

    private void reserviewholder(ViewHolder viewholder) {
        viewholder.roundImageView.setImageResource(R.drawable.meimeng_ico_user_missing_small);
        viewholder.pictureImageView.setImageResource(R.drawable.meimeng_ico_index_missing);
        viewholder.text_view_name.setText(null);
        viewholder.textView_story.setText(null);
        viewholder.textView_location.setText(null);
        viewholder.textView_vote_num.setText(null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_name_home:
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(context, 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(context, 2);
                } else {

                    gotoOthersSelfActivity(v.getTag());
                }

                break;
            case R.id.imageview_round_home:
                if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 0) {
                    Utils.JudgeUserVerfiy(context, 0);
                } else if (MeiMengApplication.sharedPreferences.getInt(CommonConstants.USER_VERFIY, 0) == 2) {
                    Utils.JudgeUserVerfiy(context, 2);
                } else {
                    gotoOthersSelfActivity(v.getTag());
                }

                break;
            default:
                break;

        }


    }

    private void gotoOthersSelfActivity(Object object) {
        Intent intent = new Intent(context, OthersSelfActivity.class);
        intent.putExtra("targetUid", datalist.get((int) object).getUid());
        context.startActivity(intent);

    }

    private class ViewHolder {
        private CircleImageView roundImageView;
        private TextView text_view_name;
        private TextView textView_story;
        private TextView textView_vote_num;
        private TextView textView_location;
        private ImageView pictureImageView;
        private TextView recommandImageView;
        private ImageView vote_num_ImageView;
        private ImageView city_ImageView;
        private TableRow vote_tableRow;
    }
}
