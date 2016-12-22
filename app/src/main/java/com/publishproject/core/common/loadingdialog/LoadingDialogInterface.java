package com.publishproject.core.common.loadingdialog;

import android.app.Dialog;

/**
 * @author Android客户端组-tanghongbin
 * @Title: LoadingDialogInterface
 * @Package com.publishproject.core.commonconfig.dialogconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/9 11:44
 * @Description: 进度框接口
 */
public interface LoadingDialogInterface<T extends Dialog> {
    void showDialog();
    void showDialog(String text);
    void dismissXDialog();
    T getDialog();
}
