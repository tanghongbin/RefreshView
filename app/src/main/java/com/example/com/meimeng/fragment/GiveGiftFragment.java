package com.example.com.meimeng.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.adapter.GiveGiftAdapter;
import com.example.com.meimeng.bean.GiftInfoBean;
import com.example.com.meimeng.util.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 010 on 2015/7/16.
 */
public class GiveGiftFragment extends Fragment {


    @Bind(R.id.give_gift_grid_view)
    GridView giveGiftGridView;

    @Bind(R.id.give_button)
    Button giveButton;

    private GiveGiftAdapter giveGiftAdapter;

    private Context context;

    //记录上一个选中的礼物
    private LinearLayout lastSelectLayout;

    //记录选中的礼物商品id
    private long goodId = -1l;

    private String name;

    private String price;

    //选中的图片
    private int goodsPicId = -1;

    //礼物列表（本地取）
    private ArrayList<GiftInfoBean> giftInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.give_gift, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();

        MeiMengApplication.currentContext=getActivity();
        //从本地获取礼物信息
        getLocalGift();

        giveGiftAdapter = new GiveGiftAdapter(context, giftInfoList);
        giveGiftGridView.setAdapter(giveGiftAdapter);
        setHorizontalGridView();//这步一定要放在setadpter之后

        return view;
    }

    /**
     * 设置giftGridView水平滚动
     */
    private void setHorizontalGridView() {
        int size = giftInfoList.size();
        DisplayMetrics dm = Utils.getScreenMetrics(context);
        float density = dm.density;

        int width = 125;
        int horizontalSpacing = 2;

        int allWidth = (int) (horizontalSpacing * (size - 1) * density + width * size * density);       //LinearLayout.LayoutParams.MATCH_PARENT
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        giveGiftGridView.setLayoutParams(params);
        giveGiftGridView.setColumnWidth(width);
        giveGiftGridView.setHorizontalSpacing(horizontalSpacing);
        giveGiftGridView.setNumColumns(size);
        //others_self_horizontal_view
        giveGiftGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout selectLayout = (LinearLayout) view.findViewById(R.id.item_of_give_gift_select_layout);

                if (lastSelectLayout == null) {
                    lastSelectLayout = selectLayout;
                } else {
                    lastSelectLayout.setVisibility(View.GONE);
                    lastSelectLayout = selectLayout;
                }

                goodsPicId = giftInfoList.get(position).getGoodsPicId();
                selectLayout.setVisibility(View.VISIBLE);
                goodId = giftInfoList.get(position).getGoodsId();
                name = giftInfoList.get(position).getGoodsName();
                price = giftInfoList.get(position).getGoodsPrices();
            }
        });
    }


    /**
     * 选择礼物的回调接口
     */
    public interface OnGiftListener {
        public void giveListener(int giftPicId, long goodId, String name, String price);
    }

    /**
     * 选择礼物
     */
    @OnClick(R.id.give_button)
    void sendGift() {
        if (goodId == -1l) {
            Toast.makeText(context, "请先选择礼物", Toast.LENGTH_SHORT).show();
        } else {
            MeiMengApplication.payTitle=name;
            MeiMengApplication.payGoodsPicId=goodsPicId;
            MeiMengApplication.payGoodsId=goodId;
            MeiMengApplication.payPrice=Double.parseDouble(price);
            ((OnGiftListener) context).giveListener(goodsPicId, goodId, name, price); //回调方法
        }

    }

    /**
     * 从本地中构造礼物信息
     */
    private void getLocalGift() {

        //清空礼物信息列表
        giftInfoList.clear();

        ArrayList<Integer> goodsPicIdList = new ArrayList<>();
        goodsPicIdList.add(R.drawable.g_4441);
        goodsPicIdList.add(R.drawable.g_4442);
        goodsPicIdList.add(R.drawable.g_4443);
        goodsPicIdList.add(R.drawable.g_4444);
        goodsPicIdList.add(R.drawable.g_4445);
        goodsPicIdList.add(R.drawable.g_4446);
        goodsPicIdList.add(R.drawable.g_4447);
        goodsPicIdList.add(R.drawable.g_4448);

        ArrayList<String> goodsNameList = new ArrayList<>();
        goodsNameList.add("巧克力");
        goodsNameList.add("玫瑰花");
        goodsNameList.add("手表");
        goodsNameList.add("香水");
        goodsNameList.add("燕尾服");
        goodsNameList.add("晚礼服");
        goodsNameList.add("跑车");
        goodsNameList.add("项链");

        ArrayList<String> goodsPriceList = new ArrayList<>();
        goodsPriceList.add("8");
        goodsPriceList.add("8");
        goodsPriceList.add("40");
        goodsPriceList.add("40");
        goodsPriceList.add("68");
        goodsPriceList.add("68");
        goodsPriceList.add("98");
        goodsPriceList.add("98");

        ArrayList<Long> goodsIdList = new ArrayList<>();
        goodsIdList.add(1l);
        goodsIdList.add(2l);
        goodsIdList.add(4l);
        goodsIdList.add(5l);
        goodsIdList.add(7l);
        goodsIdList.add(8l);
        goodsIdList.add(10l);
        goodsIdList.add(11l);

        for (int i = 0; i < 8; i++) {
            GiftInfoBean giftInfoBean = new GiftInfoBean();
            if (i ==0||i==1) {
                giftInfoBean.setGoodsId(Long.valueOf(i + 1));
            }else if (i ==2||i==3) {
                giftInfoBean.setGoodsId(Long.valueOf(i + 2));
            }else if (i ==4||i==5) {
                giftInfoBean.setGoodsId(Long.valueOf(i + 3));
            }else if (i ==6||i==7) {
                giftInfoBean.setGoodsId(Long.valueOf(i + 4));
            }
            giftInfoBean.setGoodsName(goodsNameList.get(i));
            giftInfoBean.setGoodsPicId(goodsPicIdList.get(i));
            giftInfoBean.setGoodsPrices(goodsPriceList.get(i));
            giftInfoList.add(giftInfoBean);
        }

    }

}
