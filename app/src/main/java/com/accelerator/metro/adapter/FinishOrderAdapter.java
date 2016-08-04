package com.accelerator.metro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.utils.DateUtil;

import java.util.List;

/**
 * Created by zoom on 2016/5/29.
 */
public class FinishOrderAdapter extends BaseExpandableListAdapter {

    private String[] groupNames = new String[]{
            MetroApp.getContext().getResources().getString(R.string.finish_order_group_name1),
            MetroApp.getContext().getResources().getString(R.string.finish_order_group_name2)};

    private List<List<Order.ElseInfoBean>> allOrders;

    private OnButtonClickListener listener;

    private Context context;

    public FinishOrderAdapter(Context context) {
        this.context = context;
    }

    public void onRefresh(List<List<Order.ElseInfoBean>> allOrders) {
        if (allOrders == null) {
            return;
        }

        if (this.allOrders != null) {
            this.allOrders.clear();
        }

        this.allOrders = allOrders;
        notifyDataSetChanged();
    }

    public List<List<Order.ElseInfoBean>> getDatas() {
        if (allOrders != null) {
            return allOrders;
        }
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return allOrders == null ? 0 : allOrders.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return allOrders.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupId) {
        return groupId;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        GroupViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_expandable_listview, null);
            viewHolder = new GroupViewHolder();
            viewHolder.groupTitle = (TextView) convertView.findViewById(R.id.order_finish_group_title);
            viewHolder.groupIndicator = (ImageView) convertView.findViewById(R.id.order_finish_group_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        if (isExpanded) {
            viewHolder.groupIndicator.setBackgroundResource(R.drawable.ic_expand_more_blue_500_24dp);
        } else {
            viewHolder.groupIndicator.setBackgroundResource(R.drawable.ic_chevron_right_blue_500_24dp);
        }

        viewHolder.groupTitle.setText(groupNames[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup viewGroup) {

        ChildViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child_expandable_listview, null);
            viewHolder = new ChildViewHolder();
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.finish_order_delete);
            viewHolder.tvOrderNum = (TextView) convertView.findViewById(R.id.finish_order_num);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.finish_order_date);
            viewHolder.tvEnd = (TextView) convertView.findViewById(R.id.finish_order_station_end);
            viewHolder.tvStart = (TextView) convertView.findViewById(R.id.finish_order_station_start);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.finish_order_price);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.finish_order_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        final Order.ElseInfoBean info = allOrders.get(groupPosition).get(childPosition);

        int type = info.getOrder_type();

        String orderType = "";

        switch (type) {
            case 2:
                orderType = MetroApp.getContext().getResources().getString(R.string.finish_order_state2);
                viewHolder.tvType.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorGreen));
                viewHolder.btnDelete.setText(MetroApp.getContext().getResources().getString(R.string.finish_order_refund));
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.refundOrder(info.getOrder_sn());
                        }
                    }
                });
                break;
            case 3:
                orderType = MetroApp.getContext().getResources().getString(R.string.finish_order_state3);
                viewHolder.btnDelete.setText(MetroApp.getContext().getResources().getString(R.string.finish_order_delete));
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.deleteOrder(info.getOrder_sn());
                        }
                    }
                });
                break;
            case 5:
                orderType = MetroApp.getContext().getResources().getString(R.string.finish_order_state5);
                viewHolder.tvType.setTextColor(MetroApp.getContext().getResources().getColor(R.color.googleColorRed));
                viewHolder.btnDelete.setText(MetroApp.getContext().getResources().getString(R.string.finish_order_delete));
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.deleteOrder(info.getOrder_sn());
                        }
                    }
                });
                break;
        }

        viewHolder.tvOrderNum.setText(info.getOrder_sn());
        viewHolder.tvType.setText(orderType);
        viewHolder.tvDate.setText(DateUtil.getOrderDate(info.getTime()));
        viewHolder.tvStart.setText(info.getStart_point());
        viewHolder.tvEnd.setText(info.getEnd_point());
        viewHolder.tvPrice.setText(info.getOrder_money());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnButtonClickListener {
        void deleteOrder(String orderSn);

        void refundOrder(String orderSn);
    }

    public void setButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    class GroupViewHolder {
        public TextView groupTitle;
        public ImageView groupIndicator;
    }

    class ChildViewHolder {
        public TextView tvOrderNum;
        public TextView tvDate;
        public TextView tvType;
        public TextView tvStart;
        public TextView tvEnd;
        public TextView tvPrice;
        public Button btnDelete;
    }
}
