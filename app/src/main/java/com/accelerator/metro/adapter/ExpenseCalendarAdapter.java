package com.accelerator.metro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ExpenseCalendarAdapter extends RecyclerView.Adapter<ExpenseCalendarAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<Order.ElseInfoBean> datas;

    public ExpenseCalendarAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void onRefresh(List<Order.ElseInfoBean> datas) {
        if (datas == null) {
            return;
        }
        if (this.datas != null) {
            this.datas.clear();
        }
        this.datas = datas;
    }

    public void onLoadMore(List<Order.ElseInfoBean> datas) {
        if (datas == null) {
            return;
        }
        for (Order.ElseInfoBean info : datas) {
            this.datas.add(info);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_expense_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Order.ElseInfoBean info = datas.get(position);

        switch (info.getOrder_type()) {
            //购票
            case 2:
                holder.tvDate.setText(DateUtil.getExpenseOrderDate(info.getTime()));
                holder.tvType.setText(MetroApp.getContext().getResources().getString(R.string.expense_calendar_buy));
                holder.tvType.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorRed));
                holder.tvMoney.setText("-" + info.getOrder_money() + "元");
                holder.tvMoney.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorRed));
                break;
            //退票
            case 5:
                holder.tvDate.setText(DateUtil.getExpenseOrderDate(info.getTime()));
                holder.tvType.setText(MetroApp.getContext().getResources().getString(R.string.expense_calendar_refund));
                holder.tvType.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorBlue));
                holder.tvMoney.setText("+" + info.getOrder_money() + "元");
                holder.tvMoney.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorBlue));
                break;
            //充值
            case 6:
                holder.tvDate.setText(DateUtil.getExpenseOrderDate(info.getTime()));
                holder.tvType.setText(MetroApp.getContext().getResources().getString(R.string.expense_calendar_recharge));
                holder.tvType.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorGreen));
                holder.tvMoney.setText("+" + info.getOrder_money() + "元");
                holder.tvMoney.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorGreen));
                break;
            default:
                holder.relativeLayout.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.expense_calendar_date)
        TextView tvDate;
        @Bind(R.id.expense_calendar_money)
        TextView tvMoney;
        @Bind(R.id.expense_tv_type)
        TextView tvType;
        @Bind(R.id.item_view)
        RelativeLayout relativeLayout;

         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
