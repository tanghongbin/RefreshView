package com.publishproject.managers;

/**
 * @author Android客户端组-tanghongbin
 * @Title: DataManager
 * @Package com.publishproject.managers
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:37
 * @Description: 用于管理单例实例
 */
public class DataManager {
    private static DataManager instance;
    public static DataManager getInstance(){
        if(instance == null){
            synchronized (DataManager.class){
                if(instance == null){
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }


    /**
     * 注销时清空数据信息
     */
    public void loginOut(){
        instance = null;
    }
}
