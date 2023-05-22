package com.qwqer.adplatform.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.qwqer.adplatform.init.QwQerAdHelper;
/**
 * Glide封装类。
 * @author E
 */
public class GlideHelper {

    public static void display(ImageView imageView , String url){
        Glide.with(QwQerAdHelper.getContext()).load(url).into(imageView);
    }

    public static void displayCenterCrop(ImageView imageView , String url){
        RequestOptions options = new RequestOptions();
        options.transform(new CenterCrop());
        Glide.with(QwQerAdHelper.getContext()).load(url).apply(options).into(imageView);
    }

    private static float getDensity(){
        return QwQerAdHelper.getContext().getResources().getDisplayMetrics().density;
    }

    public static void  displayDef(ImageView imageView , String url , int defImageResId){
        RequestOptions options = new RequestOptions();
        options.placeholder(defImageResId);
        Glide.with(QwQerAdHelper.getContext()).load(url)
                .thumbnail(loadTransform(QwQerAdHelper.getContext(), defImageResId))
                .apply(options).into(imageView);
    }

    public static void display(ImageView imageView , String url , int corner){
        int targetCorner = (int) (corner * getDensity());
        targetCorner = (targetCorner < 1) ? 1 : targetCorner;
        RoundedCorners roundedCorners = new RoundedCorners(targetCorner);
        RequestOptions options = new RequestOptions();
        options.transform(new CenterCrop(), roundedCorners);

        Glide.with(QwQerAdHelper.getContext()).load(url).apply(options).into(imageView);
    }

    public static void display(ImageView imageView, String url, int corner, int defImageResId) {
        int targetCorner = (int) (corner * getDensity());
        targetCorner = (targetCorner < 1) ? 1 : targetCorner;
        RoundedCorners roundedCorners = new RoundedCorners(targetCorner);
        RequestOptions options = new RequestOptions();
        options.placeholder(defImageResId);
        options.transform(new CenterCrop(), roundedCorners);
        Glide.with(QwQerAdHelper.getContext()).load(url)
                .thumbnail(loadTransform(QwQerAdHelper.getContext(), defImageResId, targetCorner))
                .apply(options).into(imageView);
    }


    /**
     * Glide显示图片。
     * @param imageView ImageView
     * @param reSidUrl 图片地址
     * @param corner  角度
     */
    public static void  display(ImageView imageView, int reSidUrl, int corner) {
        int targetCorner = (int) (corner * getDensity());
        targetCorner = (targetCorner < 1)? 1 : targetCorner;
        RoundedCorners roundedCorners = new RoundedCorners(targetCorner);
        RequestOptions options = new RequestOptions();
        options.transform(new CenterCrop(), roundedCorners);
        Glide.with(QwQerAdHelper.getContext()).load(reSidUrl).apply(options).into(imageView);
    }

    /**
     * 对应的ScaleType 为 fitXY
     * @param imageView
     * @param url
     * @param corner
     */
    public static void displayFitXY(ImageView imageView, int url, int corner) {
        int targetCorner = (int) (corner * getDensity());
        targetCorner = targetCorner < 1 ? 1 : targetCorner;
        RoundedCorners roundedCorners = new RoundedCorners(targetCorner);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(QwQerAdHelper.getContext()).load(url).apply(options).into(imageView);
    }

    private static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, int radius) {
        RoundedCorners roundedCorners = new RoundedCorners(radius);
        RequestOptions options = new RequestOptions();
        options.transform(new CenterCrop(), roundedCorners);
        return Glide.with(context).load(placeholderId).apply(options);
    }

    private static RequestBuilder<Drawable> loadTransform(Context context , @DrawableRes int placeholderId) {
        RequestOptions options = new RequestOptions();
        return Glide.with(context).load(placeholderId).apply(options);
    }

}
