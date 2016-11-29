package com.example.com.meimeng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.custom.RoundCornerImageView;
import com.example.com.meimeng.gson.gsonbean.ReceivedGiftItem;
import com.example.com.meimeng.net.InternetUtils;
import com.example.com.meimeng.util.ImageUtil;
import com.example.com.meimeng.util.Utils;

import java.util.ArrayList;

/**
 * Created by lx on 2015/8/5.
 */
public class GiftListAdapter extends BaseAdapter {

    private Context context;
    private int giftType = 1;

    private ArrayList<ReceivedGiftItem> datalist;

    public GiftListAdapter(Context context, ArrayList<ReceivedGiftItem> datalist,int giftType) {

        this.context = context;
        this.datalist = datalist;
        this.giftType = giftType;
    }

    public interface GetTargetUid {
        void getTargetUid(long uid);
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ReceivedGiftItem item = (ReceivedGiftItem) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.git_item, null);

        final ImageView giftImage = (ImageView) convertView.findViewById(R.id.git_item_gitPic);
        final RoundCornerImageView giftHeadPic = (RoundCornerImageView) convertView.findViewById(R.id.git_item_headPic);
        giftHeadPic.setAngie(20f, 20f);
        InternetUtils.getPicIntoView(200, 200, giftHeadPic, item.getHeadPic(), true);


//        InternetUtils2.getPicIntoView(200, 200, giftImage, item.getGiftPic(), true);
        TextView giftName = (TextView) convertView.findViewById(R.id.git_item_gitname);
        TextView giftSenderName = (TextView) convertView.findViewById(R.id.git_item_nickname);
        TextView giftSendTime = (TextView) convertView.findViewById(R.id.gift_item_time);
        Button givegiftback = (Button) convertView.findViewById(R.id.givegiftback_button);
        switch (giftType){//1：我收到的礼物2：我送出的礼物
            case 1:
                givegiftback.setVisibility(View.VISIBLE);
                break;
            case 2:
                givegiftback.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        String name = String.valueOf(item.getGiftPic());
        Bitmap bitmap = ImageUtil.getGiftImage(context, name);

        if(bitmap != null){
            giftImage.setImageBitmap(bitmap);
        }

        final int p = position;

        if(givegiftback.getVisibility() == View.VISIBLE) {
            givegiftback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GetTargetUid) context).getTargetUid(((ReceivedGiftItem) getItem(p)).getUid());
                }
            });
        }

        if(!TextUtils.isEmpty(item.getGiftName())){
            giftName.setText(item.getGiftName());
        }


//        giftSenderName.setText(item.getNickname());
        StringBuffer sb = new StringBuffer();
        if(!TextUtils.isEmpty(item.getFirstName())) {
            sb.append(item.getFirstName());
        }else {
            sb.append("某某");
        }
            switch (item.getSex()) {
                case 0://先生
                    sb.append("先生");
                    break;
                case 1://女士
                    sb.append("女士");
                    break;

            }
        giftSenderName.setText(Utils.setString(sb.toString(),4));
        giftSenderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(context, OthersSelfActivity.class);
                intent.putExtra("targetUid", item.getUid());
                context.startActivity(intent);*/

            }
        });
        giftSendTime.setText(Utils.getStrTime(item.getGmtCreate()));
        giftSendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(context, OthersSelfActivity.class);
                intent.putExtra("targetUid", item.getUid());
                context.startActivity(intent);
*/
            }
        });
        giftHeadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(context, OthersSelfActivity.class);
                intent.putExtra("targetUid", item.getUid());
                context.startActivity(intent);*/

            }
        });

        return convertView;
    }


}
