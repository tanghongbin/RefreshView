package com.publishproject.util;

import android.content.Context;
import android.content.Intent;

/**
 * @author Android客户端组-tanghongbin
 * @Title: ImageChooseAndCropUtil
 * @Package com.publishproject.util
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/20 15:59
 * @Description: 图片选择和裁剪工具类
 */
public class ImageChooseAndCropUtil {
    public static final String TYPE = "type";
    public static final int TYPE_CHOOSE_AND_CROP = 0x128;//裁剪和选择或照相都要
    public static final int TYPE_CHOOSE = 0x129;//只选择图片或只照相
    public static final int TYPE_CROP = 0x130;//只裁剪
    private ImageCropCallback cropCallback;

    static ImageChooseAndCropUtil imageChooseAndCropUtil;
    Context context;
    public static ImageChooseAndCropUtil getInstance(Context context){
        if(imageChooseAndCropUtil == null){
            synchronized (ImageChooseAndCropUtil.class){
                if(imageChooseAndCropUtil == null){
                    imageChooseAndCropUtil = new ImageChooseAndCropUtil(context);
                }
            }
        }
        return imageChooseAndCropUtil;
    }
    public ImageChooseAndCropUtil(Context context){
        this.context = context;
    }

    /**
     * 选择单张图片并且裁剪，然后再返回
     * @param cropCallback
     */
    public void chooseAndCropPic(ImageCropCallback cropCallback){
        if(cropCallback == null){
            throw new NullPointerException("callback--不能为空");
        }
        this.cropCallback = cropCallback;
        context.startActivity(new Intent(context,ImageSingleChooseActivity.class)
        .putExtra(TYPE,TYPE_CHOOSE_AND_CROP));
    }

    /**
     * 只选择图片，不做裁剪
     * @param cropCallback
     */
    public void choosePic(ImageCropCallback cropCallback){
        if(cropCallback == null){
            throw new NullPointerException("callback--不能为空");
        }
        this.cropCallback = cropCallback;
        context.startActivity(new Intent(context,ImageSingleChooseActivity.class)
                .putExtra(TYPE,TYPE_CHOOSE));
    }

    /**
     * 只做裁剪
     * @param cropCallback
     */
    public void cropPic(ImageCropCallback cropCallback){
        if(cropCallback == null){
            throw new NullPointerException("callback--不能为空");
        }
        this.cropCallback = cropCallback;
        context.startActivity(new Intent(context,ImageSingleChooseActivity.class)
                .putExtra(TYPE,TYPE_CROP));
    }

    public ImageCropCallback getCropCallback() {
        return cropCallback;
    }
}
