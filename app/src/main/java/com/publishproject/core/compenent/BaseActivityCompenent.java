package com.publishproject.core.compenent;

import com.publishproject.core.activities.BaseActivity;
import com.publishproject.core.common.loadingdialog.LoadingDialogInterface;
import com.publishproject.core.module.BaseActivityModule;
import com.publishproject.core.views.HeadView;
import com.publishproject.events.ProgressDialogEvent;

import dagger.Component;
import dagger.Provides;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BaseActivityCompenent
 * @Package com.publishproject.core.compenent
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/22 16:24
 * @Description: TODO
 */
@Component(modules = {BaseActivityModule.class})
public interface BaseActivityCompenent {

    void inject(BaseActivity activity);

    public LoadingDialogInterface getDialog();

    public ProgressDialogEvent getDialogEvent();

    public HeadView getHeadView();

}
