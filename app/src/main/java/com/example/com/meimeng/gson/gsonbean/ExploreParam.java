package com.example.com.meimeng.gson.gsonbean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ExploreParam {
    private List<ExploreListItem> userFind;//严格按照接口里面的字段来写

    public List<ExploreListItem> getExploreListItem() {
        return userFind;
    }

    public void setExploreListItem(List<ExploreListItem> userFind) {
        this.userFind = userFind;
    }
}
