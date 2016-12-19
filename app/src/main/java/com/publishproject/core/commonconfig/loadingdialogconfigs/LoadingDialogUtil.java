package com.publishproject.core.commonconfig.loadingdialogconfigs;

import android.content.Context;

import com.publishproject.core.commonconfig.loadingdialogconfigs.loadingdialogtypes.CustomProgressDialog;

/**
 * @author Android客户端组-tanghongbin
 * @Title: LoadingDialogUtil
 * @Package com.publishproject.core.commonconfig.dialogconfigs

 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/9 11:45
 * @Description: loading加载框，常用请求数据开始和数据请求结束
 */
public class LoadingDialogUtil {
    public static LoadingDialogInterface createLoadingDialog(Context context){
        return new CustomProgressDialog(context);
    }
}
