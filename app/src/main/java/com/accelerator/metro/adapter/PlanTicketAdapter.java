package com.accelerator.metro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.PlanTicket;
import com.accelerator.metro.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicketAdapter extends RecyclerView.Adapter<PlanTicketAdapter.ViewHolder> {

    private List<PlanTicket.ElseInfoBean> infos;

    public PlanTicketAdapter() {
    }

    public void onRefresh(List<PlanTicket.ElseInfoBean> infos) {

        if (infos == null) {
            return;
        }

        if (this.infos != null) {
            this.infos.clear();
        }

        this.infos = infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (infos == null) {
            return;
        }

        PlanTicket.ElseInfoBean info = infos.get(position);

        holder.tvStartDate.setText(DateUtil.getOrderDate(Integer.parseInt(info.getWork_time())));
        holder.tvDays.setText(String.valueOf(info.getDays()));
        holder.tvStartStation.setText(info.getStart_site());
        holder.tvEndStation.setText(info.getEnd_site());

        String type = info.getAuto_type();

        if (type.equals("0")) {
            holder.tvSate.setText(MetroApp.getContext().getResources().getString(R.string.plan_ticket_week_tickets));
        } else if (type.equals("1")) {
            holder.tvSate.setText(MetroApp.getContext().getResources().getString(R.string.plan_ticket_month_tickets));
        } else if (type.equals("2")) {
            holder.tvSate.setText(MetroApp.getContext().getResources().getString(R.string.plan_ticket_others_tickets));
        }

    }

    @Override
    public int getItemCount() {
        return infos == null ? 0 : infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.plan_ticket_start_date)
        TextView tvStartDate;
        @Bind(R.id.plan_ticket_order_state)
        TextView tvSate;
        @Bind(R.id.plan_ticker_station_start)
        TextView tvStartStation;
        @Bind(R.id.plan_ticker_station_end)
        TextView tvEndStation;
        @Bind(R.id.plan_ticker_days)
        TextView tvDays;

         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
