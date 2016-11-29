package com.xqd.chatmessage.fragment;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment的管理类
 */
public class AllFragmentManagement extends Application {

    public static List<Fragment> fragmentList = new ArrayList<>();
    public AllFragmentManagement() {

    }

    /**
     * @param activity    要填充的activity
     * @param containerId 置换fragment的空间ID
     * @param fragment    需要添加的fragment
     */
    public static void ChangeFragment(Activity activity, int containerId, Fragment fragment, boolean isAdd) {

        FragmentManager fg = activity.getFragmentManager();
        FragmentTransaction ft = fg.beginTransaction();
        HideAllFragment(ft);
        if (isAdd) {
            ft.add(containerId, fragment);
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }

    public static void ChangeFragment(Activity activity, int containerId, Fragment fragment) {

        FragmentManager fg = activity.getFragmentManager();
        FragmentTransaction ft = fg.beginTransaction();
        ft.replace(containerId, fragment);
        ft.commit();

    }

    /**
     * 主Activity中隐藏所有在list中的Fragment
     *
     * @param ft
     */
    public static void HideAllFragment(FragmentTransaction ft) {
        for (Fragment mfragment : fragmentList) {
            ft.hide(mfragment);
        }
    }


}

