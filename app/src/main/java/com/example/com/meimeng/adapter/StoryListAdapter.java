package com.example.com.meimeng.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.bean.StoryBean;

import java.util.ArrayList;

/**
 * Created by 010 on 2015/7/17.
 */
public class StoryListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<StoryBean> storyBeans;

    public StoryListAdapter(Context context, ArrayList<StoryBean> storyBeans) {
        this.context = context;
        this.storyBeans = storyBeans;
    }

    @Override
    public int getCount() {
        return storyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return storyBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_story, null);
            viewHolder = new ViewHolder();
            viewHolder.typeText = (TextView) convertView.findViewById(R.id.story_type_text);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.story_content);
            viewHolder.deleteImageView = (RelativeLayout) convertView.findViewById(R.id.story_delete_image_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            resetViewHolder(viewHolder);
        }
        StoryBean bean = storyBeans.get(position);
        if (bean.getType() != null) {
            if (bean.getType().equals("other")) {
                viewHolder.deleteImageView.setVisibility(View.GONE);
            }
        }

        viewHolder.typeText.setText(bean.getStoryType());
        viewHolder.contentText.setText(bean.getContent());
        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storytipsDialog(context, "您确定要删除这条故事？", position);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }

    private void resetViewHolder(ViewHolder holder) {
        holder.typeText.setText(null);
        holder.contentText.setText(null);

    }

    private class ViewHolder {
        private TextView typeText;

        private RelativeLayout deleteImageView;

        private TextView contentText;

    }

    //删除故事的提醒
    private void storytipsDialog(final Context context, String str, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.register_tips, null);

        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(view);
        dialog.show();
        TextView tipstext = (TextView) view.findViewById(R.id.reguster_tips_text);
        tipstext.setText(str);
        TextView cancelText = (TextView) view.findViewById(R.id.register_tips_cancel);
        cancelText.setText("返回");
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();

            }
        });

        TextView open_upText = (TextView) view.findViewById(R.id.register_tips_sure);
        open_upText.setText("确认");
        open_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storyBeans.remove(position);
                dialog.hide();
                notifyDataSetChanged();
            }
        });

    }
}
