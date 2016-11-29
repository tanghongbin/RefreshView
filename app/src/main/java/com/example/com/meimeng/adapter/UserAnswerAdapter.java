package com.example.com.meimeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.com.meimeng.R;
import com.example.com.meimeng.gson.gsonbean.UserAnswerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lx on 2015/8/6.
 */
public class UserAnswerAdapter extends BaseExpandableListAdapter {

    private Context context ;

    private List<UserAnswerData> userAnswerDatas ;

    private List<String> type ;

    private HashMap<String , List<UserAnswerData> > map = new HashMap<>() ;

    public UserAnswerAdapter( Context context , List<UserAnswerData> userAnswerDatas ){

        this.context = context ;
        this.userAnswerDatas = userAnswerDatas ;

        type = new ArrayList<>() ;
        type.add("个性描述") ;
        type.add("生活习惯") ;
        type.add("爱情观点") ;
        type.add("约会类型") ;
        type.add("婚姻期望") ;
        type.add("婚后生活") ;
        type.add("理想对象") ;

        initMap();

    }

    private void initMap(){

        map.clear();

        List<UserAnswerData> userAnswerDatas1 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas2 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas3 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas4 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas5 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas6 = new ArrayList<>();
        List<UserAnswerData> userAnswerDatas7 = new ArrayList<>();

        for ( UserAnswerData userAnswerData:userAnswerDatas){

            if( userAnswerData.getQid() <= 6  ){

                userAnswerDatas1.add(userAnswerData) ;

            }else if ( userAnswerData.getQid() <= 11 ){

                userAnswerDatas2.add(userAnswerData) ;

            }else if ( userAnswerData.getQid() <= 16 ){

                userAnswerDatas3.add(userAnswerData) ;

            }else if( userAnswerData.getQid() <= 22 ){

                userAnswerDatas4.add(userAnswerData) ;

            }else if ( userAnswerData.getQid() <=  28){

                userAnswerDatas5.add(userAnswerData) ;

            }else if( userAnswerData.getQid() <=  33 ){

                userAnswerDatas6.add(userAnswerData) ;

            }else if( userAnswerData.getQid() <=  40 ){

                userAnswerDatas7.add(userAnswerData) ;

            }

        }

        map.put(type.get(0),userAnswerDatas1) ;
        map.put(type.get(1),userAnswerDatas2) ;
        map.put(type.get(2),userAnswerDatas3) ;
        map.put(type.get(3),userAnswerDatas4) ;
        map.put(type.get(4),userAnswerDatas5) ;
        map.put(type.get(5),userAnswerDatas6) ;
        map.put(type.get(6),userAnswerDatas7) ;


    }

    @Override
    public int getGroupCount() {
        return type.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return map.get(type.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return type.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return map.get(type.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if ( map.get(type.get(groupPosition)).isEmpty() ){

            convertView = LayoutInflater.from(context).inflate(R.layout.empty_item,null) ;

            return  convertView ;
        }

        convertView =  LayoutInflater.from(context).inflate(R.layout.user_answer_show_parent, null) ;


        TextView user_answer_type = (TextView) convertView.findViewById(R.id.user_answer_type);

        user_answer_type.setText(type.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        UserAnswerData userAnswerData = (UserAnswerData) getChild(groupPosition, childPosition);


        if( convertView == null ){

            convertView = LayoutInflater.from(context).inflate(R.layout.user_answer_show_children , null) ;

        }

        TextView user_answer_question = (TextView) convertView.findViewById(R.id.user_answer_question);
        TextView user_answer_answer = (TextView) convertView.findViewById(R.id.user_answer_answer);

        user_answer_question.setText( String.valueOf( childPosition + 1 ) + "."  + userAnswerData.getQuestion());

        String answer = "" ;

        for ( int i = 0 ; i < userAnswerData.getAnswer().size() ; i++){
            answer = answer + userAnswerData.getAnswer().get(i) + " ";
        }

        user_answer_answer.setText( answer);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {

        if ( !userAnswerDatas.isEmpty() ){
            initMap();
        }
        super.notifyDataSetChanged();
    }
}
