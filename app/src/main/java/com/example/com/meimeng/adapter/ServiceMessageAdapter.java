package com.example.com.meimeng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.gson.gsonbean.ServiceMessageItem;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.Utils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lx on 2015/8/10.
 */
public class ServiceMessageAdapter extends BaseAdapter {

    private Context context;

    private List<ServiceMessageItem> dataList;

    public ServiceMessageAdapter(Context context, List<ServiceMessageItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ServiceMessageItem bean = dataList.get(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_server, null);
            viewHolder = new ViewHolder();
            viewHolder.headerImage = (RoundCornerImageView) convertView.findViewById(R.id.item_of_chat_header);

            viewHolder.nicknameText = (TextView) convertView.findViewById(R.id.item_of_chat_name);

            viewHolder.timeText = (TextView) convertView.findViewById(R.id.item_of_chat_time);

            viewHolder.messageText = (TextView) convertView.findViewById(R.id.item_of_chat_message);

            viewHolder.likeImageView = (ImageView) convertView.findViewById(R.id.item_of_chat_like_image_view);

            viewHolder.placeImageView = (ImageView) convertView.findViewById(R.id.item_of_chat_place_image_view);

            viewHolder.placeText = (TextView) convertView.findViewById(R.id.item_of_chat_place_text);

            viewHolder.placeLayout = (LinearLayout) convertView.findViewById(R.id.item_of_chat_place_layout);

            viewHolder.redPointImageView = (ImageView) convertView.findViewById(R.id.service_red_point_image_view);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            resetViewHolder(viewHolder);
        }

        switch (bean.getType()) {

            //被人关注
            case 1:
                viewHolder.nicknameText.setText("喜欢");
               // viewHolder.headerImage.setImageResource(R.drawable.follow);
                setAtBitmap(viewHolder.headerImage, bean);
                viewHolder.messageText.setText(bean.getSenderNick()+"喜欢了你");
                break;
            //故事获赞
            case 2:
                viewHolder.nicknameText.setText("相册点赞");
               // viewHolder.headerImage.setImageResource(R.drawable.xcdz);
                setAtBitmap(viewHolder.headerImage, bean);
                viewHolder.messageText.setText(bean.getSenderNick() + "为你点赞");
                break;
            //被邀请参加活动
            case 3:
                viewHolder.nicknameText.setText("活动邀请");
                viewHolder.headerImage.setImageResource(R.drawable.yqcg);
//                viewHolder.placeLayout.setVisibility(View.VISIBLE);
//                viewHolder.placeText.setText(bean.getPlaceDetail());
                viewHolder.messageText.setText("您被邀请参加"+Utils.setString(bean.getContext().getActivityTitle(), 6));
                break;
            //活动支付
            case 4:
                viewHolder.nicknameText.setText("活动支付");
                viewHolder.headerImage.setImageResource(R.drawable.zfcg);
                viewHolder.messageText.setText("您已成报名支付"+Utils.setString(bean.getContext().getActivityTitle(),6));
                break;
            //聊天次数增加
            case 5:
                viewHolder.nicknameText.setText("聊天");
                viewHolder.headerImage.setImageResource(R.drawable.zjltcs);
                viewHolder.messageText.setText("您聊天的次数已增加");
                break;
            //获得礼物
            case 6:
                viewHolder.nicknameText.setText("收到礼物");
                setAtBitmap(viewHolder.headerImage, bean);
                viewHolder.messageText.setText(bean.getSenderNick()+"送给你的礼物");
                break;
            //系统消息
            case 8:
                viewHolder.nicknameText.setText("系统提示");
                viewHolder.headerImage.setImageResource(R.drawable.icon_srhn);
                viewHolder.messageText.setText("你好，欢迎进入美盟！");
                break;
            case 9:
                viewHolder.nicknameText.setText("报名成功");
                viewHolder.headerImage.setImageResource(R.drawable.zfcg);
                viewHolder.messageText.setText("你已经支付成功！"/*+Utils.setString(bean.getContext().getActivityTitle(),6)*/);
                break;
            //审核通知
            case 7:
                switch (bean.getContext().getVerfiyFlg()){
                    case 1://审核通过
                        viewHolder.headerImage.setImageResource(R.drawable.shtg);
                        viewHolder.nicknameText.setText("审核通过");
                        switch (bean.getContext().getSubType()){
                            case 1://证件审核
                                viewHolder.messageText.setText("您上传的证件通过了审核！");
                                break;
                            case 2://照片审核
                                viewHolder.messageText.setText("您上传的照片通过了审核！");
                                break;
                            case 3://会员审核通过
                                viewHolder.messageText.setText("您的账号通过了审核！");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2://审核不通过
                        viewHolder.headerImage.setImageResource(R.drawable.shbtg);
                        viewHolder.nicknameText.setText("审核不通过");
                        switch (bean.getContext().getSubType()){
                            case 1://证件审核
                                viewHolder.messageText.setText("抱歉，您上传的证件未通过审核！");
                                break;
                            case 2://照片审核
                                viewHolder.messageText.setText("抱歉，您上传的照片未通过审核！");
                                break;
                            case 3://会员审核通过
                                viewHolder.messageText.setText("抱歉，您的账号未通过审核！");
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;

        }
        Log.d("wz","MeiMengApplication.serviceUnreadNum="+MeiMengApplication.serviceUnreadNum);
        if (MeiMengApplication.serviceUnreadNum!=null) {
            //1代表初始，未读
            if (!bean.getStatus().equals("1")) {
                viewHolder.redPointImageView.setVisibility(View.GONE);
                //2代表已读
            }else if(!bean.getStatus().equals("2")){
                viewHolder.redPointImageView.setVisibility(View.VISIBLE);
            }
            MeiMengApplication.serviceUnreadNumText.setVisibility(View.VISIBLE);
        }else{
            MeiMengApplication.serviceUnreadNumText.setVisibility(View.GONE);
        }
//        viewHolder.nicknameText.setText(bean.getSenderNick());
        viewHolder.timeText.setText(Utils.getDisplayDate(bean.getSendTime(), "yyyy-MM-dd HH:mm"));//yyyy-MM-dd
//        viewHolder.messageText.setText(bean.get);
        return convertView;
    }

    /**
     * 设置网络图片
     *
     * @param iv
     * @param bean
     */
    private void setAtBitmap(final ImageView iv,final ServiceMessageItem bean){
        try {
            Observable observable = InternetUtils.getBytesObservale(Long.parseLong(bean.getSenderHeadPic()), true);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1() {
                        @Override
                        public void call(Object o) {
                            byte[] bytes = (byte[]) o;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            iv.setImageBitmap(bitmap);
                            //iv.setAngie(10f,10f);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(context, "SlideListAdapter: 下载图片出错，" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    /**
     * 重置服务消息的列表信息
     *
     * @param holder
     */
    private void resetViewHolder(ViewHolder holder) {
        holder.headerImage.setImageBitmap(null);

        holder.nicknameText.setText(null);

        holder.timeText.setText(null);

        holder.messageText.setText(null);

        holder.placeText.setText(null);

        holder.placeLayout.setVisibility(View.GONE);

        holder.likeImageView.setVisibility(View.GONE);

    }

    private class ViewHolder {
        private RoundCornerImageView headerImage;

        private TextView nicknameText;

        private TextView timeText;

        private TextView messageText;

        private ImageView likeImageView;

        private ImageView placeImageView;

        private TextView placeText;

        private LinearLayout placeLayout;

        private ImageView redPointImageView;
    }
}
