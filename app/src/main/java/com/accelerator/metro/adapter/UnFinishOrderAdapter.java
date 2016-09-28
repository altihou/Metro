package com.accelerator.metro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class UnFinishOrderAdapter extends RecyclerView.Adapter<UnFinishOrderAdapter.ViewHolder> {

    private List<Order.ElseInfoBean> datas;
    private ButtonClickListener listener;

    public UnFinishOrderAdapter() {
    }

    public interface ButtonClickListener {
       void onCancelClick(Order.ElseInfoBean info);
       void onPayClick(Order.ElseInfoBean info);
    }

    public void setButtonClickListener(ButtonClickListener listener){
        this.listener=listener;
    }

    public void onRefresh(List<Order.ElseInfoBean> datas){
        if (datas==null){
            return;
        }
        if (this.datas!=null){
            this.datas.clear();
        }
        this.datas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_un_finish_order, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (datas==null){
            return;
        }

        Order.ElseInfoBean info=datas.get(position);

        holder.tvDate.setText(DateUtil.getOrderDate(info.getTime()));
        holder.tvStart.setText(info.getStart_point());

        if (info.getOrder_type() == 7 && info.getIs_complete() == 1){
            holder.btnCancel.setVisibility(View.GONE);
            holder.tvPrice.setText(8 + "元");
            holder.tvCount.setText("共" + 1 + "张");
            holder.tvEnd.setText("***");
            holder.tvState.setText(MetroApp.getContext().getResources().getString(R.string.un_finish_order_un_normal));
        }else {
            String count = info.getCount();
            String endStation = info.getEnd_point();
            String price = info.getOrder_money();
            holder.tvPrice.setText(price + "元");
            holder.tvCount.setText("共" + count + "张");
            holder.tvEnd.setText(endStation);
            holder.tvState.setText(MetroApp.getContext().getResources().getString(R.string.un_finish_order_state_not_pay));
        }

        holder.itemView.setTag(info);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.un_finish_order_date)
        TextView tvDate;
        @Bind(R.id.un_finish_order_state)
        TextView tvState;
        @Bind(R.id.un_finish_order_station_start)
        TextView tvStart;
        @Bind(R.id.un_finish_order_station_end)
        TextView tvEnd;
        @Bind(R.id.un_finish_order_price)
        TextView tvPrice;
        @Bind(R.id.un_finish_order_count)
        TextView tvCount;
        @Bind(R.id.un_finish_order_btn_cancel)
        Button btnCancel;
        @Bind(R.id.un_finish_order_btn_pay)
        Button btnPay;

         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.un_finish_order_btn_cancel)
        public void onCancelClick(View view){
            if (listener!=null){
                Order.ElseInfoBean info= (Order.ElseInfoBean) itemView.getTag();
                listener.onCancelClick(info);
            }
        }

        @OnClick(R.id.un_finish_order_btn_pay)
        public void onPayClick(View view){
            if (listener!=null){
                Order.ElseInfoBean info= (Order.ElseInfoBean) itemView.getTag();
                listener.onPayClick(info);
            }
        }

    }

}
