package com.example.com.meimeng.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.com.meimeng.MeiMengApplication;
import com.example.com.meimeng.R;
import com.example.com.meimeng.net.InternetUtils;

public final class OtherPhotosFragment extends Fragment {
    private long picId;

    private ImageView imageView;
    private LinearLayout linearLayout;
    public static OtherPhotosFragment newInstance(long picId) {
        OtherPhotosFragment fragment = new OtherPhotosFragment();
        fragment.picId = picId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout imgLayout = (LinearLayout) inflater.from(getActivity()).inflate(R.layout.others_photo_item, null);
        MeiMengApplication.currentContext = getActivity();
        imageView = (ImageView) imgLayout.findViewById(R.id.othersPhotoImageView);
        WindowManager wm = (WindowManager) this.getActivity().getSystemService(Activity.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);

        imgLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = imageView.getWidth();
//                InternetUtils.getPicIntoView(width, 0, imageView, picId);
                InternetUtils.setPhotoBitmap(width, imageView, picId);
                Log.e("OthersPhotosFragment", "width=" + width);
            }
        });


        return imgLayout;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
