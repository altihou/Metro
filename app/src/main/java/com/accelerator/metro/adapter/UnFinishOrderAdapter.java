package com.accelerator.metro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accelerator.metro.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zoom on 2016/5/29.
 */
public class UnFinishOrderAdapter
        extends RecyclerView.Adapter<UnFinishOrderAdapter.OrderNormalViewHolder> {

    private List<String> lists;

    public UnFinishOrderAdapter() {
    }

    public void setData(List<String> lists) {
        if (lists == null)
            return;
        this.lists = lists;
    }

    public void setRefresh(List<String> lists) {
        if (lists == null)
            return;
        this.lists.clear();
        this.lists = lists;
    }

    @Override
    public OrderNormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderNormalViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderNormalViewHolder holder, int position) {

        if (lists != null) {
            holder.textView.setText(lists.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    class OrderNormalViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_test)
        TextView textView;

        public OrderNormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
