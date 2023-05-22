package com.qwqer.adplatform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.qwqer.adplatform.R;

public class DotView extends BaseView{

    private View dot1View;
    private View dot2View;
    private View dot3View;
    private View dot4View;
    private View dot5View;

    public DotView(Context context) {
        super(context);
        init();
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected int getLayoutViewId() {
        return R.layout.dot_view;
    }

    @Override
    protected void initViews() {
        dot1View = findViewById(R.id.dot1View);
        dot2View = findViewById(R.id.dot2View);
        dot3View = findViewById(R.id.dot3View);
        dot4View = findViewById(R.id.dot4View);
        dot5View = findViewById(R.id.dot5View);
    }

    private int maxCount = 5;

    public void setMaxCount(int maxCount){
        maxCount = maxCount < 1 ? 1 : maxCount > 5 ? 5 : maxCount;
        this.maxCount = maxCount;

        dot1View.setVisibility(maxCount > 1 ? View.VISIBLE : View.GONE);
        dot2View.setVisibility(maxCount >= 2 ? View.VISIBLE : View.GONE);
        dot3View.setVisibility(maxCount >= 3 ? View.VISIBLE : View.GONE);
        dot4View.setVisibility(maxCount >= 4 ? View.VISIBLE : View.GONE);
        dot5View.setVisibility(maxCount >= 5 ? View.VISIBLE : View.GONE);
    }

    public void onItemSelected(int index){
        dot1View.setBackgroundResource(index%maxCount == 0 ? R.drawable.ad_dot_circle_selected : R.drawable.ad_dot_circle_unselected);
        dot2View.setBackgroundResource(index%maxCount == 1 ? R.drawable.ad_dot_circle_selected : R.drawable.ad_dot_circle_unselected);
        dot3View.setBackgroundResource(index%maxCount == 2 ? R.drawable.ad_dot_circle_selected : R.drawable.ad_dot_circle_unselected);
        dot4View.setBackgroundResource(index%maxCount == 3 ? R.drawable.ad_dot_circle_selected : R.drawable.ad_dot_circle_unselected);
        dot5View.setBackgroundResource(index%maxCount == 4 ? R.drawable.ad_dot_circle_selected : R.drawable.ad_dot_circle_unselected);
    }

}
