package com.publishproject.core.common.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : 汤洪斌
 * @time : 2016/9/8 16:59
 *
 * @param T item对象
 * @param W 每个item布局视图生成的ViewDataBinding的子类
 * @des     : 展示简单视图的基类adapter
 * @simple
 *
 *     构造参数：
 *     1.Object -每个列表item的对象，
 *     2.ViewDataBinding-每个item生成的编译布局的binding类，
 *     3.mContext 上下文资源对象
 *     4.mList-存放item的实例的集合
 *     5.mLayoutId- 每个item的布局id
 *
 *
 *    BaseBindingAdapter adapter = new BaseBindingAdapter<Object,ViewDataBinding>(mContext,mList,mLayoutId){
 *        @Ovrride
 *        public void binding(Object c,ViewDataBinding binding){
 *            //进行赋值初始化操作
 *        }
 *    };
 */
public class BaseBindingAdapter<OBJ, DATABINDING extends ViewDataBinding> extends BaseAdapter {
    //在调试时如果没有数据时可写死的数量
    public static int UNCHANGE_COUNT = 0;
    private Context mContext;
    private List<OBJ> mList;
    private int mLayoutId;
    private Hook mHook;
    private Builder buildler;

    public BaseBindingAdapter(Context context, List<OBJ> list, int layoutId, Hook<DATABINDING> hook) {
        this.mLayoutId = layoutId;
        this.mContext = context;
        this.mList = list;
        this.mHook = hook;
    }
    public BaseBindingAdapter(Builder builder){
        this.buildler = builder;
    }

    public List<OBJ> getList(){
        return mList;
    }

    @Override
    public int getCount() {
        return mList == null ? UNCHANGE_COUNT: mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DATABINDING w = null;
        if(convertView == null){
            convertView = View.inflate(mContext, mLayoutId,null);
            w = DataBindingUtil.bind(convertView);
            convertView.setTag(w);
        }

        w = (DATABINDING) convertView.getTag();

        //因为测试需要所以手动操作view，以后正式环境需要去掉

        if(mList != null){
            OBJ t  = mList.get(position);
            if(t != null){
                if(mHook != null){
                    mHook.binding(w,t);
                    mHook.operateView(convertView,t);
                }
            }
        }
        return convertView;
    }
    //一个钩子类，用于传递多余的view操作
    public abstract class Hook<W>{
        void operateView(View view, OBJ t){

        };
        abstract void binding(W w, OBJ t);
    }

    /**
     * 此类用于构建adapter所需要传递的参数,可动态添加
     * */
    public final class Builder{
        /**
         * @param context 上下文环境对象
         * */
        public Builder context(Context context){
            mContext = context;
            return this;
        }
        /**
         * @param list 集合对象存储数据
         * */
        public Builder list(List list){
            if(mList == null){
                mList = list;
            }
            return this;
        }
        /**
         * @param layoutId 布局id
         * */
        public Builder layout(int layoutId){
            mLayoutId = layoutId;
            return this;
        }
        /**
         * 如果list已经赋值，那么则不需要再使用数组
         * @param array 数组数据
         * */
        public Builder array(OBJ[] array){
            if(mList == null && array != null){
                mList = new ArrayList<>(Arrays.asList(array));
            }
            return this;
        }
        /**
         * 表明构建完成，并无实际用处
         * */
        public void build(){

        }
    }
}
