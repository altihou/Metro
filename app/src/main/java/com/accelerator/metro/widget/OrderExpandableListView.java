package com.accelerator.metro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Nicholas on 2016/8/4.
 */
public class OrderExpandableListView extends ExpandableListView {

    public OrderExpandableListView(Context context) {
        super(context);
    }

    public OrderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
