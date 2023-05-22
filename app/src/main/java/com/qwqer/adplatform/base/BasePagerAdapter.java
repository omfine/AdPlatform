package com.qwqer.adplatform.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;

import com.qwqer.adplatform.utils.AdLog;

import java.util.List;
/**
 * PagerAdapter的基类。
 * @param <T>
 */
public class BasePagerAdapter<T extends View> extends PagerAdapter {

    private Context context = null;
    private List<T> list = null;

    public BasePagerAdapter(List<T> list) {
        this.list = list;
    }

    public BasePagerAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        //return list.size();
        int size = list.size();
        return  size < 2 ? size : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (list.size() < 2){
            AdLog.e("==================destroyItem :: " + list.size());
            container.removeView(list.get(position));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (list.size() < 2){
            container.addView(list.get(position));
            return list.get(position);
        }else {
            View view = list.get(position%list.size());
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            container.addView(view);
            return view;
        }
    }

}
