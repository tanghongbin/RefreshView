package com.publishproject.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;

import pushlish.tang.com.commonutils.ToastUtils;

/**
 * @author Android客户端组-tanghongbin
 * @Title: IpUtil
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 17:12
 * @Description: TODO
 */
public class IpUtil {
    /**
     * 自带弹框修改ip地址
     * @param context
     */
    public static void changeIp(final Context context){
        final EditText mEditText=new EditText(context);
        mEditText.setHint(Urls.getDebugDomain());
        mEditText.setTextColor(Color.BLACK);
        ToastUtils.showDialog(context,  mEditText, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = mEditText.getText().toString();
                Urls.saveDebugDomain(url);
                ToastUtils.showMessage(context,"修改IP成功");
            }
        });
    }
}
