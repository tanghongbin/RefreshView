package com.example.com.meimeng.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.LikePeopleBean;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.net.InternetUtils;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/7/17.
 */
public class SlideListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LikePeopleBean> likePeopleBeans;
    private OnSlideClickListener slideClickListener;

    public SlideListAdapter(Context context, ArrayList<LikePeopleBean> likePeopleBeans, OnSlideClickListener slideClickListener) {
        this.context = context;
        this.likePeopleBeans = likePeopleBeans;
        this.slideClickListener = slideClickListener;
    }

    @Override
    public int getCount() {
        return likePeopleBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return likePeopleBeans.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slide_list_view, null);
            viewHolder = new ViewHolder();

            viewHolder.headerImageView = (RoundCornerImageView) convertView.findViewById(R.id.slide_header_image_view);
            viewHolder.headerImageView.setAngie(10f, 10f);
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.slide_name_text);
            viewHolder.placeText = (TextView) convertView.findViewById(R.id.slide_place_text);
            viewHolder.ageText = (TextView) convertView.findViewById(R.id.slide_age_text);
            viewHolder.heightText = (TextView) convertView.findViewById(R.id.slide_height_text);
            viewHolder.slide_layout = (LinearLayout) convertView.findViewById(R.id.slide_layout);
            viewHolder.thirdLayout = (LinearLayout) convertView.findViewById(R.id.slide_third_layout);
            viewHolder.slideholdlayout = (RelativeLayout) convertView.findViewById(R.id.slide_hold_layout);
            viewHolder.notopen = (ImageView) convertView.findViewById(R.id.weikaitong);
//            viewHolder.conversation = (ImageView) convertView.findViewById(R.id.slide_conversation);
            viewHolder.conversation = (RelativeLayout) convertView.findViewById(R.id.slide_conversation_layout);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }


        viewHolder.headerImageView.setTag(position);
        LikePeopleBean bean = likePeopleBeans.get(position);
        Log.i("msg", bean.getTy() + "");
        if (bean.getTy() == 2) {
            viewHolder.thirdLayout.setVisibility(View.GONE);
            viewHolder.slide_layout.setVisibility(View.GONE);
        }
        if (bean.getTy() == 3) {
            viewHolder.conversation.setVisibility(View.VISIBLE);
            viewHolder.notopen.setVisibility(View.VISIBLE);

            if (bean.getType() == 3) {
                viewHolder.notopen.setVisibility(View.GONE);
            }
            if (bean.getType() == 2) {
                viewHolder.notopen.setVisibility(View.VISIBLE);
            }
        }
        InternetUtils.getPicIntoView(200, 200, viewHolder.headerImageView, bean.getHeadPic(), position, true);
        if (!TextUtils.isEmpty(bean.getFirstName())) {
            switch (bean.getSex()) {
                case 0://男士
                    viewHolder.nameText.setText(bean.getFirstName() + "先生");
                    break;
                case 1://女士
                    viewHolder.nameText.setText(bean.getFirstName() + "女士");
                    break;
            }

        }
        if (!TextUtils.isEmpty(bean.getCity())) {
            viewHolder.placeText.setText(bean.getCity());
        }

        if (slideClickListener != null) {
            final View item = convertView;
            final int pos = position;
           /* viewHolder.firstLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickListener.onFirstClick(pos, item);
                }
            });*/

            viewHolder.thirdLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickListener.onThirdClick(pos, item);
                }
            });
            viewHolder.headerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickListener.onHeadPicClick(pos, item);
                }
            });
            viewHolder.slideholdlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickListener.slideholdlayoutClick(pos, item);
                }
            });
            viewHolder.conversation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickListener.slideconversationClick(pos, item);
                }
            });
        }

        if (!TextUtils.isEmpty(bean.getAge() + "")) {
            viewHolder.ageText.setText((bean.getAge() - 1) + "岁");
        }
        if (!TextUtils.isEmpty(bean.getHeight() + "")) {
            viewHolder.heightText.setText(bean.getHeight() + "cm");
        }

        return convertView;

    }

    private void resetViewHolder(ViewHolder viewHolder) {
        viewHolder.headerImageView.setImageBitmap(null);
        viewHolder.nameText.setText(null);
        viewHolder.placeText.setText(null);
    }

    private class ViewHolder {
        //        private ImageView conversation;
        private RelativeLayout conversation;
        private LinearLayout slide_layout;
        private RoundCornerImageView headerImageView;
        private RelativeLayout slideholdlayout;
        private ImageView notopen;
        private TextView nameText;
        private TextView placeText;
        private TextView ageText;
        private TextView heightText;
        // private LinearLayout firstLayout;
        private LinearLayout thirdLayout;
    }

    public interface OnSlideClickListener {
        public void onFirstClick(int position, View item);

        public void onThirdClick(int position, View item);

        public void onHeadPicClick(int position, View item);

        public void slideholdlayoutClick(int position, View item);

        public void slideconversationClick(int position, View item);
    }
}
