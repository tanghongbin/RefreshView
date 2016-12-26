package com.publishproject.core.module;

import com.publishproject.R;
import com.publishproject.YqApplication;
import com.publishproject.core.activities.BaseActivity;
import com.publishproject.core.common.loadingdialog.LoadingDialogInterface;
import com.publishproject.core.common.loadingdialog.LoadingDialogUtil;
import com.publishproject.core.views.HeadView;
import com.publishproject.events.ProgressDialogEvent;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * @author Android客户端组-tanghongbin
 * @Title: BaseActivityModule
 * @Package com.publishproject.core.module
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/22 16:25
 * @Description: TODO
 */
@Module
public class BaseActivityModule {

    BaseActivity activity;

    public BaseActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    public LoadingDialogInterface getDialog(){
       return LoadingDialogUtil.createLoadingDialog(activity);
    }
    @Provides
    public ProgressDialogEvent getDialogEvent(){
       return new ProgressDialogEvent(activity);
    }
    @Provides
    public HeadView getHeadView(){
        return (HeadView)activity.findViewById(R.id.common_head_view);
    }
}
